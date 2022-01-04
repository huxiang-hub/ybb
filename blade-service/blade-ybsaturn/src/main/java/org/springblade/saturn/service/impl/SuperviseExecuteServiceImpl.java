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
package org.springblade.saturn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.saturn.entity.SuperviseExecute;
import org.springblade.saturn.mapper.SuperviseExecuteMapper;
import org.springblade.saturn.service.ISuperviseExecuteService;
import org.springframework.stereotype.Service;

/**
 * 设备清零日志表 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class SuperviseExecuteServiceImpl extends ServiceImpl<SuperviseExecuteMapper, SuperviseExecute> implements ISuperviseExecuteService {

}