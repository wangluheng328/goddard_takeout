package com.wes.goddard.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wes.goddard.dto.DishDto;
import com.wes.goddard.entity.Dish;
import com.wes.goddard.entity.DishFlavor;
import com.wes.goddard.mapper.DishMapper;
import com.wes.goddard.service.DishFlavorService;
import com.wes.goddard.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    /**
     * Add new dish with flavors data
     *
     * @param dishDto
     */
    @Transactional // Since we are dealing with two tables, we need to ensure consistency
    public void saveWithFlavor(DishDto dishDto) {
        //save basic info to table
        this.save(dishDto);
        Long dishId = dishDto.getId();// dish id
        // dish flavor
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //save flavors to dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * Query dish info and flavor info based on id
     *
     * This method is for 回显 when trying to edit
     *
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id) {
        // Query basic info
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        // Query flavor info from dish_flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // Update basic info in Dish table
        this.updateById(dishDto);

        // Remove the corresponding flavor record in dish_flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        // insert new flavor data into the dish_flavor table
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }


    // wzjxd
    @Override
    @Transactional
    public void updateStatus(List<Long> ids, Integer status) {
        for (Long id : ids) {
            DishDto dishDto = dishService.getByIdWithFlavor(id);
            dishDto.setStatus(status);
            this.updateById(dishDto);
        }
    }

    // wzjxd
    @Override
    @Transactional
    public void removeWithFlavor(List<Long> ids) {


        /**
         *
         * Since the input is a list of ids (long type), we need to get the corresponding DishDto types and store them in a list too.
         *
         * Note that the following logic might not be optimized, for instance the dishDtoList might not be necessary. We might be
         * able to use the ids directly. However, for my own understanding, I dishDtoList the new list.
         */
        List<DishDto> dishDtoList = new ArrayList<>();
        for (Long id : ids) {
            dishDtoList.add(dishService.getByIdWithFlavor(id));
        }

        for (DishDto dishDto : dishDtoList) {
            this.removeById(dishDto.getId());
        }

        for (DishDto dishDto : dishDtoList) {

            // Remove the corresponding flavor record in dish_flavor
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

            dishFlavorService.remove(queryWrapper);
        }


    }

}