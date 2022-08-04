package com.wes.goddard.service.impl;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.wes.goddard.common.CustomException;
import com.wes.goddard.common.CustomException;
import com.wes.goddard.entity.Category;
//import com.wes.goddard.entity.Dish;
//import com.wes.goddard.entity.Setmeal;
import com.wes.goddard.entity.Dish;
import com.wes.goddard.entity.Setmeal;
import com.wes.goddard.mapper.CategoryMapper;
import com.wes.goddard.service.CategoryService;
//import com.wes.goddard.service.DishService;
//import com.wes.goddard.service.SetmealService;
import com.wes.goddard.service.DishService;
import com.wes.goddard.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService{

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * Remove category based on ID, and check if there's binded dishes under that category.
     * @param id
     *
     *
     *
     *
     *
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // Query Condition
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);

        //Check if the category has dishes under it, if so, throw and custom exception
        if (count1 > 0) {
            throw new CustomException("Cannot Remove: There are dishes in this category");
        }

        //Check if the category has meals under it, if so, throw and custom exception
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            throw new CustomException("Cannot Remove: There are meals in this category");
        }

        // if the above exceptions were not triggered, delete normally
        super.removeById(id);
    }
}
