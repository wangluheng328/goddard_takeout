package com.wes.goddard.dto;

import com.wes.goddard.entity.Setmeal;
import com.wes.goddard.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;


}
