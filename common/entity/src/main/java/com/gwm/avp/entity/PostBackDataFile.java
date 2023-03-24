package com.gwm.avp.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "回传数据文件列表",description = "")
@Data
public class PostBackDataFile implements Serializable {
    //文件名
    private String fileName;
    //文件大小
    private Long fileSize;
    //文件存储在oss的索引
    private String fileIndex;

//    private boolean compressedData;
}
