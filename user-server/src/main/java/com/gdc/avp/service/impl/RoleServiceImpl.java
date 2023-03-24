package com.gdc.avp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gwm.avp.entity.Role;
import com.gdc.avp.mapper.RoleMapper;
import com.gdc.avp.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
