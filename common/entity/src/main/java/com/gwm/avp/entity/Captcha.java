package com.gwm.avp.entity;

import lombok.Data;

@Data
public class Captcha {
    private String captchaImageBase64;
    private String captchaId;
}
