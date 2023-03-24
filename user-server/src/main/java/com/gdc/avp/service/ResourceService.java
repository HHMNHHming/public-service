package com.gdc.avp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gwm.avp.entity.Resource;

import java.util.List;

public interface ResourceService extends IService<Resource> {
    /**
     * 根据角色id获取资源信息
     * @param roleId
     * @param type 0：菜单；1：普通资源；不传获取全部
     * @param isParent 0：其他资源；1：顶级菜单；其他或不传获取全部
     * @return
     */
    List<Resource> getResourceByRoleId(Long roleId, Integer type, Integer isParent);

    /**
     * 根据parentid获取资源
     * @param parentId
     * @return
     */
    List<Resource> getResourceByParentId(Long parentId);

    /**
     * 获取资源树
     * @param parentList 顶级菜单列表
     * @param childrenList 其他资源列表
     * @return
     */
    List<Resource> getResourceTree(List<Resource> parentList,List<Resource> childrenList);

    /**
     * 保存角色与资源的对应关系
     * @param roleId
     * @param resourceIds
     */
    void saveRoleResources(Long roleId, Long[] resourceIds);


    /**
     * 根据资源id查询角色名称
     * @param resourceId
     * @return
     */
    List<String> getRoleNameByResourceId(Long resourceId);

    /**
     * 加载资源到redis
     */
    void loadResourceIntoRedis();

    /**
     * 根据角色id获取其所属资源id
     * @param roleId
     * @return
     */
    List<Long> getSelectedResourceIdByRoleId(Long roleId);

    /**
     * 删除角色所属资源
     * @param roleId
     */
    void deleteRoleResource(Long roleId);
}
