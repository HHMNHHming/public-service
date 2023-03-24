package com.gwm.avp.entity;

import lombok.Data;

@Data
public class TokenRequest extends BaseEntity{
    private String vin;
    private int status;
    private long tokenRequestFrequency;
    private int enabled;
}
