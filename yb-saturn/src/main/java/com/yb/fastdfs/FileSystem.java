package com.yb.fastdfs;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Description: fastfds 文件上传返回类
 * @Author my
 * @Date Created in 2020/9/17 14:12
 */
@ApiModel("fastFDS文件上传返回类")
@Data
public class FileSystem {

    private Integer fileId;

    private String filePath;

    private String fileName;
}
