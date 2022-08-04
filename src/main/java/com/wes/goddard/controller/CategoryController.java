package com.wes.goddard.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wes.goddard.common.R;
import com.wes.goddard.entity.Category;
import com.wes.goddard.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Category Controller
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * Add new category
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("Add new category success");
    }

    /**
     * Paginated query
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){

        Page<Category> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);

        //Query
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * Delete category based in ID
     * @param id
     * @return
     */
    // Don't forget to put @RequestParam(name of the data in frontend) when the frontend name and variable name in controller is diff
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long id){
        log.info("Delete category，id：{}",id);

//        categoryService.removeById(id);
        categoryService.remove(id);

        return R.success("Category removed successfully");
    }

    /**
     * Edit category by ID
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("Edit category info：{}",category);

        categoryService.updateById(category);

        return R.success("Edit Success");
    }

    /**
     * Query based on condition
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        // Create query condition wrapper
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // Create query condition
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        // Create sorting condition
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
