package com.gwm.avp.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class EdcVehicle implements Serializable {

    private String id;

    private String vin;
//    哈弗神兽
    private String seriesCode;

    private String tbjEigenvalue;

    private String tfuEigenvalue;
//    哈弗
    private String brandName;
//    B02
    private String productCode;
//    域控平台版本1.0 or 1.5
    private String dcVersion;
//    保存时间
    private Date createTime;

}
