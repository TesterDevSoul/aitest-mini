package com.ceshiren.aitestmini.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@ApiModel(value="添加测试任务实体类")
@Getter
@Setter
@ToString
public class TestTaskAddDTO {

    @ApiModelProperty(value="测试任务名称",required=true)
    private String taskName;

    @ApiModelProperty(value="测试任务备注", required=true)
    private String remark;

    @ApiModelProperty(value="测试用例的ID列表",example = "[1,5,10]", required=true)
    private List<Integer> testCaseIdList;

}
