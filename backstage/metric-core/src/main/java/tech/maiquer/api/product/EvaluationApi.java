package tech.maiquer.api.product;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.maiquer.common.model.LayuiQuery2;
import tech.maiquer.common.model.LayuiResult;
import tech.maiquer.common.model.Paging;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.Evaluation;
import tech.maiquer.system.service.EvaluationService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@Api(tags = "测评接口")
@RequestMapping("/api/evaluation")
public class EvaluationApi {

    /**
     * 服务对象
     */
    @Resource
    private EvaluationService evaluationService;

    @GetMapping({"/queryAll", "/queryAll/{type}"})
    @ApiOperation(value = "查询所有", notes = "null")
    public Result queryAll(@PathVariable(name = "type", required = false) String type) {
        return type == null ? evaluationService.queryAll() : evaluationService.queryAllByType(Integer.parseInt(type));
    }

    /**
     * 分页查询
     *
     * @param current 当前页
     * @param size    分页大小
     * @return 查询结果
     */
    @GetMapping("/queryByPage/{page}/{size}")
    @ApiOperation(value = "分页查询", notes = "注意正确传参")
    public Paging<Evaluation> queryByPage(@ApiParam(name = "page", value = "当前页", required = true) @PathVariable("page") Integer current, @ApiParam(name = "size", value = "分页大小", required = true) @PathVariable("size") Integer size) {
        return evaluationService.queryByPage(current, size);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/queryById/{id}")
    @ApiOperation(value = "通过Id查询", notes = "注意正确传参")
    public Result queryById(@ApiParam(name = "id", value = "测评Id", required = true) @PathVariable("id") Integer id) {
        return evaluationService.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param evaluation 实体
     * @return 新增结果
     */
    @PostMapping("/addEva")
    @PreAuthorize("hasPermission('/api/evaluation/addEva','sys:admin:addEva')")
    @ApiOperation(value = "添加测评", notes = "注意正确传参")
    public Result add(@RequestBody Evaluation evaluation) {
        return evaluationService.insert(evaluation);
    }

    /**
     * 编辑数据
     *
     * @param evaluation 实体
     * @return 编辑结果
     */
    @PutMapping("/editEva")
    @PreAuthorize("hasPermission('/api/evaluation/editEva','sys:admin:editEva')")
    @ApiOperation(value = "编辑测评", notes = "注意正确传参")
    public Result edit(@RequestBody Evaluation evaluation) {
        return evaluationService.update(evaluation);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/deleteEva/{id}")
    @PreAuthorize("hasPermission('/api/evaluation/deleteEva','sys:admin:deleEva')")
    @ApiOperation(value = "通过Id删除测评", notes = "注意正确传参")
    public Result deleteById(@ApiParam(name = "id", value = "测评Id", required = true) @PathVariable("id") Integer id) {
        return evaluationService.deleteById(id);
    }

    @DeleteMapping("/batchRemove")
    @PreAuthorize("hasPermission('/api/evaluation/batchRemove','sys:admin:deleEva')")
    @ApiOperation(value = "通过Ids删除测评",notes = "注意正确传参")
    public Result deleteByIds(@ApiParam(name="evaIds",value = "测评IDs",required = true) @RequestParam("evaIds") String evaIds) {
        return evaluationService.deleteByIds(evaIds);
    }

    @GetMapping("/layui/list")
    public LayuiResult<Object> getEvaluationList(LayuiQuery2 param) {
        List<Evaluation> list = evaluationService.getEvaluationList(param);
        Long count = evaluationService.countEvaluationList(param);
        return LayuiResult.success(list,count);
    }

}
