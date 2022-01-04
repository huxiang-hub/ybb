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
package com.yb.system.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.system.user.entity.Tenant;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 服务类
 *
 * @author Chill
 */
public interface SaITenantService extends BaseService<Tenant> {

    /**
     * 自定义分页
     *
     * @param page
     * @param tenant
     * @return
     */
    IPage<Tenant> selectTenantPage(IPage<Tenant> page, Tenant tenant);

    /**
     * 新增
     *
     * @param tenant
     * @return
     */
    boolean saveTenant(Tenant tenant);

    List<Tenant> getTenantList(String factoryName);

    Tenant getTenant(String factoryName);

    List<Tenant> getList();

    Tenant TenantIdLogin(String tenant);
}
