package com.ceshiren.aitestmini.util;

import com.ceshiren.aitestmini.dto.OperateJenkinsJobDTO;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.*;

import java.io.*;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

import com.offbytwo.jenkins.client.JenkinsHttpClient;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static java.lang.Thread.sleep;

public class JenkinsUtil {
    //1. 获取 Jenkins 的 job XML文件
    private static String getJobXml(String path) throws Exception {

        File file = new File(path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        StreamResult streamResult = new StreamResult(outputStream);

        transformer.transform(domSource, streamResult);
        String xmlString = outputStream.toString();
        System.out.println(xmlString);
        return xmlString;
    }

    //2.创建 JenkinsHttpClient 对象
    //  根据 Jenkins 客户端创建 JenkinsServer 对象

    //根据xml构建job
    /**
     * 1. 获取 Jenkins 的 job XML文件
     * 2. 获取 Jenkins 信息
     * 3. 根据 Jenkins 信息创建 JenkinsHttpClient 对象
     * 4. 根据 Jenkins 客户端创建 JenkinsServer 对象
     * 5. 更新job -- 更新配置参数
     *     如果job存在，更新；如果不存在，创建。
     * 6. 获取job信息
     *     获取Jenkins所有的job信息
     *     获取单个的job信息
     * 7. job构建参数组装
     * 8. 构建
     * @param operateJenkinsJobDto
     * @return
     * @throws Exception
     */
    public static OperateJenkinsJobDTO buildWithXML(OperateJenkinsJobDTO operateJenkinsJobDto) throws Exception {

        //1. 获取jenkins参数
        //jenkins地址
        String jenkinsUrl = operateJenkinsJobDto.getJenkinsUrl();
        //jenkins用户
        String jenkinsUserName = operateJenkinsJobDto.getJenkinsUserName();
        //Jenkins密码
        String jenkinsPassword = operateJenkinsJobDto.getJenkinsPassword();
        //Jenkins 参数
        Map<String, String> params = operateJenkinsJobDto.getParams();

        //2. 获取Jenkins job模板xml数据
        String xmlPath = "src/main/resources/jenkins/"+operateJenkinsJobDto.getJob()+".xml";
        String jobConfigXml = getJobXml(xmlPath);
        jobConfigXml = StringUtils.replace(jobConfigXml,"${caseURL}",params.get("testCommand"));
        System.out.println("---jobConfigXml----");
        System.out.println(jobConfigXml);
        //3.创建 JenkinsServer
        //创建Jenkins客户端
        JenkinsHttpClient jenkinsHttpClient = new JenkinsHttpClient(new URI(jenkinsUrl), jenkinsUserName, jenkinsPassword);
        try {
            JenkinsServer jenkinsServer = new JenkinsServer(jenkinsHttpClient);
            //4. 更新job -- 更新配置参数
            //需要提前在Jenkins上创建job，才能更新job
            try{
                jenkinsServer.updateJob(null, operateJenkinsJobDto.getJob(), jobConfigXml,true);
            }catch (Exception e){
                jenkinsServer.createJob(null, operateJenkinsJobDto.getJob(), jobConfigXml, true);
            }
            //获取当前Jenkins服务器上所有的job
            Map<String, Job> jobMap = jenkinsServer.getJobs();
            //根据当前的job名称查询出当前job
            Job job = jobMap.get(operateJenkinsJobDto.getJob());
            /**
             * job.getName  mini01_job
             * job.getUrl   http://81.70.96.121:10240/job/mini01_job/
             * job.getFullName()    null
             */
            operateJenkinsJobDto.setJenkinsUrl(job.getUrl());

            //构建当前job     Jenkins配置关闭CSRF 否则报错500
            if(Objects.isNull(params)){
                job.build(true);
            }else {
              job.build(params, true);
            }
            System.out.println("开始构建...");
            JobWithDetails jobWithDetails = null;
            jobWithDetails = job.details();
            int nextBuildNumber = jobWithDetails.getNextBuildNumber();
            operateJenkinsJobDto.setJobId(nextBuildNumber);
            while (true){
                System.out.println("nextBuildNumber:"+nextBuildNumber);
                if(isFinished(job, nextBuildNumber)){
                    System.out.println("构建完成...");
                    break;
                }
                sleep(2000);

            }
            BuildResult buildResult = jobWithDetails.getLastBuild().details().getResult();

            operateJenkinsJobDto.setJobStatus(buildResult.toString());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jenkinsHttpClient.close();
        }

        //参数{testCommand=pwd}
        System.out.println(params);
        System.out.println(operateJenkinsJobDto);
        return operateJenkinsJobDto;
    }

    /*
    **
     * 判断指定的构建版本号是否执行完成
     *
     * @param number
     *      构建版本号
     * @param jobName
     *      构建名称
     * @return true为构建完成，false为未构建完成
     */
    public static boolean isFinished(Job job1, int number) {
        boolean isBuilding = false;
        if (number <= 0) {
            throw new IllegalArgumentException("jenkins build number must greater than 0!");
        }
        try {
            JobWithDetails job = job1.details();
            System.out.println("job:"+job);
            // build 如果为空则证明正在构建，走else了
            Build buildByNumber = job.getBuildByNumber(number);
            System.out.println("buildByNumber:"+buildByNumber);
            if (null != buildByNumber) {
                BuildWithDetails details = buildByNumber.details();
                if (null != details) {
                    isBuilding = details.isBuilding();
                } else {
                    isBuilding = true;
                }
            } else {
                isBuilding = true;
            }

            return !isBuilding;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
