package com.gwm.avp.client;

import com.gwm.avpdatacloud.basicpackages.utils.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient("user-server")
public interface OauthClient {
    @RequestMapping("/user/login")
    ResultData loginMethod();
}
