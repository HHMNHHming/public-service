package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "任务和策略对应数据",description = "")
@Data
public class TaskItem extends BaseEntity implements Serializable {
    private Long taskId;

    private Long strategyId;

    private String attr1;

    private String attr2;

    private String attr3;

    private String attr4;

    private String attr5;

    private String attr6;

    private String attr7;

    private String attr8;

    private String attr9;

    private String attr10;

    private String attr11;

    private String attr12;

}