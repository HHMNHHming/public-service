package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "毫末配置结果同信息",description = "")
@Data
public class SyncHaoMoSend extends BaseEntity implements Serializable {

    private String token;

    private String lastVin;

    private String lastVersion;

    private Long lastTime;

    private String returnContent;
}