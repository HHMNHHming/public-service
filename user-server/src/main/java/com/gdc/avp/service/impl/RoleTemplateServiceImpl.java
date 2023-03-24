package com.gdc.avp.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gwm.avp.entity.RoleTemplate;
import com.gdc.avp.mapper.RoleTemplateMapper;
import com.gdc.avp.service.RoleTemplateService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RoleTemplateServiceImpl extends ServiceImpl<RoleTemplateMapper, RoleTemplate> implements RoleTemplateService {
    /**
     * 删除角色所对用的模板
     *
     * @param roleId
     */
    @Override
    public void deleteRoleTemplate(Long roleId) {
        baseMapper.deleteRoleTemplate(roleId);
    }

    /**
     * 保存角色与模板的对应关系
     *
     * @param roleId
     * @param templateCodes
     */
    @Override
    public void saveRoleTemplates(String userName,Long roleId, Long[] templateCodes) {
        Arrays.stream(templateCodes).
                forEach(templateCode -> baseMapper.saveRoleTemplates(userName,roleId, templateCode));
    }

    /**
     * 根据角色id获取其所属模板code
     *
     * @param roleId
     * @return
     */
    @Override
    public List<String> getSelectedTemplateCodeByRoleId(Long roleId) {
        return baseMapper.getSelectedTemplateCodeByRoleId(roleId);
    }

    @Override
    public List<String> findTemplateCodeListByUserName(String userName) {
        return baseMapper.findTemplateCodeListByUserName(userName);
    }

    @Override
    public Page<RoleTemplate> findByPage(Page<RoleTemplate> page,String userName) {
        return page.setRecords(this.baseMapper.findByPage(page,userName));
    }
}
