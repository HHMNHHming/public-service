package com.gwm.avp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "下发详情",description = "")
@Data
public class SendDetails extends BaseEntity implements Serializable {

    private String vin;

    private Long sendOutId;

    private Integer status;

    private String carSeriesId;

    private String carTypeId;

    private String msg;

    private String dataContent;

    private String version;

    private String isoVersionId;

    private Integer retry;

    private String configStatus;

    //是否启用
    private String isEnable;
    //配置文件上传oss之后，更新sendDetail表状态的时间，因为有毫末等解析状态的更新，所以要跟updateTIme区分开
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date ossTime;

}