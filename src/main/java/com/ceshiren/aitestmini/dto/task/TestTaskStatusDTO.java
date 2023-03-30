package com.ceshiren.aitestmini.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ApiModel(value="测试任务状态更改类")
@Getter
@Setter
@ToString
public class TestTaskStatusDTO {

    @ApiModelProperty(value="测试任务id",required=true)
    private Integer id;
    @ApiModelProperty(value="任务构建地址")
    private String buildUrl;
    @ApiModelProperty(value="任务状态")
    private Integer status;
}
