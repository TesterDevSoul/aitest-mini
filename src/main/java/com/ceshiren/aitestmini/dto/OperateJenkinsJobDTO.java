package com.ceshiren.aitestmini.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class OperateJenkinsJobDTO {

    private String jenkinsUrl;

    private String jenkinsUserName;

    private String jenkinsPassword;

    private String job;
    //job当前构建的url
    private String jobUrl;
    private Integer jobId;
    private String jobStatus;

    //构建参数
    private Map<String, String> params;

}
