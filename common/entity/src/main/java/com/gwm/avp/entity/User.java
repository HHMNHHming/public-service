package com.gwm.avp.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "用户", description = "")
@Data
public class User extends BaseEntity {

    private String userName;

    private String pwd;

    private Long roleId;

    @JSONField(serialize = false)
    @TableField(exist = false)
    private String roleName;

    private int enabled;

    private int passwordErrorCount;

    private Date passwordUpdateTime;
}
