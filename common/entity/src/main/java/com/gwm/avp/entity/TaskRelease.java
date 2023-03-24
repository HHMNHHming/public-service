package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "释放vin数据",description = "")
@Data
public class TaskRelease extends BaseEntity implements Serializable {

    private Long sendOutId;

    private String vin;

    private Integer status;

}