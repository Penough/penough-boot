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
 * 数据表父级实体类
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
public class SuperEntity implements Serializable {
    public static final String ID = "id";
    public final static String CREATE_TIME = "createTime";
    public final static String CREATE_TIME_COLUMN = "create_time";
    public final static String MODIFY_TIME = "modifyTime";
    public final static String MODIFY_TIME_COLUMN = "modify_time";
    public final static String CREATE_USER = "createUser";
    public final static String CREATE_USER_COLUMN = "create_user";
    public final static String MODIFY_USER = "modifyUser";
    public final static String MODIFY_USER_COLUMN = "modify_user";

    @ApiModelProperty("主键")
    @NotNull(message = "主键不可为空", groups = Update.class)
    @TableId(value = ID)
    protected Long id;

    @ApiModelProperty("创建时间")
    @TableField(value = CREATE_TIME_COLUMN, fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(value = MODIFY_TIME_COLUMN, fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime modifyTime;

    @ApiModelProperty("创建用户")
    @TableField(value = CREATE_USER_COLUMN, fill = FieldFill.INSERT)
    protected String createUser;

    @ApiModelProperty("修改用户")
    @TableField(value = MODIFY_USER_COLUMN, fill = FieldFill.INSERT_UPDATE)
    protected String modifyUser;

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

