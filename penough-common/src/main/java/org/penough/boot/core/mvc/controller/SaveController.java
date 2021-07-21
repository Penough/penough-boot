package org.penough.boot.core.mvc.controller;

import cn.hutool.core.bean.BeanUtil;
import org.penough.boot.core.mvc.controller.request.ApiResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 新增
 *
 * @param <Entity>  实体
 * @param <SaveDTO> 保存参数
 * @author Penough
 * @date 2020/11/04
 */
public interface SaveController<Entity, SaveDTO> extends BaseController<Entity> {

    /**
     * 新增
     *
     * @param saveDTO 保存参数
     * @return 实体
     */
    @ApiOperation(value = "新增")
    @PostMapping
    default ApiResult<Entity> save(@RequestBody @Validated SaveDTO saveDTO) {
        ApiResult<Entity> result = handlerSave(saveDTO);
        if (result.getDefExec()) {
            Entity model = BeanUtil.toBean(saveDTO, getEntityClass());
            getBaseService().save(model);
            result.setResult(model);
        }
        return result;
    }

    /**
     * 自定义新增
     *
     * @param model
     * @return 返回SUCCESS_RESPONSE, 调用默认更新, 返回其他不调用默认更新
     */
    default ApiResult<Entity> handlerSave(SaveDTO model) {
        return ApiResult.successDef();
    }

}
