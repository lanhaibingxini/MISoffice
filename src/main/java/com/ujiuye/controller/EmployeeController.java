package com.ujiuye.controller;

import com.alibaba.fastjson.JSONObject;
import com.ujiuye.bean.Employee;
import com.ujiuye.service.EmployeeService;
import com.ujiuye.service.EmployeeServiceImpl;
import com.ujiuye.utils.BaseResult;
import com.ujiuye.utils.EmployeeResult;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    //使用session内置对象存放登录成功的对象信息
    @RequestMapping("login")
    @ResponseBody
    public EmployeeResult login(String jobnumber, String password){
//        EmployeeResult result = employeeService.login(jobnumber, password);
//        //如果登录成功
//        if(result.isLoginSuccess()){
//            //就将登录成功的员工信息存入session内置对象中
//            session.setAttribute("employee", result.getEmployee());
//        }

        //shiro登录
        EmployeeResult result = employeeService.shiroLogin(jobnumber, password);
        return result;
    }

    //获取当前登录对象
    @RequestMapping("getCurrentEmployee")
    @ResponseBody
    public Employee getCurrentEmployee(){
        Session session = SecurityUtils.getSubject().getSession();
        Employee employee = (Employee) session.getAttribute("employee");
        return employee;
    }

    //分页查询
    @RequestMapping("getEmployeeList")
    @ResponseBody
    public JSONObject getEmployeeList(Employee employee){
        //获取员工表数据
        List<Employee> employeeList = employeeService.findEmployeeByCondition(employee);
        //获取员工表数据总条数
        int total = employeeService.totalRowsEmployeeByCondition(employee);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", employeeList);
        jsonObject.put("total", total);
        return jsonObject;
    }

    //单条删除
    @RequestMapping("deleteEmployeeByEid")
    @ResponseBody
    public BaseResult deleteEmployeeByEid(int eid){
        return employeeService.deleteEmployeeByEid(eid);
    }

    //新增员工
    @RequestMapping("addEmployee")
    @ResponseBody
    public BaseResult addEmployee(Employee employee){
        return employeeService.addEmployee(employee);
    }

    //批量删除
    @RequestMapping("batchDelete")
    @ResponseBody
    public BaseResult batchDelete(String eids){
        return employeeService.batchDeleteByEids(eids);
    }

    //将需要修改的员工信息存入session
    @RequestMapping("getUpdateEmployee")
    @ResponseBody
    public BaseResult getUpdateEmployee(int eid){
        BaseResult result = new BaseResult();
        Session session = SecurityUtils.getSubject().getSession();
        Employee employee = employeeService.findEmployeeRoleByEid(eid);
        session.setAttribute("updateEmployee", employee);
        result.setSuccess(true);
        return result;
    }

    //将需要需要修改的员工信息从session中取出，展示到修改页面上
    @RequestMapping("getUpdateEmployee2")
    @ResponseBody
    public Employee getUpdateEmployee2(){
        Session session = SecurityUtils.getSubject().getSession();
        Employee employee = (Employee) session.getAttribute("updateEmployee");
        return employee;
    }

    //修改员工信息
    @RequestMapping("updateEmployee")
    @ResponseBody
    public BaseResult updateEmployee(Employee employee){
        return employeeService.updateEmployee(employee);
    }

    //导出
    @RequestMapping("exportEmployee")
    public ResponseEntity<byte[]> exportEmployee(Employee employee) throws IOException {
        String filename = "employee.xls";
        //用于创建xls格式的文档
        Workbook workbook = new HSSFWorkbook();
        //创建sheet
        Sheet sheet = workbook.createSheet();
        //创建第一行数据
        Row title = sheet.createRow(0);
        //从索引0开始，代表第一行，第一列
        title.createCell(0).setCellValue("员工id");
        title.createCell(1).setCellValue("员工姓名");
        title.createCell(2).setCellValue("员工性别");
        title.createCell(3).setCellValue("员工年龄");
        title.createCell(4).setCellValue("员工工号");
        title.createCell(5).setCellValue("入职日期");
        title.createCell(6).setCellValue("联系电话");

        employee.setPage(1);
        employee.setRows(999);
        List<Employee> employeeList = employeeService.findEmployeeByCondition(employee);
        for (Employee employee1 : employeeList){
            //创建下一行数据
            Row nextRow = sheet.createRow(sheet.getLastRowNum() + 1);
            nextRow.createCell(0).setCellValue(employee1.getEid());
            nextRow.createCell(1).setCellValue(employee1.getEname());
            if (employee1.getEsex() == 0){
                nextRow.createCell(2).setCellValue("男");
            }else {
                nextRow.createCell(2).setCellValue("女");
            }
            nextRow.createCell(3).setCellValue(employee1.getEage());
            nextRow.createCell(4).setCellValue(employee1.getJobnumber());
            nextRow.createCell(5).setCellValue(employee1.getHireDatestr());
            nextRow.createCell(6).setCellValue(employee1.getPhone());
        }
        //字节数组输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //将输出流写入xls文档
        workbook.write(byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        HttpHeaders httpHeaders = new HttpHeaders();
        //指定响应格式
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //指定浏览器的打开方式
        httpHeaders.setContentDispositionFormData("attachment", filename);
        return new ResponseEntity<byte[]>(bytes, httpHeaders, HttpStatus.OK);
    }
}
