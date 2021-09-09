package org.penough.boot.mvc.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.penough.boot.common.utils.DateUtils;
import org.penough.boot.database.mybatis.conditions.Wraps;
import org.penough.boot.database.mybatis.conditions.query.QueryWrap;
import org.penough.boot.mvc.controller.request.PageVO;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

/**
 * 分页Controller
 *
 * @param <Entity>  实体
 * @param <PageDTO> 分页参数
 * @author Penough
 * @date 2020/11/04
 */
public interface PageController<Entity, PageDTO> extends BaseController<Entity> {

    /**
     * 根据 bean字段 反射出 数据库字段
     *
     * @param beanField
     * @param clazz
     * @return
     */
    default String getDbField(String beanField, Class<?> clazz) {
        Field field = ReflectUtil.getField(clazz, beanField);
        if (field == null) {
            return StrUtil.EMPTY;
        }
        TableField tf = field.getAnnotation(TableField.class);
        if (tf != null && StringUtils.isNotBlank(tf.value())) {
            String str = tf.value();
            return str;
        }
        return StrUtil.EMPTY;
    }

    /**
     * 处理参数
     *
     * @param params
     */
    default void handlerQueryParams(PageVO<Entity> params) {
    }

    /**
     * 执行查询
     * <p>
     * 可以覆盖后重写查询逻辑
     *
     * @param params
     * @param page
     * @param defSize
     */
    default void query(PageVO<Entity> params, IPage<Entity> page, Long defSize) {
        handlerQueryParams(params);

        if (defSize != null) {
            page.setSize(defSize);
        }
        Entity model = BeanUtil.toBean(params.getModel(), getEntityClass());
        dealEmptyStr(model);
        QueryWrap<Entity> wrapper = Wraps.q(model);

        handlerWrapper(wrapper, params);
        getBaseService().page(page, wrapper);

        // 处理结果
        handlerResult(page);
    }


    /**
     * 处理时间区间，可以覆盖后处理组装查询条件
     *
     * @param wrapper
     * @param params
     */
    default void handlerWrapper(QueryWrap<Entity> wrapper, PageVO<Entity> params) {
        if (CollUtil.isNotEmpty(params.getMap())) {
            Map<String, String> map = params.getMap();
            //拼装区间
            for (Map.Entry<String, String> field : map.entrySet()) {
                String key = field.getKey();
                String value = field.getValue();
                if (StrUtil.isEmpty(value)) {
                    continue;
                }
                if (key.endsWith("_st")) {
                    String beanField = StrUtil.subBefore(key, "_st", true);
                    wrapper.ge(getDbField(beanField, getEntityClass()), DateUtils.getStartTime(value));
                }
                if (key.endsWith("_ed")) {
                    String beanField = StrUtil.subBefore(key, "_ed", true);
                    wrapper.le(getDbField(beanField, getEntityClass()), DateUtils.getEndTime(value));
                }
            }
        }
    }

    /**
     * 自定义处理返回结果
     *
     * @param page
     */
    default void handlerResult(IPage<Entity> page) {
        // 调用注入方法
    }

    /**
     * 空串处理<br/>
     * 该步骤是为了应对{@link #query(PageVO,IPage,Long)}方法内的{@link Wraps#q(Entity)}构造方法 <br/>
     * 由于Wraps的skipEmpty在直接通过实体构造的情况下是不生效的(只在eq,ge等方法生效) <br/>
     * 且空串一般情况下是不参与查询过滤的 <br/>
     * 因此在该方法前进行一次字符串字段的判空处理，如果为空串则直接置空 <br/>
     * 如果不需要判空处理，重写即可
     * @param model 实体
     */
    default void dealEmptyStr(Entity model){
        Field[] fields = model.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(f -> {
            try {
                f.setAccessible(true);
                Object val = f.get(model);
                if(val instanceof String){
                    if(StringUtils.isBlank((String)val)) f.set(model, null);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
