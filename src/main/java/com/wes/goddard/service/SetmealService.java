package com.wes.goddard.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wes.goddard.dto.SetmealDto;
import com.wes.goddard.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);

    public SetmealDto getByIdWithDish(Long id);

    public void updateWithDish(SetmealDto setmealDto);

    public void updateStatus(List<Long> ids, Integer status);
}
