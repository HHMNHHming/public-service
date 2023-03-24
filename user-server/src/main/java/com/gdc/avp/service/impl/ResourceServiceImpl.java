package com.gdc.avp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gwm.avp.entity.Resource;
import com.gwm.avpdatacloud.basicpackages.log.GDCLog;
import com.gdc.avp.mapper.ResourceMapper;
import com.gdc.avp.service.ResourceService;
import com.gwm.avp.util.Common;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public List<Resource> getResourceByRoleId(Long roleId, Integer type, Integer isParent) {
        return baseMapper.getResourceByRoleId(roleId, type, isParent);
    }

    public List<Resource> getResourceByParentId(Long parentId) {
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Resource::getParentId, parentId);
        return this.list(queryWrapper);
    }

    public List<Resource> getResourceTree(List<Resource> parentList,List<Resource> childrenList){
        parentList.forEach(item ->
                buildResourceTree(item, childrenList)
        );
        return parentList;
    }

    private Resource buildResourceTree(Resource resource,List<Resource> list){
        List<Resource> childrenList = new ArrayList<>();
        list.forEach(item->{
            if (item.getParentId().equals(resource.getId())){
                childrenList.add(buildResourceTree(item,list));
            }
        });
        resource.setChildrenList(childrenList);
        return resource;
    }

    @Transactional
    public void saveRoleResources(Long roleId, Long[] resourceIds) {
        deleteRoleResource(roleId);
//        Arrays.stream(resourceIds).
//                forEach(resourceId -> baseMapper.saveRoleResource(roleId, resourceId));
        baseMapper.saveRoleResourceBatch(roleId, resourceIds);
    }

    @Override
    public List<String> getRoleNameByResourceId(Long resourceId) {
        return baseMapper.getRoleNameByResourceId(resourceId);
    }

    public void deleteRoleResource(Long roleId) {
        baseMapper.deleteRoleResource(roleId);
    }



    @PostConstruct
    public void initData() {
        Integer flag = (Integer) redisTemplate.opsForValue().get(Common.Resource.RESOURCE_INIT_FLAG);
        if (flag==null||flag == Common.Resource.initFlag.IDLE.toValue()) {
            redisTemplate.opsForValue().set(Common.Resource.RESOURCE_INIT_FLAG,
                    Common.Resource.initFlag.INITIALIZING.toValue());
            try {
                loadResourceIntoRedis();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                redisTemplate.opsForValue().set(Common.Resource.RESOURCE_INIT_FLAG,
                        Common.Resource.initFlag.IDLE.toValue());
            }
        } else {
            return;
        }
    }

    @Override
    public void loadResourceIntoRedis(){
        Map<String, Map<String,Object>> resourceRolesMap = new TreeMap<>();
        List<Map<String,Object>> resourceList = baseMapper.findAllWithRole();
        resourceList.stream().forEach(resource -> {
            Map<String,Object> values = new HashMap<>();
            values.put("name",resource.get("name"));
            values.put("roles",resource.getOrDefault("roleNames","").toString().split(","));
            resourceRolesMap.put(resource.get("path").toString(), values);
        });
        redisTemplate.delete(Common.Resource.RESOURCE_ROLES_MAP);
        redisTemplate.opsForHash().putAll(Common.Resource.RESOURCE_ROLES_MAP, resourceRolesMap);
    }

    @Override
    public List<Long> getSelectedResourceIdByRoleId(Long roleId) {
        return baseMapper.getSelectedResourceIdByRoleId(roleId);
    }

}
