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
package com.yb.exeset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.vo.ExecuteWasteVO;
import com.yb.exeset.entity.ExesetQuality;
import com.yb.exeset.vo.ExesetQualityVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 质量巡检设置_yb_exeset_quality Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ExesetQualityMapper extends BaseMapper<ExesetQuality> {

    /**
     * 自定义分页
     *
     * @param page
     * @param exesetQuality
     * @return
     */
    List<ExesetQualityVO> selectExesetQualityPage(IPage page, ExesetQualityVO exesetQuality);

    ExesetQuality getQualityModel(Integer mId);
    /***故停机窗口设置 设置弹出窗口 ，窗口小时时间 等
     * remark    yb_exeset_quality
     */
    public boolean updateQualityPoP(ExesetQuality quality);

    List<ExecuteWasteVO> getQualityList(Integer sdId);

}
