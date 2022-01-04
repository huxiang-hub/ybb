package com.sso.supervise.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sso.supervise.entity.SuperviseExecute;

public interface ISuperviseExecuteService extends IService<SuperviseExecute> {

    SuperviseExecute getExecuteOrder(Integer maId);

}