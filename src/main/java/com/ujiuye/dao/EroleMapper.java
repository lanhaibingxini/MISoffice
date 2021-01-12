package com.ujiuye.dao;

import com.ujiuye.bean.Erole;
import com.ujiuye.bean.EroleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EroleMapper {
    long countByExample(EroleExample example);

    int deleteByExample(EroleExample example);

    int deleteByPrimaryKey(Integer rid);

    int insert(Erole record);

    int insertSelective(Erole record);

    List<Erole> selectByExample(EroleExample example);

    Erole selectByPrimaryKey(Integer rid);

    int updateByExampleSelective(@Param("record") Erole record, @Param("example") EroleExample example);

    int updateByExample(@Param("record") Erole record, @Param("example") EroleExample example);

    int updateByPrimaryKeySelective(Erole record);

    int updateByPrimaryKey(Erole record);

    //根据员工id获取角色信息
    List<Erole> findRolesByEid(int eid);

    //根据条件分页获取列表数据
    public List<Erole> getRoleByCondition(Erole erole);

    //根据条件获取数据总条数
    public int countRowsByCondition(Erole erole);
}