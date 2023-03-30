package com.ceshiren.aitestmini.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.util.List;


@ApiModel(value="测试任务实体类")
@Getter
@Setter
@ToString
public class TestTaskDTO {
    @ApiModelProperty(value="测试任务id",required=true)
    private Integer id;
    @ApiModelProperty(value="测试任务名称")
    private String taskName;
    @ApiModelProperty(value="任务构建地址")
    private String buildUrl;
    @ApiModelProperty(value="任务状态")
    private Integer status;
    @ApiModelProperty(value="任务执行命令")
    private String command;
    @ApiModelProperty(value="测试任务备注")
    private String remark;
    @ApiModelProperty(value="测试用例的ID列表",example = "[1,5,10]")
    private List<Integer> testCaseIdList;
    @ApiModelProperty(value="job构建ID",example = "46")
    private Integer jobId;

}
