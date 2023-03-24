package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "MDC周期缓存回传数据",description = "")
@Data
public class MDCPeriodicCacheData implements Serializable {
    private String vin;
    private String requestId;
    private String content;
    private String path;
    private String platform;
    private Boolean isLogin;
}
