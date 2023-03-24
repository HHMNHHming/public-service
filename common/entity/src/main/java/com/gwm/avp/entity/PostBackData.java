package com.gwm.avp.entity;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "回传数据",description = "")
@Data
public class PostBackData implements Serializable {

    private String id;

    //模板编码
    private String template_id;
    //车辆VIN
    private String vin;
    private Long task_id;
    private String task_name;
    //车系编码
    private String car_series_id;
    //车型编码
    private String car_type_id;
    //软件版本编码
    private String iso_version_id;
    //软件版本号
    private String version;
    //标签编码  多个逗号间隔
    private String tags;
    //数据状态 error异常、无效 init未处理 analyzing解析中 decoding视频解码中 done已完成 deleted已删除
    private String status;
    //记录数据状态描述信息
    private String descriptors;
    //摘要名称 展示摘要信息
    private String digest_name;
    //回传详细信息
    private Object content;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date create_time;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;
    //触发时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String trigger_time;
    //故障码
    private String fault_code;
    //处理后数据存储路径
    private String handle_path;
    //发送到解析服务的消息
    private String message;
    private String bucket_name;
    //原始数据存储路径
    private String original_path;
    //解析后存储路径
    private String store_path;
    /** 特别声明 **/
    /** 原始数据目录下的文件列表应当通过oss接口实现动态查询  **/
    //最终显示文件路径
    private JSONArray all_path;
}
