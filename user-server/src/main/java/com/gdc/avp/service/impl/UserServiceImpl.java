package com.gdc.avp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gwm.avp.entity.User;
import com.gdc.avp.mapper.UserMapper;
import com.gdc.avp.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    @Transactional
    public void updatePassword(User user) {
        baseMapper.updateById(user);
        baseMapper.updatePasswordTime(user.getUserName());
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        save(user);
        baseMapper.updatePasswordTime(user.getUserName());
    }

    @Override
    public int getPasswordUpdatedDays(String userName) {
        return baseMapper.getPasswordUpdatedDays(userName);
    }
}
