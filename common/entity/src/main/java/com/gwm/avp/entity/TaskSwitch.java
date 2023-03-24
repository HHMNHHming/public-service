package com.gwm.avp.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class TaskSwitch extends BaseEntity implements Serializable {

    private Long sendOutId;

    private String vin;

    private Integer type;

}