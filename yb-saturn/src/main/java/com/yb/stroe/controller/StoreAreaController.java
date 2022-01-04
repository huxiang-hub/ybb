package com.yb.stroe.controller;

import com.yb.stroe.entity.StoreArea;
import com.yb.stroe.service.StoreAreaService;
import com.yb.stroe.vo.StoreAreaVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/StoreArea")
@Api(tags = "库区管理")
public class StoreAreaController {

    @Autowired
    private StoreAreaService storeAreaService;

    /**
     * 新增仓库管理
     * @param storeArea
     * @return
     */
    @PostMapping("save")
    @ApiOperation(value = "新增", notes = "传入storeArea")
    public R save(StoreArea storeArea) {
        Date date = new Date();
        storeArea.setCreateAt(date);
        storeArea.setUpdateAt(date);
        int insert = storeAreaService.getBaseMapper().insert(storeArea);
        if(insert > 0){
            return R.data(storeArea);
        }
        return R.fail("保存失败");
    }

    @GetMapping("list")
    @ApiOperation(value = "查询所有库区")
    public R list(){
        List<StoreArea> storeSeatList = storeAreaService.list();
        return R.data(storeSeatList);
    }
    @PostMapping("delete")
    @ApiOperation(value = "删除", notes = "传入ids")
    public R delete(@ApiParam(value = "主键集合", required = true) @RequestParam String ids){
        return R.status(storeAreaService.removeByIds(Func.toIntList(ids)));
    }
    @PostMapping("update")
    @ApiOperation(value = "修改", notes = "传入storeArea")
    public R update(StoreArea storeArea){
        storeArea.setUpdateAt(new Date());
        return R.status(storeAreaService.updateById(storeArea));
    }
    @GetMapping("selectStoreAreaByStId")
    @ApiOperation(value = "根据库位id查询库区", notes = "传入库位id")
    public R selectStoreAreaByStId(Integer stId){
        return R.data(storeAreaService.selectStoreAreaByStId(stId));
    }
}
