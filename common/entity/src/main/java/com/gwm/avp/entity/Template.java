package com.gwm.avp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "模板数据",description = "任务及配置新增、修改、详情模板")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Template extends BaseEntity {

    @NotBlank(message = "名称不能为空")
    @Length(min = 2,max = 20,message = "名称长度需要在2~20个字符间")
    @ApiModelProperty(name = "名称")
    private String name;

    @Length(max = 50,message = "描述最多支持50字符")
    @ApiModelProperty(name = "描述")
    private String details;

    @NotNull(message = "模板编码不能为Null")
    @Max(value = 9999,message = "模板编码应当小于9999")
    @ApiModelProperty(name = "模板编码")
    private Integer code;

    @NotNull(message = "需要设置是否支持多任务")
    @ApiModelProperty(name = "是否支持多任务")
    private Boolean multiple;
}
