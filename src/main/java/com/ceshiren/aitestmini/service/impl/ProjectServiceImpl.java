package com.ceshiren.aitestmini.service.impl;

import com.ceshiren.aitestmini.common.Constants;
import com.ceshiren.aitestmini.converter.TestCaseConverter;
import com.ceshiren.aitestmini.converter.TestTaskConverter;
import com.ceshiren.aitestmini.dao.TestCaseMapper;
import com.ceshiren.aitestmini.dao.TestTaskCaseRelMapper;
import com.ceshiren.aitestmini.dao.TestTaskMapper;
import com.ceshiren.aitestmini.dto.*;
import com.ceshiren.aitestmini.dto.task.TestTaskAddDTO;
import com.ceshiren.aitestmini.dto.task.TestTaskDTO;
import com.ceshiren.aitestmini.entity.TestCase;
import com.ceshiren.aitestmini.entity.TestTask;
import com.ceshiren.aitestmini.entity.TestTaskCaseRel;
import com.ceshiren.aitestmini.service.JenkinsService;
import com.ceshiren.aitestmini.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {
    private TestCaseMapper testCaseMapper;

    private TestCaseConverter testCaseConverter;
    @Autowired
    public void setTestCaseMapper(TestCaseMapper testCaseMapper) {
        this.testCaseMapper = testCaseMapper;
    }

    @Autowired
    public void setTestCaseConverter(TestCaseConverter testCaseConverter) {
        this.testCaseConverter = testCaseConverter;
    }


    private TestTaskConverter testTaskConverter;
    @Autowired
    public void setTestTaskConverter(TestTaskConverter testTaskConverter) {
        this.testTaskConverter = testTaskConverter;
    }
    private TestTaskMapper testTaskMapper;
    @Autowired
    public void setTestTaskMapper(TestTaskMapper testTaskMapper) {

        this.testTaskMapper = testTaskMapper;
    }

    private TestTaskCaseRelMapper testTaskCaseRelMapper;

    @Autowired
    public void setTestTaskCaseRelMapper(TestTaskCaseRelMapper testTaskCaseRelMapper) {
        this.testTaskCaseRelMapper = testTaskCaseRelMapper;
    }

    private JenkinsService jenkinsService;

    @Autowired
    public void setJenkinsService(JenkinsService jenkinsService) {
        this.jenkinsService = jenkinsService;
    }
    @Override
    public int createTestCase(TestCaseDTO testCaseDto) {
        //TestCaseDTO 转为数据库表 TestCase 2种方式：BeanUtils
        log.info("添加用例传入:{}",testCaseDto);
        //Spring自带工具类 - BeanUtils
        //TestCase testCase = new TestCase();
        //BeanUtils.copyProperties(testCaseDto, testCase);
        //mapstruct
        TestCase testCase = testCaseConverter.testCaseDTOForTestCase(testCaseDto);
        //实体类转换完成后，往数据库添加 mapper操作
        testCase.setCreateTime(new Date());//添加创建时间
        testCase.setUpdateTime(new Date());//添加更新时间
        log.info("数据库添加:{}", testCase);
        //Mybatis 是否开启主键自动生成，并将自动生成的主键值回填到对象中。
        //Mybatis 会使用数据库的自增长功能生成一个主键值，
        // 并将该值回填到对象中，从而避免了手动设置主键值的繁琐操作。
        return testCaseMapper.insertUseGeneratedKeys(testCase);
    }

    @Override
    public TestCaseDTO getTestCase(TestCaseDTO testCaseDTO){
        TestCase testCase = testCaseConverter.testCaseDTOForTestCase(testCaseDTO);
        testCase = testCaseMapper.selectOne(testCase);
        System.out.println(testCase);
        return testCaseConverter.testCaseForTestCaseDTO(testCase);
    }

    @Override
    public List<TestCaseDTO> getTestCaseList() {
        List<TestCase> testCaseList = testCaseMapper.selectAll();
        System.out.println(testCaseList);
        return testCaseConverter.testCaseListForTestCaseDTOList(testCaseList);
    }

    @Override
    //由于不只是往任务表里面插入数据，还需要往关联表中添加数据，所以开启事务
    @Transactional(rollbackFor = Exception.class)
    //只要有报错就回滚 指定事务回滚的异常类型，如果发生该类型的异常，事务将回滚。
    public int createTask(TestTaskAddDTO testTaskDto) {
        log.info("创建任务传入:{}",testTaskDto);
        //根据用例ID查询出来所有的用例
        List<Integer> testCaseIdList = testTaskDto.getTestCaseIdList();
        //将List转换成一个Stream对象
        String testCaseIds = testCaseIdList.stream()
                //使用map()方法将每个Integer对象转换为它的字符串表示
                .map(String::valueOf)
                //使用Collectors.joining()方法将所有字符串连接起来，中间用逗号加空格分隔。
                .collect(Collectors.joining(", "));
        List<TestCase> testCases = testCaseMapper.selectByIds(testCaseIds);
        //根据传入的ID查找测试用例
        System.out.println(testCases);
        //如果测试用例不为空
        if(Objects.nonNull(testCases) && testCases.size()>0) {
            TestTask testTask = testTaskConverter.testTaskAddDtoForTestTask(testTaskDto);
            //Status 类型改为Integer
            testTask.setStatus(Constants.STATUS_ONE);
            testTask.setCreateTime(new Date());
            testTask.setUpdateTime(new Date());
            //要执行的命令
            StringBuilder sb = new StringBuilder();
            //默认命令为
            sb.append("pwd");
            testTask.setTestCommand(sb.toString());
            int i = testTaskMapper.insertUseGeneratedKeys(testTask);
            //测试任务和测试用例对应关系添加 test_task_case_rel
            //一个测试任务 对应 多条测试用例，所以放在list内一次性添加到数据库
            List<TestTaskCaseRel> testTaskCaseList = new ArrayList<>();
            //根据查询出来的testcase有的ID进行插入测试任务对应的用例ID
            testCases.forEach(testCase -> {
                TestTaskCaseRel testTaskCaseRel = new TestTaskCaseRel();
                testTaskCaseRel.setTaskId(testTask.getId());
                testTaskCaseRel.setCaseId(testCase.getId());
                testTaskCaseList.add(testTaskCaseRel);
            });
            //数据进行批量插入
            return testTaskCaseRelMapper.insertList(testTaskCaseList);
        }
        return 0;

    }

    @Override
    public TestTaskDTO getTestTask(TestTaskDTO testTaskDTO) {
        TestTask testTask = testTaskConverter.testTaskDtoForTestTask(testTaskDTO);
        return getTestTaskDTO(testTask);
    }

    @Override
    public List<TestTaskDTO> getTestTaskList() {
        List<TestTaskDTO> testTaskDTOList = new ArrayList<>();
        List<TestTask> testTaskList = testTaskMapper.selectAll();
        System.out.println(testTaskList);
        //没有任务列表
        if(Objects.isNull(testTaskList)){
            return null;
        }
        testTaskList.forEach(testTask -> {
            TestTaskDTO testTaskDTO = getTestTaskDTO(testTask);
            testTaskDTOList.add(testTaskDTO);
        });

        System.out.println(testTaskDTOList);

        return testTaskDTOList;
    }

    //1.更改任务的状态；2.调用Jenkins接口执行。
    //添加事务保证两个步骤同时执行或同时不执行
    //Jenkins发起调用和Jenkins执行为异步，不是很精准
    @Transactional(rollbackFor = Exception.class)
    @Override
    public TestTaskDTO runTask(TestTaskDTO testTaskDTO) {
        TestTask testTask = testTaskConverter.testTaskDtoForTestTask(testTaskDTO);

        //1.更改任务的状态；
        //任务查看是否存在
        testTask = testTaskMapper.selectOne(testTask);
        //参数校验
        if(Objects.isNull(testTask)){
            return null;
        }

        //2.调用Jenkins接口执行。
        OperateJenkinsJobDTO operateJenkinsJobDTO = jenkinsService.runTask(testTask);


        //如果任务存在 更新任务状态为 执行中
        testTask.setStatus(Constants.STATUS_THREE);
        testTask.setBuildUrl(operateJenkinsJobDTO.getJenkinsUrl());
        testTask.setUpdateTime(new Date());
        testTask.setJobId(operateJenkinsJobDTO.getJobId());
        testTaskMapper.updateByPrimaryKeySelective(testTask);
        System.out.println(testTask);
        return testTaskConverter.testTaskForTestTaskDto(testTask);
    }

    @Override
    public String getReport(TestTaskDTO testTaskDTO) {
        //http://81.70.96.121:10240/job/mini01_job/allure/

        TestTask testTask = testTaskConverter.testTaskDtoForTestTask(testTaskDTO);
        testTask = testTaskMapper.selectOne(testTask);
        String allureReport = jenkinsService.getAllureReport(testTask);
        return allureReport;
    }


    private TestTaskDTO getTestTaskDTO(TestTask testTask) {
        //查询任务详情
        testTask = testTaskMapper.selectOne(testTask);

        //查询用例列表
        Example testTaskCaseRel = new Example(TestTaskCaseRel.class);
        Example.Criteria criteria = testTaskCaseRel.createCriteria();
        //声明的实体类属性
        criteria.andEqualTo("taskId", testTask.getId());
        //根据任务ID查询出来关联的用例ID
        List<TestTaskCaseRel> testTaskCaseRels = testTaskCaseRelMapper.selectByExample(testTaskCaseRel);
        System.out.println("testTaskCaseRels:" + testTaskCaseRels);//每个任务对应的用例
        List<Integer> caseIdList = new ArrayList<>();
        //用例放在数组内
        testTaskCaseRels.forEach(testTaskCaseRel1 -> caseIdList.add(testTaskCaseRel1.getCaseId()));

        //任务拼接
        TestTaskDTO testTaskDTO = testTaskConverter.testTaskForTestTaskDto(testTask);
        testTaskDTO.setTestCaseIdList(caseIdList);
        return testTaskDTO;
    }


}
