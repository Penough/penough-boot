package org.penough.boot.database.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

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
public class SuperEntity extends BaseEntity {
    public final static String MODIFY_TIME = "modifyTime";
    public final static String MODIFY_TIME_COLUMN = "modify_time";
    public final static String MODIFY_USER = "modifyUser";
    public final static String MODIFY_USER_COLUMN = "modify_user";

    @ApiModelProperty("修改时间")
    @TableField(value = MODIFY_TIME_COLUMN, fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime modifyTime;

    @ApiModelProperty("修改用户")
    @TableField(value = MODIFY_USER_COLUMN, fill = FieldFill.INSERT_UPDATE)
    protected String modifyUser;


}

