package com.ujiuye.service;

import com.alibaba.fastjson.JSONArray;
import com.ujiuye.bean.Efunction;
import com.ujiuye.utils.BaseResult;
import org.springframework.stereotype.Service;

import java.util.List;

public interface FunctionService {
    //根据eid查询这个用户拥有的所有功能
    public List<Efunction> getFunctionsByEid(int eid);

    //将接收到的所有功能转换成JSONArray格式
    public JSONArray functions2Json(List<Efunction> efunctionList);

    //使用递归的方式实现
    public JSONArray functions2Json2(List<Efunction> efunctionList, int fparentid, List<Efunction> roleFunctions);

    //模糊查询
    List<Efunction> findFunctionByCondition(Efunction efunction);

    //数据总条数
    int countFunction(Efunction efunction);

    //获取一级功能
    List<Efunction> getFirstFunction();

    //新增功能
    BaseResult addFunction(Efunction efunction);

    //根据fid查询需要修改的功能信息
    Efunction findFunctionByFid(int fid);

    //修改功能
    BaseResult updateFunction(Efunction efunction);

    //根据fid删除权限
    BaseResult deleteFunctionByFid(int fid);

    //批量删除
    BaseResult batchDelete(String fids);

    //根据父级功能id获取子功能信息
    List<Efunction> getFunctionByFparentid(int fparentid);

    //获取所有功能
    List<Efunction> findAllFunction();

    //根据角色id获取所有功能
    List<Efunction> findAllFunctionByRid(int rid);

    //将系统所有功能和该角色的所有功能转换成JSONArray格式
    JSONArray convertRolesFunction(List<Efunction> allFunctions, List<Efunction> roleFunctions);
}
