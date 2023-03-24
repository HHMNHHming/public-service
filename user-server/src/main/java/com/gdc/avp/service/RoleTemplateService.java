package com.gdc.avp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gwm.avp.entity.RoleTemplate;

import java.util.List;

public interface RoleTemplateService extends IService<RoleTemplate> {
    /**
     * 删除角色所对用的模板
     * @param roleId
     */
    void deleteRoleTemplate(Long roleId);

    /**
     * 保存角色与模板的对应关系
     * @param roleId
     * @param templateCodes
     */
    void saveRoleTemplates(String userName,Long roleId, Long[] templateCodes);

    /**
     * 根据角色id获取其所属模板code
     * @param roleId
     * @return
     */
    List<String> getSelectedTemplateCodeByRoleId(Long roleId);

    List<String> findTemplateCodeListByUserName(String userName);

    Page<RoleTemplate> findByPage(Page<RoleTemplate> page,String userName);
}
