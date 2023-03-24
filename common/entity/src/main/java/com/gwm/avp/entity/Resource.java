package com.gwm.avp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * (Resourse)表实体类
 *
 * @author makejava
 * @since 2021-10-21 10:26:57
 */
@Data
public class Resource extends BaseEntity {
    
    private String name;
    
    private String title;
    
    private String url;
    
    private String size;
    
    private String icon;
    //0.menu;1.resource
    private Integer type;
    
    private Integer circle;
    
    private Integer sort;
    
    private Long parentId;

    @TableField(exist = false)
    private Resource parent;
    
    private Date createTime;
    
    private Date updateTime;

    private boolean hidden;

    @TableField(exist = false)
    private List<Resource> childrenList;

    private String path;

    private String stype;
}