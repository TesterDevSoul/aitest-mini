package com.ceshiren.aitestmini.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value="请求实体类")
@Getter
@Setter
@ToString
public class RequestInfoDTO {
    //请求的接口地址
    @ApiModelProperty(value="请求的接口地址，用于拼装命令",hidden=true)
    private String requestUrl;
    //请求的服务器地址
    @ApiModelProperty(value="请求的服务器地址，用于拼装命令",hidden=true)
    private String baseUrl;
}
