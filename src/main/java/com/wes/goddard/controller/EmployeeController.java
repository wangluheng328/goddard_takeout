package com.wes.goddard.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wes.goddard.common.R;
import com.wes.goddard.entity.Employee;
import com.wes.goddard.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * employee login
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){

        //1. Encrypt the password submitted from the webpage with md5
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2. Query the database for the submitted username
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3. If username not found, return login fail
        if(emp == null){
            return R.error("Login Failed");
        }

        //4. Compare password, if not equal, return login fail
        if(!emp.getPassword().equals(password)){
            return R.error("Login Failed");
        }

        //5. Check employee status, if disabled, return disabled
        if(emp.getStatus() == 0){
            return R.error("Account Disabled");
        }

        //6. Login success, save the employee's ID to Session, and return login success
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * employee logout
     * @param request
     * @return
     *
     *
     * when logged in, there will be a record in the session/local storage if press F12 on the webpage
     * when logged out, that record will be removed (might need to hit refresh)
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //clear the
        request.getSession().removeAttribute("employee");
        return R.success("Logout Success");
    }

    /**
     * Add employee
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("Add employee，new employee info：{}",employee.toString());

        //set initial default password 123456，and use md5 to encrpyt this initial password
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        //get current user (creater) id
//        Long empId = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("Add new employee success");
    }

    /**
     * Query employee info paginated
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);

        Page pageInfo = new Page(page,pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);

        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * Edit Employee info based on ID
     *
     * Such as disabling/enabling accounts
     *
     * Workflow:
     *  1. when clicking disabling/enabling, it triggers statusHandle method
     *  2. in statusHandle, it starts ajax request via enableOrDisable, sending id and status (1 if currently 0, vice versa)
     *  3. controller receiving the data as encapsulated, and use MybatisPlus method to update
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());

//        Long empId = (Long)request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);

        return R.success("User update Success");
    }

    /**
     * Query User info based on ID
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("Querying employee info with ID ...");
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("Employee not found");
    }
}
