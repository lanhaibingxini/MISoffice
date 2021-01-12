package com.ujiuye.service;

import com.ujiuye.bean.Erole;
import com.ujiuye.utils.BaseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public interface RoleService {

    //根据条件分页获取列表数据
    public List<Erole> getRoleByCondition(Erole erole);

    //根据条件获取数据总条数
    public int countRowsByCondition(Erole erole);

    //新增
    public BaseResult addRole(Erole erole);

    //根据rid删除
    public BaseResult deleteRoleByrid(int rid);

    //根据rids批量删除
    public BaseResult batchDeleteByRids(String rids);

    //根据rids获取全部角色
    public List<Erole> getAllRoles();

    //根据rid将选中的角色信息存入session中
    public Erole getUpdateRoleByRid(int rid);

    //根据fid是修改员工页面
    public BaseResult updateRole(Erole erole);

}
