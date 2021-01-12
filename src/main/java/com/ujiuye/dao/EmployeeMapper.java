package com.ujiuye.dao;

import com.ujiuye.bean.Employee;
import com.ujiuye.bean.EmployeeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EmployeeMapper {
    long countByExample(EmployeeExample example);

    int deleteByExample(EmployeeExample example);

    int deleteByPrimaryKey(Integer eid);

    int insert(Employee record);

    int insertSelective(Employee record);

    List<Employee> selectByExample(EmployeeExample example);

    Employee selectByPrimaryKey(Integer eid);

    int updateByExampleSelective(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByExample(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);

    //根据工号和密码获取员工信息
    Employee findEmployeeByJobnumberAndPassword(String jobnumber, String password);

    //根据条件获取员工表数据
    public List<Employee> findEmployeeByCondition(Employee employee);

    //根据条件获取员工表数据总条数
    public int totalRowsEmployeeByCondition(Employee employee);
}