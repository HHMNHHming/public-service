package com.gwm.avp.controller;

import com.gwm.avp.component.JwtGenerator;
import com.gwm.avpdatacloud.basicpackages.utils.ResultData;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


//@RestController=@Controller+@ResponseBody
@Controller
@RequestMapping("/access")
public class AccessController {
    @Autowired
    JwtGenerator jwtGenerator;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @RequestMapping("/control")
    @ResponseBody
    public ResultData oauthMethod(@RequestParam String userName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException {
        //拿到授权码，现在生成令牌
        //oauthClient.loginMethod();
        //用户名、头像等个人信息
        Map<String,Object> claims = new HashMap<>();
        claims.put("user_name",userName);
        claims.put("authorities",new String[]{"admin"});
        claims.put("roleIds",new Long[]{1L});
        String str_jwt = jwtGenerator.generator(claims);

        System.out.println("生成令牌成功···");
        redisTemplate.opsForValue().set(userName,str_jwt,30L, TimeUnit.MINUTES);
        return ResultData.SUCCESS("ok",str_jwt);
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        System.out.println("你好，执行成功了哦");
        return "helloWorld";
    }
}