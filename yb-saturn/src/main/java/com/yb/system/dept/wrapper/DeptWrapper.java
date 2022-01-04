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
package com.yb.system.dept.wrapper;

import com.yb.base.entity.BaseDeptinfo;
import com.yb.system.dept.service.SaIDeptService;
import com.yb.system.dept.vo.SaDeptVO;
import org.springblade.common.constant.CommonConstant;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.node.INode;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author Chill
 */
public class DeptWrapper extends BaseEntityWrapper<BaseDeptinfo, SaDeptVO> {

    private static SaIDeptService deptService;

    static {
        deptService = SpringUtil.getBean(SaIDeptService.class);
    }

    public static DeptWrapper build() {
        return new DeptWrapper();
    }

    @Override
    public SaDeptVO entityVO(BaseDeptinfo dept) {
        SaDeptVO deptVO = BeanUtil.copy(dept, SaDeptVO.class);
        if (Func.equals(dept.getPId(), CommonConstant.TOP_PARENT_ID)) {
            deptVO.setParentName(CommonConstant.TOP_PARENT_NAME);
        } else {
            BaseDeptinfo parent = deptService.getById(dept.getPId());
            deptVO.setParentName(parent.getDpName());
        }
        return deptVO;
    }

    public List<INode> listNodeVO(List<BaseDeptinfo> list) {
        List<INode> collect = list.stream().map(dept -> BeanUtil.copy(dept, SaDeptVO.class)).collect(Collectors.toList());
        return ForestNodeMerger.merge(collect);
    }

}
