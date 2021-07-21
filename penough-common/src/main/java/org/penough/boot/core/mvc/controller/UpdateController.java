package org.penough.boot.core.mvc.controller;

import cn.hutool.core.bean.BeanUtil;
import org.penough.boot.core.database.entity.SuperEntity;
import org.penough.boot.core.mvc.controller.request.ApiResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 修改
 *
 * @param <Entity>    实体
 * @param <UpdateDTO> 修改参数
 * @author Penough
 * @date 2020/11/04
 */
public interface UpdateController<Entity, UpdateDTO> extends BaseController<Entity> {

    /**
     * 修改
     *
     * @param updateDTO
     * @return
     */
    @ApiOperation(value = "修改")
    @PutMapping
    default ApiResult<Entity> update(@RequestBody @Validated(SuperEntity.Update.class) UpdateDTO updateDTO) {
        ApiResult<Entity> result = handlerUpdate(updateDTO);
        if (result.getDefExec()) {
            Entity model = BeanUtil.toBean(updateDTO, getEntityClass());
            getBaseService().updateById(model);
            result.setResult(model);
        }
        return result;
    }

    /**
     * 自定义更新
     *
     * @param model
     * @return 返回SUCCESS_RESPONSE, 调用默认更新, 返回其他不调用默认更新
     */
    default ApiResult<Entity> handlerUpdate(UpdateDTO model) {
        return ApiResult.successDef();
    }
}
