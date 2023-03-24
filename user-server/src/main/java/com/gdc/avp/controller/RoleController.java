package com.gdc.avp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gwm.avp.entity.Role;
import com.gwm.avp.entity.User;
import com.gdc.avp.service.RoleService;
import com.gdc.avp.service.UserService;
import com.gwm.avpdatacloud.basicpackages.utils.ResultData;
import com.gwm.avpdatacloud.basicpackages.log.annotation.GDC;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@GDC
@Api(tags = "角色管理API")
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

//    @Autowired
//    private SessionManage roleSessionManage;

    @ApiOperation(value = "分页查询",httpMethod = "GET")
    @GetMapping("/page")
    @ResponseBody
    public ResultData page(@RequestParam Integer page,@RequestParam Integer pageSize,String roleName){
        Page<Role> rolePage= new Page<>(page,pageSize);
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(roleName),Role::getName,roleName);
        return ResultData.SUCCESS("", roleService.page(rolePage,wrapper));
    }

    @ApiOperation(value = "查询所有角色",httpMethod = "GET")
    @GetMapping("/findAll")
    @ResponseBody
    public ResultData findAll(){
        List<Role> roles = roleService.list();
        roles = roles.stream().filter(role -> !"超级管理员".equals(role.getName()))
                .collect(Collectors.toList());
        return ResultData.SUCCESS("",roles);
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @ApiOperation(value = "新增角色",httpMethod = "POST")
    @PostMapping("/insert")
    @ResponseBody
    public ResultData insert(@RequestBody @Validated Role role){
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getName,role.getName());
        Role rtnRole = roleService.getOne(wrapper);
        if (rtnRole!=null){
            return ResultData.FAILL("名称已存在，请重新添加！");
        }
        roleService.save(role);
        return ResultData.SUCCESS("新增成功！");
    }

    @ApiOperation(value = "更新角色",httpMethod = "POST")
    @PostMapping("/update")
    @ResponseBody
    public ResultData update(@RequestBody @Validated Role role){
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getName,role.getName());
        Role selectRole = roleService.getOne(wrapper);
        if (selectRole!=null&&!role.getId().equals(selectRole.getId())) {
            return ResultData.FAILL("名称已存在，请重新修改！");
        }
        roleService.updateById(role);
        return ResultData.SUCCESS("修改成功！");
    }

    @ApiOperation(value = "删除角色",httpMethod = "POST")
    @PostMapping("/delete")
    @ResponseBody
    public ResultData delete(@RequestParam String id){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getRoleId,id);
        User selectUser = userService.getOne(wrapper);
        if(selectUser != null ){
            return ResultData.FAILL("操作失败！请检查是否有关联用户。");
        }
        roleService.removeById(id);
        return ResultData.SUCCESS("删除成功！");
    }

    @ApiOperation(value = "修改角色状态",httpMethod = "POST")
    @PostMapping("/status")
    @ResponseBody
    public ResultData updateStatus(@RequestParam Long id,@RequestParam @Max(value = 1) @Min(value = 0) Integer status){
        Role role = new Role();
        role.setId(id);
        role.setStatus(status);
        roleService.updateById(role);
        return ResultData.SUCCESS("修改成功！");
    }

}
