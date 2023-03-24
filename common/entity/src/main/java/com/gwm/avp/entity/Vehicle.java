package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "车辆信息")
@Data
public class Vehicle extends BaseEntity {

    @NotBlank(message = "vin不能为空")
    @Length(min = 17,max = 17,message = "vin长度17字符")
    @ApiModelProperty(value = "车架号")
    private String vin;

    @NotNull(message = "车系编号不能为Null")
    @ApiModelProperty(value = "车系编号")
    private String carSeriesId;

    @NotNull(message = "车型编号不能为Null")
    @ApiModelProperty(value = "车型编号")
    private String carTypeId;

    @ApiModelProperty(value = "软件版本号，多个用逗号分割")
    private String isoVersionId;
}
