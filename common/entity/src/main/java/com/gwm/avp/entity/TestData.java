package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "测试数据",description = "")
@Data
public class TestData extends BaseEntity implements Serializable {

    private Long taskId;

    private Integer status;

    private Integer type;

    private Boolean rd;

    private Boolean qa;

    private Boolean pm;

    private String rdOperator;

    private String qaOperator;

    private String pmOperator;

    private String carSeries;

    private String carType;

    private List<TestVin> vins;

}