package com.gwm.avp.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

/**
 * 当前请求用户信息
 *
 */

/**
 * 不建议再使用这种方式获取当前用户名称。现在已经在components组件中增加了webfilter，通过在filter对request中Authorization进行解析获取userName并放入request中，
 * 所以在controller的接口中可以通过以下方式获取：
 * @ApiOperation(value = "获取当前角色所属资源",httpMethod = "GET")
 * @GetMapping("/findMyResource")
 * @ResponseBody
 * public ResultData findMyResource(@RequestAttribute String userName,@RequestAttribute String[] roleNames){
 *     ......
 * }
 * 在接口中增加参数userName,roleNames获取当前用户名称以及角色信息
 *
 *@author 辛野
 *@version 当前版本1.2.0，计划在后续版本中移除此类。
 */
@Deprecated
public class CurrentUserUtil {

    //TODO 这里需要修改
    public static String getUserName(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(authorization)){
            return null;
        }
        String jwt = authorization.replace("Bearer ","");
        String[] args = jwt.replace("\r\n", "").split("\\.");
        String userStr  = null;
        try {
            JSONObject payload = JSONObject.parseObject(new String(Base64.getUrlDecoder().decode(args[1].getBytes())));
            userStr = payload.getString("user_name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userStr;
    }

}
