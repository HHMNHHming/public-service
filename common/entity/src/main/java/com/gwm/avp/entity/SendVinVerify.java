package com.gwm.avp.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendVinVerify extends BaseEntity implements Serializable {

    private Long taskId;

    private String vin;

    private Integer status;

    private Integer type;

    private String uId;

    private String carSeriesId;
    //车系业务编码
    private String carTypeId;

    private String isoVersionId;

    private Long templateId;
    //版本号名称
    private String version;
}