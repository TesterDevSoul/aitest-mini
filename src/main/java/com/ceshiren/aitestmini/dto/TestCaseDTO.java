package com.ceshiren.aitestmini.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TestCaseDTO {
    @ApiModelProperty(value="测试用例id")
    private Integer id;
    @ApiModelProperty(value="测试用例名称",required=true)
    private String caseName;
    @ApiModelProperty(value="测试用例数据",
            notes = "文件类型case时不传值", required=true)
    private String caseData;
    @ApiModelProperty(value="测试用例备注")
    private String remark;
}
