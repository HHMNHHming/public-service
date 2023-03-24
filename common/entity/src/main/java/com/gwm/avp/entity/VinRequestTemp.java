package com.gwm.avp.entity;

import lombok.Data;

@Data
public class VinRequestTemp extends BaseEntity{

    private String vin;
    private Long sendOutId;
    private Integer type;
}
