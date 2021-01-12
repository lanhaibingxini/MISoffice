package com.ujiuye.dao;

import com.ujiuye.bean.Efunction;
import com.ujiuye.bean.EfunctionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EfunctionMapper {
    long countByExample(EfunctionExample example);

    int deleteByExample(EfunctionExample example);

    int deleteByPrimaryKey(Integer fid);

    int insert(Efunction record);

    int insertSelective(Efunction record);

    List<Efunction> selectByExample(EfunctionExample example);

    Efunction selectByPrimaryKey(Integer fid);

    int updateByExampleSelective(@Param("record") Efunction record, @Param("example") EfunctionExample example);

    int updateByExample(@Param("record") Efunction record, @Param("example") EfunctionExample example);

    int updateByPrimaryKeySelective(Efunction record);

    int updateByPrimaryKey(Efunction record);

    //根据员工id获取这个员工的功能
    List<Efunction> getFunctionsByEid(int eid);

    //获取所有权限信息
    List<Efunction> findFunctionByCondition(Efunction efunction);

    //获取数据总条数
    int countFunction(Efunction efunction);
}