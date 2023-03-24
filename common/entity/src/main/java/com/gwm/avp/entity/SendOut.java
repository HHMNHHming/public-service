package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "下发数据",description = "")
@Data
public class SendOut extends BaseEntity implements Serializable {

    private Long taskId;

    private String taskName;

    private Integer status;

    private Boolean period;

    private Date startDate;

    private Date endDate;

    private Integer taskStatus;

    private Integer preTaskStatus;

    private String isoVersionId;

    private Boolean releaseStatus;

    private Integer switchStatus;

    private Integer error;

    private Integer succ;

    private Integer total;

    private Date startTime;

    private Date endTime;

    private Integer retry;

    private Integer closeTestStatus;

    private Integer grayscaleTestStatus;

    private String version;

    private Integer kafkaStatus;

    private Long templateId;

    private String operator;

    private String errMessage;

    private Integer isForced;

}