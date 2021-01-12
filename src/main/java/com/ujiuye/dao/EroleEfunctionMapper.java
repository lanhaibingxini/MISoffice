package com.ujiuye.dao;

import com.ujiuye.bean.Efunction;
import com.ujiuye.bean.EroleEfunction;
import com.ujiuye.bean.EroleEfunctionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EroleEfunctionMapper {
    long countByExample(EroleEfunctionExample example);

    int deleteByExample(EroleEfunctionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EroleEfunction record);

    int insertSelective(EroleEfunction record);

    List<EroleEfunction> selectByExample(EroleEfunctionExample example);

    EroleEfunction selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EroleEfunction record, @Param("example") EroleEfunctionExample example);

    int updateByExample(@Param("record") EroleEfunction record, @Param("example") EroleEfunctionExample example);

    int updateByPrimaryKeySelective(EroleEfunction record);

    int updateByPrimaryKey(EroleEfunction record);

    //根据角色id获取所有功能
    List<Efunction> findAllFunctionByRid(int rid);
}