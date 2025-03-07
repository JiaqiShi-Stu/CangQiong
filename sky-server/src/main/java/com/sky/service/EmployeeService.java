package com.sky.service;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);
    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return
     */
    void register(EmployeeDTO employeeDTO);


    PageResult pageSearch(EmployeePageQueryDTO employpageQueryDTO);

    void changeStatus(Integer status,Long userId);

    Employee getbyId(Long id);

    void update(EmployeeDTO employeeDTO);
}
