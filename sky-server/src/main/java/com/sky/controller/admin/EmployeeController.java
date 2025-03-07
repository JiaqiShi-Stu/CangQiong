package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "Employee Controller")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    @ApiOperation(value = "新增员工")
    public Result<String> register(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工：{}", employeeDTO);
        employeeService.register(employeeDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation(value = "员工分页查询")
    public Result<PageResult> pageSearch(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询，查询参数为：{}", employeePageQueryDTO);
        PageResult pageresult = employeeService.pageSearch(employeePageQueryDTO);
        return Result.success(pageresult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation(value = "员工启用禁用")
    public Result OnandOff(@PathVariable Integer status,Long id){
        log.info("当前员工将状态为：{} {}", status,id);
        employeeService.changeStatus(status,id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "员工查询")
    public Result EmpSearch(@PathVariable Long id){
        log.info("当前员工id：{} {}", id);
        Employee employee = employeeService.getbyId(id);
        return Result.success(employee);
    }

    @PutMapping()
    @ApiOperation(value = "员工修改")
    public Result OnandOff(@RequestBody EmployeeDTO employeeDTO) {
        log.info("当前员工信息 {}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }



}
