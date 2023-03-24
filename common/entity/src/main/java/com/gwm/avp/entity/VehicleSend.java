package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "车辆下发记录",description = "")
public class VehicleSend extends BaseEntity{

    @NotNull(message = "taskId不能为NULL")
    @ApiModelProperty(value = "任务Id",required = true)
    private Long taskId;

    @NotBlank(message = "taskName不能为空")
    @ApiModelProperty(value = "任务名称",required = true)
    private String taskName;

    @NotNull(message = "sendOutId不能为NULL")
    @ApiModelProperty(value = "下发任务Id",required = true)
    private Long sendOutId;

    @NotNull(message = "释放状态不能为NULL")
    @ApiModelProperty(value = "释放状态",required = true)
    private Integer releaseAll;

    @NotNull(message = "开关状态不能为NULL")
    @ApiModelProperty(value = "开关状态",required = true)
    private Integer switchStatus;

    @ApiModelProperty(value = "状态(0 初始化 1 完成获取vin列表 2 vin状态重置 3 完成 -1 失败)")
    private Integer status;

    @ApiModelProperty(value = "异常信息记录")
    private String errMessage;

    @ApiModelProperty(value = "任务内容")
    private String content;

    @ApiModelProperty(value = "软件版本业务编码集合，逗号分隔")
    private String versionCode;

    private Integer templateCode;

}
