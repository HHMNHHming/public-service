package com.gwm.avp.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class SendReleaseSwitch extends BaseEntity implements Serializable {

    private Long sendOutId;

    private String vin;

    private Integer type;

    private String carSeriesId;
    //车系业务编码
    private String carTypeId;

    private String isoVersionId;
    //版本号名称
    private String version;

}