package com.ujiuye.controller;

import com.alibaba.fastjson.JSONObject;
import com.ujiuye.bean.Erole;
import com.ujiuye.service.RoleService;
import com.ujiuye.utils.BaseResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    //分页查询
    @RequestMapping("getRoleList")
    @ResponseBody
    public JSONObject getRoleList(Erole erole){
        List<Erole> eroleList = roleService.getRoleByCondition(erole);
        int total = roleService.countRowsByCondition(erole);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", eroleList);
        jsonObject.put("total", total);
        return jsonObject;
    }

    //新增方法
    @RequestMapping("addRole")
    @ResponseBody
    public BaseResult addRole(Erole erole){
        return roleService.addRole(erole);
    }

    //单条删除
    @RequestMapping("deleteRoleByrid")
    @ResponseBody
    public BaseResult deleteRoleByrid(int rid){
        return roleService.deleteRoleByrid(rid);
    }

    //批量删除
    @RequestMapping("batchDelete")
    @ResponseBody
    public BaseResult batchDelete(String rids){
        return roleService.batchDeleteByRids(rids);
    }

    //根据rids获取全部角色
    @RequestMapping("getAllRoles")
    @ResponseBody
    public List<Erole> getAllRoles(){
        return roleService.getAllRoles();
    }

    //根据rid将选中的角色信息存入session中
    @RequestMapping("getUpdateRole")
    @ResponseBody
    public BaseResult getUpdateRole(int rid){
        BaseResult result = new BaseResult();
        Erole erole = roleService.getUpdateRoleByRid(rid);
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("updataRole", erole);
        result.setSuccess(true);
        return result;
    }

    //从session当中取出存入好的数据，展示在页面
    @RequestMapping("getUpdateRole2")
    @ResponseBody
    public Erole getUpdateRole2(){
        Session session = SecurityUtils.getSubject().getSession();
        Erole erole = (Erole) session.getAttribute("updataRole");
        return erole;
    }

    //修改员工页面
    @RequestMapping("updateRole")
    @ResponseBody
    public BaseResult updateRole(Erole erole){
        return roleService.updateRole(erole);
    }
}
