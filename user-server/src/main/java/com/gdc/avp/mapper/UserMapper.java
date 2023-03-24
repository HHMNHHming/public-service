package com.gdc.avp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gwm.avp.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Update("UPDATE user u set u.password_update_time = NOW() where u.user_name = #{userName}")
    void updatePasswordTime(@Param("userName") String userName);

    @Select("select TIMESTAMPDIFF(DAY,u.password_update_time,DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%S')) as day from user u where u.user_name = #{userName}")
    int getPasswordUpdatedDays(@Param("userName") String userName);
}
