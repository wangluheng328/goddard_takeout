package com.wes.goddard.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wes.goddard.dto.DishDto;
import com.wes.goddard.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    // Add new dish, meanwhile insert the corresponding flavors. Need to manage two tables: dish, dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

    public void updateStatus(List<Long> ids, Integer status);

    public void removeWithFlavor(List<Long> ids0);

}
