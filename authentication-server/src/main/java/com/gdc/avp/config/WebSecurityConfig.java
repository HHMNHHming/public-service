package com.gdc.avp.config;


import com.gdc.avp.interceptor.RedisSessionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebSecurityConfig extends WebMvcConfigurationSupport {

    @Bean
    public RedisSessionInterceptor getRedisSessionInterceptor(){
        return new RedisSessionInterceptor();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getRedisSessionInterceptor());
        super.addInterceptors(registry);
    }
}
