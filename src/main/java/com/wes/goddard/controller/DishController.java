package com.wes.goddard.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wes.goddard.common.R;
import com.wes.goddard.dto.DishDto;
import com.wes.goddard.entity.Category;
import com.wes.goddard.entity.Dish;
import com.wes.goddard.service.CategoryService;
import com.wes.goddard.service.DishFlavorService;
import com.wes.goddard.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Dish Controller
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * Add new dish
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("Add new dish success");
    }

    /**
     * Dish paginated query
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(name != null,Dish::getName,name);

        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,queryWrapper);


        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        /**
         * records is a list of Dish. However, we need to return a DishDto object to the frontend. So we need to do the following
         * to encapsulate the category names
         */

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * Query dish info and dish-flavor info
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    /**
     * Update
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        return R.success("Update Success");
    }

    // wzjxd
    /**
     * Switch Status
     *
     * In EmployeeController, the input for update is @RequestBody Employee employee, as the data from frontend is 1 id and 1 status
     * However, here the returned value is multiple ids but still 1 status, so that I split the input into literally id and status
     * Then, in the service Impl, i looped through the ids, retrieve dishDto for each id, set the status attribute to the inputted status,
     * and then finally update
     *
     * Things to consider: is it doable if I write the input still as DishDto or List<DishDto> here? Basically, how does it recognize the input
     * as an object with only two attributes?
     *
     */
    @PutMapping("/status/{sta}")
    public R<String> updateStatus(@PathVariable Long sta, @RequestParam List<Long> id, @RequestParam Integer status){
//        log.info("ids:{}",);

        dishService.updateStatus(id, status);

        return R.success("Update Success");
    }


    // wzjxd
    /**
     * Remove dish
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

        /**
         * Here I don't use what's commented out below because that will only delete the records in the Dish table.
         * The purpose of removeWithFlavor method is to delete the corresponding records in dish_flavor table as well.
         */
//        for (Long id : ids)
//        {
//            dishService.removeById(id);
//        }
        dishService.removeWithFlavor(ids);

        return R.success("Remove Dish Success");
    }

    /**
     * Query dishes based on conditions
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        // Condition: dishes need to be selling, i.e. status = 1
        queryWrapper.eq(Dish::getStatus,1);

        // Ordering
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);
    }

}
