package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "触发源数据",description = "")
@Data
public class TaskStrategy extends BaseEntity implements Serializable {

    private String name;

    private String digestName;

    private Long templateId;

    private String content;

    private String tagIds;

    /**
     * 2 feedback  3 shadow
     */
    private int collectMode;

    /**
     * 是否可以删除
     */
    @ApiModelProperty(value = "是否可以删除",hidden = true)
    private boolean canDel;
    /**
     * 是否可以编辑
     */
    @ApiModelProperty(value = "是否可以编辑",hidden = true)
    private boolean canUpdate;

    @ApiModelProperty(value = "任务状态",hidden = true)
    private Integer status;

    private String operator;

}