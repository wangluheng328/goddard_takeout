package com.wes.goddard.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Categories
 */
// @Data gives lots of common POJO methods such as toString and getters/setters
@Data
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //Type: 1 Dish Category; 2 Meal Category
    private Integer type;


    //Category name
    private String name;


    //Order (as shwon in the mobile end)
    private Integer sort;


    //Create time
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    //Update time
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    //Created by?
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    //Updated by?
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
