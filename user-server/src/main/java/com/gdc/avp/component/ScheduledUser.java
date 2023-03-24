package com.gdc.avp.component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gwm.avp.entity.User;
import com.gdc.avp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ScheduledUser {

    @Autowired
    private UserService userService;

    /**
     * 每日3点
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void unlockUsers(){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(User::getPasswordErrorCount,0);
        List<User> list = userService.list(queryWrapper);
        list.forEach(user -> {
            user.setPasswordErrorCount(0);
            userService.updateById(user);
        });
    }

}
