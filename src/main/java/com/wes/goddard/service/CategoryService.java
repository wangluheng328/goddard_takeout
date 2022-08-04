package com.wes.goddard.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wes.goddard.entity.Category;

public interface CategoryService extends IService<Category> {

    public void remove(Long id);

}
