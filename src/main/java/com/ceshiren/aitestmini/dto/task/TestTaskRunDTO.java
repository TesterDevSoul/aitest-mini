package com.ceshiren.aitestmini.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ApiModel(value="测试任务启动实体类")
@Getter
@Setter
@ToString
public class TestTaskRunDTO {
    @ApiModelProperty(value="测试任务id",required=true)
    private Integer id;
    @ApiModelProperty(value="任务执行命令",required=true)
    private String command;
    @ApiModelProperty(value="测试任务备注")
    private String remark;

}
