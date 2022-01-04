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
package com.yb.exeset.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.execute.vo.ExecuteWasteVO;
import com.yb.exeset.entity.ExesetQuality;
import com.yb.exeset.mapper.ExesetQualityMapper;
import com.yb.exeset.service.IExesetQualityService;
import com.yb.exeset.vo.ExesetQualityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 质量巡检设置_yb_exeset_quality 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ExesetQualityServiceImpl extends ServiceImpl<ExesetQualityMapper, ExesetQuality> implements IExesetQualityService {

    @Autowired
    private ExesetQualityMapper qualityMapper;

    @Override
    public IPage<ExesetQualityVO> selectExesetQualityPage(IPage<ExesetQualityVO> page, ExesetQualityVO exesetQuality) {
        return page.setRecords(baseMapper.selectExesetQualityPage(page, exesetQuality));
    }

    @Override
    public ExesetQuality getQualityModel(Integer mId) {

        return qualityMapper.getQualityModel(mId);
    }

    @Override
    public boolean updateQualityPoP(ExesetQuality quality) {
        return qualityMapper.updateQualityPoP(quality);
    }

    @Override
    public List<ExecuteWasteVO> getQualityList(Integer sdId) {
        return qualityMapper.getQualityList(sdId);
    }


}
