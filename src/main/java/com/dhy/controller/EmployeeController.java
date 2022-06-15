package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dhy.common.R;
import com.dhy.entity.Employee;
import com.dhy.service.EmployeeService;
import com.dhy.utils.JwtUtils;
import com.dhy.utils.Regex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

  @Autowired
  private EmployeeService employeeService;

  /**
   * 用户登录
   *
   * @param employee
   * @return
   */
  @PostMapping("/login")
  public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request) {
    if (Regex.IsCheckout(employee.getUsername(), "^[0-9a-z]{6,8}$") && Regex.IsCheckout(employee.getPassword(), "^[0-9a-z]{6,8}$")) {
      return R.error("用户信息校验失败");
    }
    String password = employee.getPassword();
    HashMap<String, String> map = new HashMap<>();
    HashMap<String, Object> data = new HashMap<>();
    LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
    password = DigestUtils.md5DigestAsHex(password.getBytes());
    queryWrapper.eq(Employee::getUsername, employee.getUsername());
    Employee emp = employeeService.getOne(queryWrapper);
    if (emp == null) return R.error("用户不存在");
    if (!emp.getPassword().equals(password)) return R.error("密码错误");
    if (emp.getStatus() != 1) return R.error("用户已被禁用");
    map.put("userId", emp.getId().toString());
    request.getSession().setAttribute("id", emp.getId());
    data.put("token", JwtUtils.token(map));
    data.put("username", emp.getUsername());
    return R.SuccessPlus(data, "登录成功");
  }

  /**
   * 添加员工信息
   * 用户默认密码123456
   *
   * @param employee
   * @return
   */
  @PostMapping
  public R<String> addEmployee(@RequestBody Employee employee, HttpServletRequest request) {
    employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
    employee.setCreateTime(LocalDateTime.now());
    employee.setUpdateTime(LocalDateTime.now());
    employee.setCreateUser((Long) request.getSession().getAttribute("id"));
    employee.setUpdateUser((Long) request.getSession().getAttribute("id"));

    employeeService.save(employee);
    return R.success(null, "添加成功");
  }

  /**
   * 分页部分
   */
  @GetMapping("/page")
  public R<Page> page(int page, int pageSize, String name) {
    // 分页构造器
    Page pageInfo = new Page(page, pageSize);
    LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.like(name != null, Employee::getName, name);
    queryWrapper.orderByDesc(Employee::getUpdateTime);
    employeeService.page(pageInfo, queryWrapper);
    return R.success(pageInfo, "获取成功");
  }

  /**
   * 修改员工信息
   *
   * @param employee
   * @return
   */
  @PutMapping
  public R<String> update(@RequestBody Employee employee) {
    LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Employee::getId, employee.getId());
    employeeService.update(employee, queryWrapper);
    return R.success(null, "员工信息修改成功");
  }
}
