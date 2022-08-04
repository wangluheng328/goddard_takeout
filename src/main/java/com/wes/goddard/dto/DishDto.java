package com.wes.goddard.dto;

import com.wes.goddard.entity.Dish;
import com.wes.goddard.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * DTO: Data Transfer Object
 * Entity: one entity matches one table in DB
 *
 *
 * This class is to encapsulate the dish data returned from frontend, since we cannot use the original dish class as there
 * is not attribute "flavors". So we have this class which extends Dish.
 */

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
