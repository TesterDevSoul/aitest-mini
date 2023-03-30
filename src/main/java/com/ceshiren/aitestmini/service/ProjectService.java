package com.ceshiren.aitestmini.service;

import com.ceshiren.aitestmini.dto.TestCaseDTO;
import com.ceshiren.aitestmini.dto.task.TestTaskAddDTO;
import com.ceshiren.aitestmini.dto.task.TestTaskDTO;

import java.util.List;

public interface ProjectService {
    int createTestCase(TestCaseDTO testCaseDto);

    TestCaseDTO getTestCase(TestCaseDTO testCaseDTO);

    List<TestCaseDTO> getTestCaseList();

    int createTask(TestTaskAddDTO testTaskDto);

    TestTaskDTO getTestTask(TestTaskDTO testTaskDTO);

    List<TestTaskDTO> getTestTaskList();


    TestTaskDTO runTask(TestTaskDTO testTaskDTO);

    String getReport(TestTaskDTO taskId);
}
