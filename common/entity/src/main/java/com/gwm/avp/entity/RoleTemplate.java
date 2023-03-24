package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色-模板对应类
 */
@ApiModel(value = "角色-模板对应类",description = "")
@Data
public class RoleTemplate extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8316520606838033582L;

    private Long roleId;

    private String templateCode;

}
