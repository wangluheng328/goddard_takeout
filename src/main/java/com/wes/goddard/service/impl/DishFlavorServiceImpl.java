package com.wes.goddard.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wes.goddard.dto.DishDto;
import com.wes.goddard.entity.DishFlavor;
import com.wes.goddard.mapper.DishFlavorMapper;
import com.wes.goddard.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper,DishFlavor> implements DishFlavorService {


}
