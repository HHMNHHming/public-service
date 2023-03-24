package com.gdc.avp.interceptor;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gwm.avpdatacloud.basicpackages.utils.ResultData;
import com.nimbusds.jose.JWSObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedisSessionInterceptor implements HandlerInterceptor {

    @Value("#{${expire}}")
    private Long expire;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        String uri = request.getHeader("uri");
        //验证当前用户是否已经登录
        if (StringUtils.isNotEmpty(authorization)) {
            //权限验证
            String realToken = authorization.replace("Bearer ", "");
            JWSObject jwsObject = JWSObject.parse(realToken);
            String userStr = jwsObject.getPayload().toString();
            JSONObject userJsonObject = JSONObject.parseObject(userStr);
            String userName = (String) userJsonObject.get("user_name");
            String sessionStr = redisTemplate.opsForValue().get(userName);
            if(StringUtils.isEmpty(sessionStr)||!realToken.equals(sessionStr)){
                response401(response,null);
                return false;
            }
            redisTemplate.expire(userName, expire, TimeUnit.MINUTES);
            JSONArray roles = userJsonObject.getJSONArray("authorities");
            String roleName = roles.getString(0);
            Object obj = redisTemplate.opsForHash().get("AUTH:RESOURCE_ROLES_MAP", uri);
            if(obj!=null){
                JSONObject authorities = JSONObject.parseObject(obj.toString());
                if ((authorities != null && !authorities.isEmpty())
                        && authorities.getJSONArray("roles").contains(roleName)) {
                    return true;
                }
                response401(response,"没有权限访问：【"+authorities.getString("name")+"】"+uri);
            }
            response401(response,"未知权限："+uri);
        }
        response401(response,null);
        return false;
    }
    public void response401(HttpServletResponse response,String message){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        try (PrintWriter writer = response.getWriter();){
            if(message == null){
                writer.print(JSON.toJSON(ResultData.Unauthorized()));
            }else{
                writer.print(JSON.toJSON(ResultData.FAILL(message)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}