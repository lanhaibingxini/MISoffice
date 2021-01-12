package com.ujiuye.dao;

import com.ujiuye.bean.Employee;
import com.ujiuye.bean.EmployeeErole;
import com.ujiuye.bean.EmployeeEroleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EmployeeEroleMapper {
    long countByExample(EmployeeEroleExample example);

    int deleteByExample(EmployeeEroleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EmployeeErole record);

    int insertSelective(EmployeeErole record);

    List<EmployeeErole> selectByExample(EmployeeEroleExample example);

    EmployeeErole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EmployeeErole record, @Param("example") EmployeeEroleExample example);

    int updateByExample(@Param("record") EmployeeErole record, @Param("example") EmployeeEroleExample example);

    int updateByPrimaryKeySelective(EmployeeErole record);

    int updateByPrimaryKey(EmployeeErole record);

    //根据eid获取该员工的员工角色信息
    Employee findEmployeeRoleByEid(int eid);
}