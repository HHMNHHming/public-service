package com.gwm.avp.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class SendKafkaTemp implements Serializable {

    private String id;

    private String batchNo;

    private String vin;

    private String sendOutId;

    private String taskId;

    private String carSeries;
    //车型code码
    private String carType;

    private String isoVersionId;

    private String version;

    private JSONObject taskContent;

    private int status;

}
