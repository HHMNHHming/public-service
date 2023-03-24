package com.gdc.avp.controller;

import com.gwm.avpdatacloud.basicpackages.utils.ResultData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限接口
 */
@RequestMapping("/auth")
@RestController
public class IndexController {


    /**
     * 检测用户是否有权限访问资源
     * @return
     */
    @RequestMapping("/check")
    public ResultData check(){
        return ResultData.SUCCESS("ok");
    }
}
