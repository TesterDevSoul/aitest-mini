package com.ceshiren.aitestmini.service.impl;

import com.ceshiren.aitestmini.dto.OperateJenkinsJobDTO;
import com.ceshiren.aitestmini.entity.TestTask;
import com.ceshiren.aitestmini.exception.ServiceException;
import com.ceshiren.aitestmini.service.JenkinsService;
import com.ceshiren.aitestmini.util.JenkinsUtil;
import com.offbytwo.jenkins.model.Job;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class JenkinsServiceImpl implements JenkinsService {

    //jenkins地址
    @Value("${jenkins.url}")
    private String jenkinsUrl;
    //Jenkins用户
    @Value("${jenkins.user}")
    private String jenkinsUserName;
    //Jenkins密码
    @Value("${jenkins.pwd}")
    private String jenkinsPassword;
    @Value("${jenkins.job}")
    private String job;



    @Override
    public OperateJenkinsJobDTO runTask(TestTask testTask) {
        OperateJenkinsJobDTO operateJenkinsJobDto = new OperateJenkinsJobDTO();
        //http://81.70.96.121:10240/
        operateJenkinsJobDto.setJenkinsUrl(jenkinsUrl);
        operateJenkinsJobDto.setJenkinsUserName(jenkinsUserName);
        operateJenkinsJobDto.setJenkinsPassword(jenkinsPassword);
        operateJenkinsJobDto.setJob(job);
        //组装Jenkins构建参数
        Map<String, String> params = new HashMap<>();
        //job对应写入的参数
        params.put("testCommand",testTask.getTestCommand());
        operateJenkinsJobDto.setParams(params);
        System.out.println(operateJenkinsJobDto);
        try {
            //调用Jenkins API
            OperateJenkinsJobDTO operateJenkinsJobDTO = JenkinsUtil.buildWithXML(operateJenkinsJobDto);
            //http://81.70.96.121:10240/job/mini01_job/
            operateJenkinsJobDto.setJenkinsUrl(operateJenkinsJobDTO.getJenkinsUrl());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        return operateJenkinsJobDto;

    }

    @Override
    public String getAllureReport(TestTask testTask) {
        System.out.println("getAllureReport:"+testTask);
        return StringUtils.applyRelativePath(testTask.getBuildUrl(),"allure");
    }


}
