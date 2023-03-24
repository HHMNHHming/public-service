package com.gdc.avp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gwm.avp.entity.User;

public interface UserService extends IService<User> {

    void updatePassword(User user);

    void saveUser(User user);

    int getPasswordUpdatedDays(String userName);
}
