package com.gdc.avp.client;

import com.gwm.avpdatacloud.basicpackages.utils.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("config-server")
@Component
public interface ConfigService {
    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/template/findByPage")
    ResultData findByPage(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize,@RequestParam(value="name",required=false) String name);

    /**
     * 获取所有模板
     * @return
     */
    @GetMapping("/template/findAll")
    ResultData findAll(@RequestParam(value="name",required=false) String name);

}
