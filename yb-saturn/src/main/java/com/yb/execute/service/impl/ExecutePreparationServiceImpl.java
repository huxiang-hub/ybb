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
package com.yb.execute.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.execute.dto.ExecutePreparationDTO;
import com.yb.execute.entity.ExecutePreparation;
import com.yb.execute.mapper.ExecutePreparationMapper;
import com.yb.execute.service.IExecutePreparationService;
import com.yb.execute.vo.ExecutePreparationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 生产准备记录_yb_execute_preparation 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ExecutePreparationServiceImpl extends ServiceImpl<ExecutePreparationMapper, ExecutePreparation> implements IExecutePreparationService {

    @Autowired
    private ExecutePreparationMapper  mapper;

    @Override
    public IPage<ExecutePreparationDTO> selectExecutePreparationPage(IPage<ExecutePreparationDTO> page, ExecutePreparationDTO executePreparation) {
        return page.setRecords(baseMapper.selectExecutePreparationPage(page, executePreparation));
    }



    @Override
    public boolean saveState(ExecutePreparationVO executePreparation) {
        return mapper.saveState(executePreparation);
    }

    @Override
    public boolean updataExecutePreparation(ExecutePreparation preparation) {
        return mapper.updataExecutePreparation(preparation);
    }

    @Override
    public ExecutePreparation getExecutePreparationById(Integer id) {
        Object obj =  mapper.getExecutePreparationById(id);
        return (ExecutePreparation)obj;
    }

    @Override
    public boolean savePreparationo(ExecutePreparation preparation) {
        return mapper.savePreparationo(preparation);
    }



    public int insertEndTime(Date finishAt) {
        ExecutePreparation executePreparation=new ExecutePreparation();
        Integer startNum = executePreparation.getStartNum();
        Integer endNum = executePreparation.getEndNum();
        Integer testPaper =null;
        if(endNum!=null && startNum!=null){
            if((endNum-startNum)>0){
                  executePreparation.setTestPaper(testPaper);
            }else{
                executePreparation.setTestPaper(endNum);
            }
        }
        executePreparation.setFinishAt(finishAt);
        executePreparation.setStartAt(new Date());
        int i = mapper.insert(executePreparation);
        return i;
    }


}
