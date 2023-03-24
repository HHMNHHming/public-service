package com.gdc.avp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

//设置session失效时间为60分钟
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60*60)
public class HttpSessionConfig {

    @Bean
    public HttpSessionIdResolver headerHttpSessionIdResolver(){
        return new HeaderHttpSessionIdResolver("x-auth-token");
    }
}
