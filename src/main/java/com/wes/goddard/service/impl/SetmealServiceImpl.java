package com.wes.goddard.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wes.goddard.common.CustomException;
import com.wes.goddard.dto.DishDto;
import com.wes.goddard.dto.SetmealDto;
import com.wes.goddard.entity.Dish;
import com.wes.goddard.entity.DishFlavor;
import com.wes.goddard.entity.Setmeal;
import com.wes.goddard.entity.SetmealDish;
import com.wes.goddard.mapper.SetmealMapper;
import com.wes.goddard.service.SetmealDishService;
import com.wes.goddard.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * Add meal, and also add meal -> dish relationships (in setmeal_dish table)
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);


        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish s : setmealDishes) {
            s.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDishes);

    }


    /**
     * Query dish info and flavor info based on id
     *
     * This method is for 回显 when trying to edit
     *
     * @param id
     * @return
     */
    public SetmealDto getByIdWithDish(Long id) {
        // Query basic info
        Setmeal setmeal = this.getById(id);

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        // Query dish info from setmeal_dish
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> dishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(dishes);

        return setmealDto;
    }


    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        // Update basic info
        this.updateById(setmealDto);

        // Remove the corresponding meal-dish record in setmeal_dish
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());

        setmealDishService.remove(queryWrapper);

        // insert new dish data into the setmeal_dish table
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();

        dishes = dishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(dishes);
    }

    // wzjxd
    @Override
    @Transactional
    public void updateStatus(List<Long> ids, Integer status) {
        for (Long id : ids) {
            SetmealDto setmealDto = this.getByIdWithDish(id);
            setmealDto.setStatus(status);
            this.updateById(setmealDto);
        }
    }


    /**
     * Remove dish and corresponding records in setmeal_dish table
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in (1,2,3) and status = 1
        // check status, and hence whether it can be removed
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if(count > 0){
            // if cannot be removed, throw exception
            throw new CustomException("Currently selling, can't remove. Please freeze first");
        }

        this.removeByIds(ids);

        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);

        setmealDishService.remove(lambdaQueryWrapper);
    }
}
