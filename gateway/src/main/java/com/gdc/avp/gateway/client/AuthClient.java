package com.gdc.avp.gateway.client;

import com.gwm.avpdatacloud.basicpackages.utils.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("authentication-server")
@Component
public interface AuthClient {
    @RequestMapping(value="/auth/check")
    ResultData check(@RequestHeader("Authorization") String authorization,
                     @RequestHeader("Other-Auth") String via,
                     @RequestHeader("uri") String uri);}
