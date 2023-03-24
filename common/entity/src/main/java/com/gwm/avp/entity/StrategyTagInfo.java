package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "标签和策略对应关系",description = "")
@Data
public class StrategyTagInfo extends BaseEntity implements Serializable {

    private Long strategyId;

    private String tagId;
}