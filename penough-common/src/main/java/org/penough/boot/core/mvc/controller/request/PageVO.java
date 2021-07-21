package org.penough.boot.core.mvc.controller.request;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.penough.boot.core.database.extra.AntiSqlFilter;
import org.penough.boot.core.database.entity.SuperEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 后台分页信息
@SuppressWarnings("javadoc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> {

    @NotNull(message = "查询对象model不能为空")
    @ApiModelProperty(value = "查询参数", required = true)
    private T model;
    // 每页记录数
    @ApiModelProperty("每页记录数")
    private Integer pageSize;
    // 当前页码
    @ApiModelProperty("当前页码")
    private Integer pageNumber;
    // 总页数
    @ApiModelProperty("总页数")
    private Integer totalPage;
    // 总记录数
    @ApiModelProperty("总记录数")
    private Integer totalRows;
    // 排序字段
    @ApiModelProperty(value = "排序,默认id", allowableValues = "id,createTime,updateTime", example = "id")
    private String sort = SuperEntity.ID;
    @ApiModelProperty(value = "排序规则, 默认descending", allowableValues = "descending,ascending", example = "descending")
    private String order = "descending";
    @ApiModelProperty(value = "扩展参数", notes = "目前实现[_st]到[_ed]参数，用于控制时间区间")
    private Map<String, String> map = new HashMap<>(1);

    /**
     * @param pageSize
     * @param pageNumber
     */
    public PageVO(Integer pageSize, Integer pageNumber) {
        super();
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    /**
     * @param pageSize
     * @param pageNumber
     * @param totalPage
     */
    public PageVO(Integer pageSize, Integer pageNumber, Integer totalPage) {
        super();
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.totalPage = totalPage;
    }

    @JsonIgnore
    public IPage getPage() {
        PageVO params = this;
        if (StrUtil.isEmpty(params.getSort())) {
            Page page = new Page(params.getPageNumber(), params.getPageSize());
            return page;
        }

        Page page = new Page(params.getPageNumber(), params.getPageSize());
        List<OrderItem> orders = new ArrayList<>();
        // 简单的 驼峰 转 下划线
        String sort = StrUtil.toUnderlineCase(params.getSort());

        // 除了 create_time 和 modifyTime 都过滤sql关键字
        if (!StrUtil.equalsAny(params.getSort(), SuperEntity.CREATE_TIME, SuperEntity.MODIFY_TIME)) {
            sort = AntiSqlFilter.getSafeValue(sort);
        }
        orders.add("ascending".equals(params.getOrder()) ? OrderItem.asc(sort) : OrderItem.desc(sort));
        page.setOrders(orders);
        return page;

    }

}
