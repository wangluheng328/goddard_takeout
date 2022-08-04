package com.wes.goddard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wes.goddard.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee>{
}
