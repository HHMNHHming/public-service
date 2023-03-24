package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "测试vin数据",description = "")
@Data
public class TestVin extends BaseEntity implements Serializable {
    private String vin;
    private Integer type;
    private Long sendOutId;
    private Long testDataId;
    private String carSeriesId;
    private String carTypeId;
    private String isoVersionId;
    private String version;
}