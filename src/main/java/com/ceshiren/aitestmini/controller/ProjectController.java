package com.ceshiren.aitestmini.controller;

import com.ceshiren.aitestmini.dto.*;
import com.ceshiren.aitestmini.dto.task.TestTaskAddDTO;
import com.ceshiren.aitestmini.dto.task.TestTaskDTO;
import com.ceshiren.aitestmini.dto.task.TestTaskRunDTO;
import com.ceshiren.aitestmini.dto.task.TestTaskStatusDTO;
import com.ceshiren.aitestmini.service.ProjectService;
import com.ceshiren.aitestmini.util.R;
import com.ceshiren.aitestmini.util.ResultCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("project")
//项目任务请求与前端交互的类
public class ProjectController {
    private ProjectService projectService;

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }
    //新增测试用例
    @ApiOperation(value = "新增测试用例")
    @PostMapping("/case")
    public R createTestCase(@RequestBody TestCaseDTO testCaseDto) {
        /**
         * 测试用例 不能Null、用例名不能为空、用例数据不能为空
         */
        if(Objects.isNull(testCaseDto)){
            return R.error(ResultCode.PARAMETER_ERROR);
        }

        if(!StringUtils.hasText(testCaseDto.getCaseName())){
            return R.error(ResultCode.PARAMETER_ERROR).message("用例名称不能为空");
        }
        if(!StringUtils.hasText(testCaseDto.getCaseData())){
            return R.error(ResultCode.PARAMETER_ERROR).message("用例数据不能为空");
        }
        int addCase = projectService.createTestCase(testCaseDto);
        return R.ok().data(testCaseDto).message(addCase + "条测试用例添加成功");
    }
    @ApiOperation(value = "根据ID获取用例数据")
    @GetMapping("case/data/{caseId}")
    public R getOneCase(@PathVariable Integer caseId) {
        TestCaseDTO testCaseDTO = new TestCaseDTO();
        testCaseDTO.setId(caseId);
        //转为对象，下次根据用例名或者根据用例其他的获取用例数据都可以
        testCaseDTO = projectService.getTestCase(testCaseDTO);
        if(Objects.isNull(testCaseDTO))
            return R.error(ResultCode.PARAMETER_NOT_EXIST).message("用例不存在");
        return R.ok().data(testCaseDTO);
    }
    @ApiOperation(value = "用例列表查询")
    @GetMapping("case/list")
    public R getTestCaseList(){
        List<TestCaseDTO> testCaseList = projectService.getTestCaseList();
        return R.ok().data(testCaseList);
    }

    //用例任务的创建
    @ApiOperation(value = "用例任务的创建")
    @PostMapping("/task")
    public R createTask(@RequestBody TestTaskAddDTO testTaskDto) {
        if(Objects.isNull(testTaskDto)){
            return R.error(ResultCode.PARAMETER_ERROR);
        }
        //任务信息对象中名称不能为空
        if(Objects.isNull(testTaskDto.getTaskName())){
            return R.error(ResultCode.PARAMETER_ERROR).message("任务名称不能为空");
        }

        List<Integer> testCaseIdList = testTaskDto.getTestCaseIdList();
        //null || []
        int testCaseIdListsize = testCaseIdList.size();
        if(Objects.isNull(testCaseIdList) || testCaseIdListsize==0){
            return R.error(ResultCode.PARAMETER_ERROR).message("用例选择列表不能为空");
        }
        int task = projectService.createTask(testTaskDto);
        if(task > 0) {
            if(task == testCaseIdListsize)
                return R.ok().data(testTaskDto).message("1条测试任务对应" + task + "条测试用例全部添加成功");
            else
                return R.ok().data(testTaskDto).message("1条测试任务"+ testCaseIdListsize +"，成功添加了" + task + "条测试用例");
        }
        return R.error(ResultCode.PARAMETER_NOT_EXIST).data(testTaskDto);
    }

    @ApiOperation(value = "根据ID获取任务详情")
    @GetMapping("task/data/{taskId}")
    public R getOneTask(@PathVariable Integer taskId){
        TestTaskDTO testTaskDTO = new TestTaskDTO();
        testTaskDTO.setId(taskId);
        //转为对象，下次根据用例名或者根据用例其他的获取用例数据都可以
        testTaskDTO = projectService.getTestTask(testTaskDTO);
        return R.ok().data(testTaskDTO);
    }
    @ApiOperation(value = "任务列表查询")
    @GetMapping("task/list")
    public R getTestTaskList(){
        List<TestTaskDTO> testTaskList = projectService.getTestTaskList();
        return R.ok().data(testTaskList);
    }


    @PostMapping("/run")
    @ApiOperation(value = "执行测试任务")
    public R runTask(@RequestBody TestTaskRunDTO testTaskRunDTO) {

        //参数校验
        if(Objects.isNull(testTaskRunDTO)){
            return R.error(ResultCode.PARAMETER_ERROR).message("参数不能为空");
        }
        if(Objects.isNull(testTaskRunDTO.getId())){
            return R.error(ResultCode.PARAMETER_ERROR).message("任务id不能为空");
        }
        TestTaskDTO testTaskDTO = new TestTaskDTO();
        BeanUtils.copyProperties(testTaskRunDTO, testTaskDTO);
        testTaskDTO = projectService.runTask(testTaskDTO);
        return R.ok().data(testTaskDTO);
    }


    //http://81.70.96.121:10240/job/mini01_job/allure/
    @ApiOperation(value = "获取报告")
    @GetMapping("/report/{taskId}")
    public R getReport(@PathVariable Integer taskId){
        TestTaskDTO testTaskDTO = new TestTaskDTO();
        testTaskDTO.setId(taskId);
        String report = projectService.getReport(testTaskDTO);
        return R.ok().data(report);

    }






}
