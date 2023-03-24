package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "车辆信息扩展数据",description = "")
public class VehicleExp extends BaseEntity{

    @NotBlank(message = "vin不能为空")
    @ApiModelProperty(value = "vin",required = true)
    private String vin;
    @ApiModelProperty(value = "数据状态 0 未执行 1 执行中  2 完成  -1 失败")
    private Integer status;

    private Long sendOutId;
    @NotNull(message = "软件版本不能为Null")
    @ApiModelProperty(value = "软件版本编号")
    private String isoVersionId;

    @ApiModelProperty(value = "模板数据")
    private Template template;

    @ApiModelProperty(value = "任务数量")
    private Integer total;
    private List<VehicleTask> taskList;
}
