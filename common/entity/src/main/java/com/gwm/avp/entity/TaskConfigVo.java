package com.gwm.avp.entity;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel(value = "新的下发配置到kafka的实体类",description = "")
@Data
public class TaskConfigVo {

    private Long sendOutId;

    private Long taskId;

    private String taskName;

    //releaseAll  （ALL(0),PART(1)）
    private Integer releaseAll;

    //switchStatus （DISABLEALL(0),ENABLEALL(1),DISABLEPART(2),ENABLEPART(3)）
    private Integer switchStatus;

    //是否是多任务 0:不是 1：是，通过templateId去调用返回。
    private String multiple;

    private String vin;

    //status 启用/禁用 (1/0)，这是是判断当前vin号到底是启用还是禁用的状态。
    private Integer status;

    //是否是相同的两个任务，只有status=0，也就是禁用的时候用这个字段去判断是下发当前任务带禁用的字段还是下发另一个任务的配置
    private String isSameTask;

    private Long templateId;

    //车系code码
    private String carSeries;
    //车型code码
    private String carType;

    private String isoVersionId;

    //版本号名称
    private String version;

    private Integer total;

    private String operator;

    private JSONArray content;

    private JSONObject detailContent;

    private List<String> vinList;
    //批次号
    private String batchNo;
    //是否强制下发所选版本 0:不是 1：是
    private Integer isForced;

    private Integer taskStatus;
}
