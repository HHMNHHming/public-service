package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "车辆任务信息",description = "")
public class VehicleTask extends BaseEntity{

    @NotBlank(message = "vin不能为空")
    @ApiModelProperty(value = "vin",required = true)
    private String vin;

    @NotBlank
    @ApiModelProperty(value = "任务内容",required = true)
    private String content;

    @NotNull
    @ApiModelProperty(value = "任务Id",required = true)
    private Long taskId;

    @NotBlank
    @ApiModelProperty(value = "任务名称",required = true)
    private String taskName;

    @NotNull(message = "软件版本不能为Null")
    @ApiModelProperty(value = "软件版本编号")
    private String isoVersionId;
}
