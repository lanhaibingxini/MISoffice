package com.ujiuye.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ujiuye.bean.Efunction;
import com.ujiuye.bean.Employee;
import com.ujiuye.bean.Erole;
import com.ujiuye.service.FunctionService;
import com.ujiuye.utils.BaseResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("function")
public class FunctionController {
    @Autowired
    private FunctionService functionService;

    @RequestMapping("getCurrentFunctions")
    @ResponseBody
    public JSONArray getCurrentFunctions(){
        //使用shiro框架，就可以从securityUtils中的subject登录对象中获取session
        Session session = SecurityUtils.getSubject().getSession();
        //从session中获取当前登录的员工信息
        Employee employee = (Employee) session.getAttribute("employee");
        //根据员工信息获取这个员工所拥有的功能
        List<Efunction> efunctionList = functionService.getFunctionsByEid(employee.getEid());
        //将查询到的员工功能转换为JSONArray格式
        JSONArray jsonArray = functionService.functions2Json2(efunctionList, 0, null);
        return jsonArray;
    }

    @RequestMapping("getFunctionList")
    @ResponseBody
    public JSONObject getFunctionList(Efunction efunction){
        //因为easyui进行分页需要每一页的数据rows和总数据条数total这两个条件，所以需要定义获取这两个条件的方法
        //查询所有功能信息
        List<Efunction> efunctionList = functionService.findFunctionByCondition(efunction);
        //查询所有数据条数
        int total = functionService.countFunction(efunction);
        //将查询到的信息和数据条数封装到JSONObject当中
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", efunctionList);
        jsonObject.put("total", total);
        return jsonObject;
    }

    //获取一级功能
    @RequestMapping("getFirstFunction")
    @ResponseBody
    public List<Efunction> getFirstFunction(){
        List<Efunction> efunctionList = functionService.getFirstFunction();
        return efunctionList;
    }

    //新增功能
    @RequestMapping("addFunction")
    @ResponseBody
    public BaseResult addFunction(Efunction efunction){
        BaseResult result = functionService.addFunction(efunction);
        return result;
    }

    //获取需要修改的功能信息
    @RequestMapping("getUpdateFunction")
    @ResponseBody
    public BaseResult getUpdateFunction(int fid){
        BaseResult result = new BaseResult();
        Efunction efunction = functionService.findFunctionByFid(fid);
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("getUpdateFunction", efunction);
        result.setSuccess(true);
        return  result;
    }

    //获取session中需要修改的信息，展示到修改页面
    @RequestMapping("getUpdateFunction2")
    @ResponseBody
    public Efunction getUpdateFunction2(){
        Session session = SecurityUtils.getSubject().getSession();
        Efunction efunction = (Efunction) session.getAttribute("getUpdateFunction");
        return  efunction;
    }

    //修改功能
    @RequestMapping("updateFunction")
    @ResponseBody
    public BaseResult updateFunction(Efunction efunction){
        BaseResult result = functionService.updateFunction(efunction);
        return result;

    }

    //删除方法
    @RequestMapping("deleteFunctionByFid")
    @ResponseBody
    public BaseResult deleteFunctionByFid(int fid){
        BaseResult result = functionService.deleteFunctionByFid(fid);
        return  result;
    }

    //批量删除
    @RequestMapping("batchDelete")
    @ResponseBody
    public BaseResult batchDelete(String fids){
        BaseResult result = functionService.batchDelete(fids);
        return  result;
    }

    //根据父级功能id获取子功能信息
    @RequestMapping("getFunctionByFparentid")
    @ResponseBody
    public List<Efunction> getFunctionByFparentid(int fparentid){
        List<Efunction> efunctionList = functionService.getFunctionByFparentid(fparentid);
        return efunctionList;
    }

    //向编辑角色页面展示所有功能
    @RequestMapping("getFunctionOfRole")
    @ResponseBody
    public JSONArray getFunctionOfRole(){
        //从session获取该角色信息
        Session session = SecurityUtils.getSubject().getSession();
        Erole erole = (Erole) session.getAttribute("updataRole");
        //获取系统所有功能
        List<Efunction> allFunctions = functionService.findAllFunction();
        //获取当前角色的所有功能
        List<Efunction> rolefunctions = functionService.findAllFunctionByRid(erole.getRid());
        JSONArray jsonArray =  functionService.functions2Json2(allFunctions, 0, rolefunctions);
        return jsonArray;
    }

}
