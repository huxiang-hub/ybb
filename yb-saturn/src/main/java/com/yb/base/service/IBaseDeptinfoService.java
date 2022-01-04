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
package com.yb.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.base.entity.BaseDeptinfo;
import com.yb.base.vo.BaseDeptinfoVO;
import com.yb.base.vo.DeptNameModel;
import com.yb.process.vo.ProcessClassifyVO;

import java.util.List;

/**
 * 部门结构_yb_ba_dept 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IBaseDeptinfoService extends IService<BaseDeptinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param baseDeptinfo
     * @return
     */
    IPage<BaseDeptinfoVO> selectBaseDeptinfoPage(IPage<BaseDeptinfoVO> page, BaseDeptinfoVO baseDeptinfo);

    /**
     * 查询所有部门
     * @return
     */
    List<BaseDeptinfoVO> baseDeptinfos();
    /**
     * 查询生产所有部门
     * @return
     */
    List<DeptNameModel> getPdNameByClassify(Integer classify);

    /**
     * 查询某个部门的工序
     */
    List<ProcessClassifyVO> getAllProcess(Integer dpId);

    /**
     * 查询对应的部门的ID，不存在则返回空
     */
    Integer getDpIdByDpName(String dpName);

    /**
     * 获取工序对应的生产部门的名称
     * @param prId
     * @return
     */
    List<BaseDeptinfo> getDpNameByPrId(Integer prId);

    /**
     * 查询分级部门信息
     * @return
     */
    List<BaseDeptinfoVO> getHierarchyDeptList();
}
