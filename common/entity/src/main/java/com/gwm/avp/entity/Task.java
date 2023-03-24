package com.gwm.avp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(value = "任务数据",description = "")
@Data
public class Task extends BaseEntity implements Serializable {


    private Long templateId;

    private String name;

    private Boolean period;

    private Integer status;

    @ApiModelProperty(value = "释放状态（0全部释放，1 部分释放）",hidden = true)
    private Boolean releaseAll;

    @ApiModelProperty(value = "开关状态（ 0 禁用释放， 1 启用释放，2 部分禁用，3 部分启用）",hidden = true)
    private Integer switchStatus;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    private List<TaskItem> items;

    @ApiModelProperty(value = "内测数据",hidden = true)
    private TestData closeTest;

    @ApiModelProperty(value = "灰测数据",hidden = true)
    private TestData grayScaleTest;

    @ApiModelProperty(value = "策略信息",hidden = true)
    private List<TaskStrategy> schedule;

    private String operator;

    @ApiModelProperty(value = "策略信息VO",hidden = true)
    List<ScheduleVo> scheduleVo;

}