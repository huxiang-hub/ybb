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
package com.yb.fz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.fz.entity.FzMacheset;
import com.yb.fz.mapper.FzMachesetMapper;
import com.yb.fz.service.FzMachesetService;
import com.yb.prod.entity.ProdClassify;
import com.yb.prod.mapper.ProdClassifyMapper;
import org.springframework.stereotype.Service;

/**
 * 产品信息（product） 服务实现类
 *ProdPartsinfoServiceImpl
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class FzMachesetServiceImpl extends ServiceImpl<FzMachesetMapper, FzMacheset> implements FzMachesetService {

}
