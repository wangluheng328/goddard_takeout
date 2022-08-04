package com.wes.goddard.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Employee Entity
 * by default, mybatis plus treat class name as table name
 */
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;//real-world ID

    private Integer status;

    // INSERT: Fill when create/insert, such as create a new employee
    // INSERT_UPDATE: Fill when create/insert AND update
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /*
    * @TableField() parameters explanation:
    *   - value = *custom col name*
    *   - exist = *true/false* whether the attribute is a column in the table.
    *                For example, it could be a simple string attribute for the business logic
    *                instead of a column in the table
    */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
