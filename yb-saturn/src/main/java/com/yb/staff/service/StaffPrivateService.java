package com.yb.staff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.staff.entity.StaffPrivate;

public interface StaffPrivateService extends IService<StaffPrivate> {

    StaffPrivate getPrivateInfo(StaffPrivate usPrivate);
}
