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
package com.yb.prod.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.prod.entity.ProdProcelink;
import com.yb.prod.vo.ProdPartsinfoVo;
import com.yb.prod.vo.ProdProcelinkVO;

import java.util.List;

/**
 * 产品对应工序关联表 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IProdProcelinkService extends IService<ProdProcelink> {

    /**
     * 自定义分页
     *
     * @param page
     * @param prodProcelink
     * @return
     */
    IPage<ProdProcelinkVO> selectProdProcelinkPage(IPage<ProdProcelinkVO> page, ProdProcelinkVO prodProcelink);

    /**
     * 部件id查询工序id
     */
    List<ProdProcelinkVO> list(Integer ptId);

    /**
     * 查询产品对应部件和工序
     * @param pdId
     * @return
     */
    List<ProdPartsinfoVo> rowSelectPr(Integer pdId, Integer pdType);

    /**
     * 查询工序分类和工序信息
     * @param pdId
     * @return
     */
    List<ProdProcelinkVO> rowSelectPd(Integer pdId, Integer pdType);
    /**
     * 删除产品对应部件对应的工序
     */
    boolean removeByPdIdAndPdType(Integer pdId, Integer pdType);
}
