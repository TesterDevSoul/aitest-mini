package com.ceshiren.aitestmini.service;

import com.ceshiren.aitestmini.dto.OperateJenkinsJobDTO;
import com.ceshiren.aitestmini.entity.TestTask;

public interface JenkinsService {
    OperateJenkinsJobDTO runTask(TestTask testTask);
    String getAllureReport(TestTask testTask);


}
