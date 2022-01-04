package com.anaysis.api;

import com.anaysis.channel.WebSocketServer;
import com.anaysis.entity.FzProcedureDO;
import com.anaysis.entity.FzScMachineJobDO;
import com.anaysis.entity.Tree;
import com.anaysis.service.FzProcedureService;
import com.anaysis.service.FzScMachineJobService;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/")
public class ApiController {
    @Autowired
    private FzProcedureService fzprocedureservice;

    @Autowired
    private FzScMachineJobService fzscmachinejobservice;

    /**
     *
     */

    @GetMapping("/pro_tree")
    @ResponseBody
    public Tree<FzProcedureDO> pro_tree(){
        Tree<FzProcedureDO> tree = fzprocedureservice.getTree();
        return tree;
    }

    @GetMapping("/prodo/{idint}")
    @ResponseBody
    public FzProcedureDO prodo(@PathVariable("idint") Integer idint){
        FzProcedureDO prodo = fzprocedureservice.getId(idint);
        return prodo;
    }

    @GetMapping("/scmachinejobs")
    @ResponseBody
    public List<FzScMachineJobDO> scmachinejobs(@RequestParam String limit,@RequestParam String offset, @RequestParam String page,
        @RequestParam String deptId, @RequestParam String cjobCode, @RequestParam String iGzzxIds, @RequestParam String sqlserverlimit,@RequestParam String machineId){

        Map<String,Object> params = new HashMap<String, Object>();
        params.put("limit", limit);
        params.put("offset", offset);
        params.put("deptId", deptId);
        params.put("cjobCode", cjobCode);
        params.put("iGzzxIds", iGzzxIds);
        params.put("sqlserverlimit", sqlserverlimit);
        params.put("machineId", machineId);
        params.put("page", page);
        List<FzScMachineJobDO> scmachineJob = fzscmachinejobservice.getPageList(params);
        return scmachineJob;
    }

    @GetMapping("/total")
    @ResponseBody
    public Integer total(@RequestParam String limit,@RequestParam String offset, @RequestParam String page,
                         @RequestParam String deptId, @RequestParam String cjobCode, @RequestParam String iGzzxIds, @RequestParam String sqlserverlimit,@RequestParam String machineId){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("limit", limit);
        params.put("offset", offset);
        params.put("deptId", deptId);
        params.put("cjobCode", cjobCode);
        params.put("iGzzxIds", iGzzxIds);
        params.put("sqlserverlimit", sqlserverlimit);
        params.put("machineId", machineId);
        params.put("page", page);
        int total = fzscmachinejobservice.count(params);
        return total;
    }

    @GetMapping("/getforobject/{ids}")
    @ResponseBody
    public List<FzScMachineJobDO> getforobject(@PathVariable("ids") String ids){
        List<FzScMachineJobDO> fzScMachineJobDOS =  fzscmachinejobservice.getListByIds(ids);
        return fzScMachineJobDOS;
    }

    @GetMapping("/listbymid")
    @ResponseBody
    public List<FzScMachineJobDO> listbymid(@RequestParam String iGzzxIds,@RequestParam String machineId){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("iGzzxIds", iGzzxIds);
        params.put("machineId", machineId);
        List<FzScMachineJobDO> scmachineJob = fzscmachinejobservice.list(params);
        return scmachineJob;
    }

    @GetMapping("/send")
    @ResponseBody
    public R send(@RequestParam String maId){
        try {
            WebSocketServer.sendInfo("确认弹出窗口",maId);//告诉机台可以弹出窗口，--需要传递是停机还是质检
        } catch (IOException e) {
            e.printStackTrace();
            return R.fail("发送失败！");
        }
        return R.success("连接成功！");
    }
}
