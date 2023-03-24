package com.gdc.avp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdc.avp.client.ConfigService;
import com.gwm.avp.entity.RoleTemplate;
import com.gwm.avp.entity.User;
import com.gdc.avp.service.RoleTemplateService;
import com.gdc.avp.service.UserService;
import com.gwm.avpdatacloud.basicpackages.log.annotation.GDC;
import com.gwm.avpdatacloud.basicpackages.utils.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;

@GDC
@Api(tags = "角色-模板API")
@Controller
@RequestMapping("/roleTemplate")
public class RoleTemplateController {

    @Autowired
    private ConfigService configService;
    @Autowired
    private RoleTemplateService roleTemplateService;
    @Autowired
    private UserService userService;


    @ApiOperation(value = "获取所有模板",httpMethod = "GET")
    @GetMapping("/findAll")
    @ResponseBody
    public ResultData findAll(String name) {
        ResultData result = configService.findAll(name);
        return ResultData.SUCCESS("", result);
    }

    @ApiOperation(value = "保存角色-模板对应关系（根据角色id）",httpMethod = "POST")
    @PostMapping("/saveRoleTemplate")
    @ResponseBody
    public ResultData saveResource(@RequestParam Long roleId,@RequestParam String templateCodes,@RequestAttribute String userName){
        if(StringUtils.isEmpty(templateCodes)){
            roleTemplateService.deleteRoleTemplate(roleId);
            return ResultData.SUCCESS("保存成功！");
        }
        String[] ids = templateCodes.split(",");
        if(ids==null||ids.length==0)
            return ResultData.FAILL("非法参数！");
        roleTemplateService.deleteRoleTemplate(roleId);
        roleTemplateService.saveRoleTemplates(userName,roleId,
                Arrays.stream(ids).map(Long::parseLong).toArray(Long[]::new));
        return ResultData.SUCCESS("保存成功！");
    }

    @ApiOperation(value = "自动保存用户-模板对应关系（根据当前用户名）",httpMethod = "POST")
    @PostMapping("/saveRoleTemplateByUserName")
    @ResponseBody
    public ResultData saveRoleTemplateByUserName(@RequestAttribute String userName,@RequestParam String templateCodes){
        String[] ids = templateCodes.split(",");
        if(ids==null||ids.length==0)
            return ResultData.FAILL("非法参数！");
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName,userName);
        User user =  userService.getOne(lambdaQueryWrapper);
        if(user==null){
            return ResultData.FAILL("当前用户不存在！");
        }
        Long roleId = user.getRoleId();
        roleTemplateService.saveRoleTemplates(userName,roleId,
                Arrays.stream(ids).map(Long::parseLong).toArray(Long[]::new));
        return ResultData.SUCCESS("保存成功！");
    }


    @ApiOperation(value = "获取当前角色所拥有的模板列表",httpMethod = "GET")
    @GetMapping("/findTemplateCodeListByRoleId")
    @ResponseBody
    public ResultData findTemplateCodeListByRoleId(@RequestParam Long roleId){
        return ResultData.SUCCESS("",
                roleTemplateService.getSelectedTemplateCodeByRoleId(roleId));
    }

    @ApiOperation(value = "根据当前用户名获取拥有的模板列表",httpMethod = "GET")
    @GetMapping("/findTemplateCodeListByUserName")
    @ResponseBody
    public ResultData findTemplateCodeListByUserName(@RequestAttribute String userName){
        if(StringUtils.isEmpty(userName)){
            return ResultData.FAILL("用户名参数为空！");
        }
        return ResultData.SUCCESS("",
                roleTemplateService.findTemplateCodeListByUserName(userName));
    }

    @ApiOperation(value = "分页查询当前用户的所有模板",httpMethod = "GET")
    @GetMapping("/findByPage")
    @ResponseBody
    public ResultData page(@RequestParam Integer page, @RequestParam Integer pageSize,
                           String userName) {
        Page<RoleTemplate> roleTemplatePage = new Page<>(page, pageSize);;
        Page<RoleTemplate> list = roleTemplateService.findByPage(roleTemplatePage, userName);
        return ResultData.SUCCESS("", list);
    }
}
