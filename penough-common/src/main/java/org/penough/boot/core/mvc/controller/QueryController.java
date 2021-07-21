package org.penough.boot.core.mvc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.penough.boot.core.database.mybatis.conditions.query.QueryWrap;
import org.penough.boot.core.mvc.controller.request.ApiResult;
import org.penough.boot.core.mvc.controller.request.PageVO;
import org.penough.boot.core.database.mybatis.conditions.Wraps;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;

/**
 * 查询Controller
 *
 * @param <Entity>  实体
 * @param <Id>      主键
 * @author Penough
 * @date 2020/11/04
 */
public interface QueryController<Entity, Id extends Serializable, PageDTO> extends PageController<Entity, PageDTO>{

    /**
     * 查询
     *
     * @param id 主键id
     * @return 查询结果
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", dataType = "long", paramType = "query"),
    })
    @ApiOperation(value = "查询", notes = "查询")
    @GetMapping("/{id}")
    default ApiResult<Entity> get(@PathVariable Id id) {
        return success(getBaseService().getById(id));
    }



    /**
     * 批量查询
     *
     * @param data 批量查询
     * @return 查询结果
     */
    @ApiOperation(value = "批量查询", notes = "批量查询")
    @PostMapping("/query")
    default ApiResult<List<Entity>> query(@RequestBody Entity data) {
        QueryWrap<Entity> wrapper = Wraps.q(data);
        return success(getBaseService().list(wrapper));
    }

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "分页列表查询")
    @PostMapping(value = "/page")
    default ApiResult<IPage<Entity>> page(@RequestBody @Validated PageVO<Entity> params) {
        // 处理参数
        IPage<Entity> page = params.getPage();
        query(params, page, null);
        return success(page);
    }

}
