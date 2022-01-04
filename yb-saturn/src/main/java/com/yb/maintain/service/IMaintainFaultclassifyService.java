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
package com.yb.maintain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.maintain.entity.MaintainFaultclassify;
import com.yb.maintain.vo.MaintainFaultclassifyVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备故障表分类_yb_machine_faultclassify 服务类
 *
 * @author Blade
 * @since 2020-03-15
 */
public interface IMaintainFaultclassifyService extends IService<MaintainFaultclassify> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param maintainFaultclassify
	 * @return
	 */
	IPage<MaintainFaultclassifyVO> selectMaintainFaultclassifyPage(IPage<MaintainFaultclassifyVO> page, MaintainFaultclassifyVO maintainFaultclassify);

    List<MaintainFaultclassifyVO> getFaultList();

	/**
	 * 获取故障停机分类树信息
	 * @return
	 */
    List<MaintainFaultclassifyVO> getMaintainFaultclassifyTree();

	/**
	 * 根据子级查询父级Fvalue值
	 * @param fvalue
	 * @return
	 */
    String getParentFvalue(String fvalue);
}
