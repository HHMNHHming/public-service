package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * (SysRole)实体类
 *
 * @author makejava
 * @since 2021-03-19 16:58:41
 */
@ApiModel(value = "角色",description = "")
@Data
public class Role extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 439454005981518647L;

    /**
     * 名称
     */
    @NotEmpty
    @Length(max = 50)
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 状态： 0 禁用 1 启用
     */
    private Integer status;
    /**
     * 排序
     */
//    private Integer sort;
//
//    private List<Menu> menuList;
//    private List<Resource> resourceList;

}
