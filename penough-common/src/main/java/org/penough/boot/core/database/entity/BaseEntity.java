package org.penough.boot.core.database.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础父级实体类
 * 包含id, cu, ct
 *
 * @author Penough
 * @date 2020/10/16
 */
@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class BaseEntity implements Serializable {
    public static final String ID = "id";
    public final static String CREATE_USER = "createUser";
    public final static String CREATE_USER_COLUMN = "create_user";
    public final static String CREATE_TIME = "createTime";
    public final static String CREATE_TIME_COLUMN = "create_time";

    @ApiModelProperty("主键")
    @NotNull(message = "主键不可为空", groups = Update.class)
    @TableId(value = ID)
    protected Long id;

    @ApiModelProperty("创建时间")
    @TableField(value = CREATE_TIME_COLUMN, fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @ApiModelProperty("创建用户")
    @TableField(value = CREATE_USER_COLUMN, fill = FieldFill.INSERT)
    protected String createUser;

    /**
     * 保存验证组
     */
    public interface Save extends Default {

    }

    /**
     * 更新验证组
     */
    public interface Update extends Default {

    }
}
