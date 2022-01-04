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
package org.springblade.saturn.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springblade.saturn.entity.Mixbox;

/**
 * 印联盒（本租户的盒子），由总表分发出去实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_machine_mixbox")
@ApiModel(value = "MachineMixbox对象", description = "印联盒（本租户的盒子），由总表分发出去")
public class MixboxVO extends Mixbox {

    private static final long serialVersionUID = 1L;
    /**
     *盒子型号
     */
    private String hdIdStr;
    /**
     * Excel出厂状态
     */
    private String depositoryStr;
    /**
     *Excel设备Id
     */
    private String maIdStr;
    /**
     * 设备编号
     */
    private String mno;
    /**
     * 部门信息
     */
    private String dpName;
    /**
     * 设备名字
     */
    private String name;
    /**
     * 设备品牌
     */
    private String  brand;
    /**
     * 设备规格
     */
    private String  specs;
    /**
     * 设备型号
     */
    private String  model;
    /**
     * 设备图片
     */
    private String  image;
    /**
     *速度
     */
    private String speed;









}
