package com.gwm.avp.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "任务释放页面参数",description = "")
@Data
public class TaskReleaseDTO implements Serializable {
    private Long taskId;
    @ApiModelProperty(value = "车系#车型#版本号code#版本号name,只有全部释放时才传")
    private String version;
    @ApiModelProperty(value = "释放状态（ 0 全部， 1 部分）")
    private Integer releaseAll;
    @ApiModelProperty(value = "开关状态（DISABLEALL(0),ENABLEALL(1),DISABLEPART(2),ENABLEPART(3)）")
    private Integer switchStatus;
    @JsonProperty("uId")
    private String uId;
    @ApiModelProperty(value = "是否选定版本强制下发该版本下的所有车辆开关（ 0 关闭， 1 开启）")
    private Integer isForced;
    @ApiModelProperty(value = "部分释放时车辆vin对应的车系#车型#版本号code#版本号name 的json")
    private String releaseJson;
    @ApiModelProperty(value = "部分启用或禁用时车辆vin对应的车系#车型#版本号code#版本号name 的json")
    private String switchJson;

}