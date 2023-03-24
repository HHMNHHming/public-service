package com.gwm.avp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "字典数据",description = "")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictInfo extends BaseEntity implements Serializable {

    @NotBlank(message = "名称不能为空")
    @ApiModelProperty(value = "数据名",required = true)
    private String name;

    @NotNull(message = "编码不能为空")
    @Length(min = 1,max = 11,message = "长度在1~11字符间")
    @ApiModelProperty(value = "编码",required = true)
    private String businessId;

    @ApiModelProperty(value = "父级ID")
    private Long parentId;
    @ApiModelProperty(value = "父级编码")
    private String pBusinessId;
    @ApiModelProperty(value = "排序权重")
    private int weight;

    @NotBlank(message = "分组不能为空")
    @ApiModelProperty(value = "分组、类型")
    private String keyword;

    @ApiModelProperty(value = "父级",hidden = true)
    private DictInfo parent;

    @ApiModelProperty(value = "子集",hidden = true)
    private List<DictInfo> children;

    @ApiModelProperty(value = "关联模板",hidden = true)
    private Template template;

    private String operator;
}
