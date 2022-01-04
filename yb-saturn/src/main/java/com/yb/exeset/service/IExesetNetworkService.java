package com.yb.exeset.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.exeset.entity.ExesetNetwork;
import com.yb.exeset.vo.ExesetNetworkVO;

/**
 * 网络设置管理_yb_exeset_network 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IExesetNetworkService extends IService<ExesetNetwork> {

    /**
     * 自定义分页
     *
     * @param page
     * @param exesetNetwork
     * @return
     */
    IPage<ExesetNetworkVO> selectExesetNetworkPage(IPage<ExesetNetworkVO> page, ExesetNetworkVO exesetNetwork);

    /**
     *网络设置管理_yb_exeset_network
     */
    public ExesetNetwork getNetwork(Integer maId);

    /**
     * 网络设置(修改)管理_yb_exeset_network
     * */
    public boolean setNetwork( ExesetNetwork exesetNetwork);
    /**
     * 设置是否通信
     *
     * */
    public  boolean setIsChart(Integer maId,Integer isChart);

}
