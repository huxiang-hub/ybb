/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yb.exeset.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 网络设置管理_yb_exeset_network实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_exeset_network")
@ApiModel(value = "ExesetNetwork对象", description = "网络设置管理_yb_exeset_network")
public class ExesetNetwork implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID")
    private Integer maId;
    /**
     * 服务地址
     */
    @ApiModelProperty(value = "服务地址")
    private String serverAddr;
    /**
     * 服务端口
     */
    @ApiModelProperty(value = "服务端口")
    private String serverPort;
    /**
     * 是否自动更新1自动更新0手动更新
     */
    @ApiModelProperty(value = "是否自动更新1自动更新0手动更新")
    private Integer isUpdate;

    @ApiModelProperty(value = "是否通信1是0否")
    private Integer isChart;

    @ApiModelProperty(value = "盒子连接WIFI的名字")
    private String wifiName;

    @ApiModelProperty(value = "盒子连接的WIFI密码")
    private String wifiPassword;
    @ApiModelProperty(value = "WIFI ip ")
    private String wifiIp;

    @ApiModelProperty(value = "WIFI ip ")
    private String wifiCovercode;

    @ApiModelProperty(value = "WIFI ip ")
    private String wifiWayget;

    private String screenIp;

    @ApiModelProperty(value = "createAt ")
    private Date createAt;

    @ApiModelProperty(value = "updateAt ")
    private Date updateAt;


    @ApiModelProperty(value = "设备屏幕的端口")
    private String screenPort;


}
