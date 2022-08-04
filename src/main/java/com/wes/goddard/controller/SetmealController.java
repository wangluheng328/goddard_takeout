package com.wes.goddard.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wes.goddard.common.R;
import com.wes.goddard.dto.DishDto;
import com.wes.goddard.dto.SetmealDto;
import com.wes.goddard.entity.Category;
import com.wes.goddard.entity.Setmeal;
import com.wes.goddard.service.CategoryService;
import com.wes.goddard.service.SetmealDishService;
import com.wes.goddard.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Dish Controller
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;


    /**
     * Add Meal
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("Meal info：{}",setmealDto);

        setmealService.saveWithDish(setmealDto);

        return R.success("Add meal success");
    }


    /**
     * Query setmeal info and meal-dish info
     *
     * This method is for echo back the data when visit the edit page
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){

        SetmealDto setmealDto = setmealService.getByIdWithDish(id);

        return R.success(setmealDto);
    }


    // wzjxd
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        log.info(setmealDto.toString());

        setmealService.updateWithDish(setmealDto);

        return R.success("Update Success");
    }

    @PutMapping("/status/{sta}")
    public R<String> updateStatus(@PathVariable Long sta, @RequestParam List<Long> ids, @RequestParam Integer status){
//        log.info("ids:{}",);

        setmealService.updateStatus(ids, status);

        return R.success("Update Success");
    }


    /**
     * meal pageinated query
     *
     * Without this method, the returned category is 1 or 2. I use this method to get the names by using categoryService to retrieve
     * the name based on Id and then insert this name
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // Condition: use like to perform query, based on input name
        queryWrapper.like(name != null,Setmeal::getName,name);
        // Ordering
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo,queryWrapper);

        // Copy object
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {

            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(item,setmealDto);

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    // wzjxd
    /**
     * Remove meals
     *
     * This method serves to both single remove and batch remove
     * The logic is: the only difference in what's sent from the frontend to the backend between single and batch remove
     * is the number of id there is in the attribute ids. Therefore, we can use a single function with an input of a list,
     * and remove by looping through the list
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);

//        for (Long id : ids)
//        {
//            dishService.removeById(id);
//        }
        setmealService.removeWithDish(ids);

        return R.success("Remove Dish Success");
    }



//    @GetMapping("/list")
//    public R<List<Setmeal>> list(Setmeal setmeal) {
//
//        log.info("setmeal:{}", setmeal);
//
//        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.like(StringUtils.isNotEmpty(setmeal.getName()), Setmeal::getName, setmeal.getName());
//        queryWrapper.eq(null != setmeal.getCategoryId(), Setmeal::getCategoryId, setmeal.getCategoryId());
//        queryWrapper.eq(null != setmeal.getStatus(), Setmeal::getStatus, setmeal.getStatus());
//        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
//
//        return R.success(setmealService.list(queryWrapper));
//    }



}
