package com.ujiuye.service;

import com.ujiuye.bean.Efunction;
import com.ujiuye.bean.Employee;
import com.ujiuye.bean.EmployeeErole;
import com.ujiuye.utils.BaseResult;
import com.ujiuye.utils.EmployeeResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public interface EmployeeService {

    //普通登录校验
    public EmployeeResult login(String jobnumber, String password);

    //shiro登录校验
    public EmployeeResult shiroLogin(String jobnumber, String password);

    //根据条件获取员工表数据
    public List<Employee> findEmployeeByCondition(Employee employee);

    //根据条件获取员工表数据总条数
    public int totalRowsEmployeeByCondition(Employee employee);

    //根据eid单条删除员工信息
    public BaseResult deleteEmployeeByEid(int eid);

    //新增员工
    public BaseResult addEmployee(Employee employee);

    //批量删除
    public BaseResult batchDeleteByEids(String eids);

    //根据eid查询员工信息
    public Employee getUpdateEmployeeByEid(int eid);

    //根据eid获取该员工的员工角色信息
    public Employee findEmployeeRoleByEid(int eid);

    //修改员工信息
    public BaseResult updateEmployee(Employee employee);
}
