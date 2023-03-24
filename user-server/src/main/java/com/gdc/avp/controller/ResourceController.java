package com.gdc.avp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gwm.avp.entity.Resource;
import com.gwm.avp.entity.Role;
import com.gwm.avp.entity.User;
//import com.gdc.avp.log.GDCLog;
import com.gdc.avp.service.ResourceService;
import com.gdc.avp.service.RoleService;
import com.gdc.avp.service.UserService;
import com.gwm.avp.util.Common;
import com.gwm.avpdatacloud.basicpackages.log.annotation.GDC;
import com.gwm.avpdatacloud.basicpackages.utils.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GDC
@Api(tags = "资源管理API")
@Controller
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "分页查询",httpMethod = "GET")
    @GetMapping("/page")
    @ResponseBody
    public ResultData page(@RequestParam Integer page, @RequestParam Integer pageSize,
                           String name, String title, Integer type) {
        Page<Resource> resourcePage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Resource::getName, name);
        queryWrapper.like(StringUtils.isNotEmpty(title), Resource::getTitle, title);
        queryWrapper.eq(type != null, Resource::getType, type);
        List<Resource> list = resourceService.page(resourcePage, queryWrapper).getRecords();
        list.forEach(resource -> resource.setParent(
                resource.getParentId() != null ? resourceService.getById(resource.getParentId()) : null));
        return ResultData.SUCCESS("", resourceService.page(resourcePage, queryWrapper).setRecords(list));
    }

    @ApiOperation(value = "查询所有菜单",httpMethod = "GET")
    @GetMapping("/listMenu")
    @ResponseBody
    public ResultData listMenu(String name){
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Resource::getType, Common.Resource.Type.MENU.toValue());
        queryWrapper.and(wrapper->wrapper.like(Resource::getName,name).or().like(Resource::getTitle,name));
        return ResultData.SUCCESS("",resourceService.list(queryWrapper));
    }

    @ApiOperation(value = "获取当前角色所属菜单",httpMethod = "GET")
    @GetMapping("/findMyMenu")
    @ResponseBody
    public ResultData findMyMenu(@RequestAttribute String userName){
        Role role = null;
        try {
            role = getRole(userName);
        } catch (Exception e) {
            return ResultData.FAILL("获取当前角色失败！");
        }
        List<Resource> parentList = resourceService.getResourceByRoleId(role.getId(), Common.Resource.Type.MENU.toValue(),
        Common.Resource.isParent.PARENT.toValue());
        List<Resource> childrenList = resourceService.getResourceByRoleId(role.getId(), Common.Resource.Type.MENU.toValue(),
                Common.Resource.isParent.CHILD.toValue());
        return ResultData.SUCCESS("",resourceService.getResourceTree(parentList,childrenList));
    }

    private Role getRole(String userName) throws Exception{
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        User user = userService.getOne(queryWrapper);
        if(user == null){
            throw new Exception("user is null！");
        }
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getId,user.getRoleId());
        Role role = roleService.getOne(roleLambdaQueryWrapper);
        return role;
    }

    @ApiOperation(value = "获取所有资源",httpMethod = "GET")
    @GetMapping("/findAllResource")
    @ResponseBody
    public ResultData findAllResource(){
        LambdaQueryWrapper<Resource> parentWrapper = new LambdaQueryWrapper<>();
        parentWrapper.isNull(Resource::getParentId);
        parentWrapper.orderByAsc(Resource::getSort);
        List<Resource> parentList = resourceService.list(parentWrapper);
        LambdaQueryWrapper<Resource> childrenWrapper = new LambdaQueryWrapper<>();
        childrenWrapper.isNotNull(Resource::getParentId);
        childrenWrapper.orderByAsc(Resource::getSort);
        List<Resource> childrenList = resourceService.list(childrenWrapper);
        return ResultData.SUCCESS("",resourceService.getResourceTree(parentList,childrenList));
    }

    @ApiOperation(value = "获取当前角色所属资源",httpMethod = "GET")
    @GetMapping("/findMyResource")
    @ResponseBody
    public ResultData findMyResource(@RequestAttribute String userName,@RequestAttribute String[] roleNames){
        Role role;
        try {
            System.out.println("======="+userName);
            role = getRole(userName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.FAILL("获取当前角色失败！");
        }
        List<Resource> childrenList = resourceService.getResourceByRoleId(role.getId(),
                Common.Resource.Type.RESOURCE.toValue(),
                Common.Resource.isParent.CHILD.toValue());
        Map<String,Object> map = new HashMap<>();
        map.put("userName",userName);
        map.put("list",childrenList);
        return ResultData.SUCCESS("",map);
    }


    @ApiOperation(value = "保存角色资源权限",httpMethod = "POST")
    @PostMapping("/saveResource")
    @ResponseBody
    public ResultData saveResource(@RequestParam Long roleId,@RequestParam String resourceIds){
        if(StringUtils.isEmpty(resourceIds)){
            resourceService.deleteRoleResource(roleId);
            resourceService.loadResourceIntoRedis();
            return ResultData.SUCCESS("保存成功！");
        }
        String[] ids = resourceIds.split(",");
        if(ids==null||ids.length==0)
            return ResultData.FAILL("非法参数！");
        Long start = System.currentTimeMillis();
        resourceService.saveRoleResources(roleId,
                Arrays.stream(ids).map(Long::parseLong).toArray(Long[]::new));
        resourceService.loadResourceIntoRedis();
        return ResultData.SUCCESS("保存成功！");
    }

    @ApiOperation(value = "删除资源",httpMethod = "POST")
    @PostMapping("/delete")
    @ResponseBody
    public ResultData delete(@RequestParam String id){
        resourceService.removeById(id);
        return ResultData.SUCCESS("删除成功！");
    }

    @ApiOperation(value = "更新资源",httpMethod = "POST")
    @PostMapping("/update")
    @ResponseBody
    public ResultData update(@RequestBody @Validated Resource resource){
        resourceService.updateById(resource);
        return ResultData.SUCCESS("修改成功！");
    }

    @ApiOperation(value = "保存资源",httpMethod = "POST")
    @PostMapping("/insert")
    @ResponseBody
    public ResultData insert(@RequestBody @Validated Resource resource){
        if (resource.getParent()!=null&&resource.getId().equals(resource.getParent()))
            return ResultData.FAILL("父资源ID不能与资源ID相同！");
        resourceService.save(resource);
        return ResultData.SUCCESS("新增成功！");
    }

    @ApiOperation(value = "获取选中资源列表",httpMethod = "GET")
    @GetMapping("/findMyResourceList")
    @ResponseBody
    public ResultData findMyResourceList(@RequestParam Long roleId){
        return ResultData.SUCCESS("",
                resourceService.getSelectedResourceIdByRoleId(roleId));
    }
}
