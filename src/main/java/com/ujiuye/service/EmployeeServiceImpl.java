package com.ujiuye.service;

import com.ujiuye.bean.Employee;
import com.ujiuye.bean.EmployeeErole;
import com.ujiuye.bean.EmployeeEroleExample;
import com.ujiuye.bean.EmployeeExample;
import com.ujiuye.dao.EmployeeEroleMapper;
import com.ujiuye.dao.EmployeeMapper;
import com.ujiuye.utils.BaseResult;
import com.ujiuye.utils.EmployeeResult;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static Logger logger = Logger.getLogger(EmployeeServiceImpl.class);
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private EmployeeEroleMapper employeeEroleMapper;

    @Override
    public EmployeeResult login(String jobnumber, String password){
        logger.info("开启登录校验，传入参数，工号：" + jobnumber + "密码：" + password);
        EmployeeResult result = new EmployeeResult();
        try {
            Employee employee = employeeMapper.findEmployeeByJobnumberAndPassword(jobnumber, password);
            if (employee != null){
                //获取到登录成功的员工信息，存入结果
                result.setEmployee(employee);
                result.setLoginSuccess(true);
                result.setMessage("登录成功");
            }else {
                result.setLoginSuccess(false);
                result.setMessage("登录失败");
            }
            result.setSuccess(true);
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage("操作失败");
            logger.error(e.getMessage());
        }
        logger.info("登录校验结束" + result);
        return result;
    }

    @Override
    public EmployeeResult shiroLogin(String jobnumber, String password) {
        EmployeeResult result = new EmployeeResult();
        //获取登录对象
        Subject subject = SecurityUtils.getSubject();
        //登录校验
        try{
            //执行shiro自带的登录方法，传入的参数是AuthenticationToken对象，
            //但是AuthenticationToken是一个接口，所以使用其实现类UsernamePasswordToken，
            //而UsernamePasswordToken传入的参数是登录的用户名和密码
            subject.login(new UsernamePasswordToken(jobnumber, password));
            //登录成功的结果信息
            result.setLoginSuccess(true);
            //操作成功的结果信息
            result.setSuccess(true);
            result.setMessage("登录成功");
            //获取shiro的session会话
            Session session = subject.getSession();
            //将登录成功的员工信息存入session作用域当中，
            //由于登录对象存放在subject当中，所以从subject当中获取登录成功的对象信息
            session.setAttribute("employee", subject.getPrincipal());
        }catch (Exception e){
            e.printStackTrace();
            result.setLoginSuccess(false);
            result.setSuccess(false);
            result.setMessage("登录失败");
        }

        return result;
    }

    @Override
    public List<Employee> findEmployeeByCondition(Employee employee) {
        employee.setLimitstart((employee.getPage() - 1) * employee.getRows());
        List<Employee> employeeList = employeeMapper.findEmployeeByCondition(employee);
        for (Employee employee1 : employeeList){
            if (employee1.getEsex() == 0){
                employee1.setEsexstr("男");
            }else {
                employee1.setEsexstr("女");
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            employee1.setHireDatestr(dateFormat.format(employee1.getHireDate()));
        }
        return employeeList;
    }

    @Override
    public int totalRowsEmployeeByCondition(Employee employee) {
        return employeeMapper.totalRowsEmployeeByCondition(employee);
    }

    @Override
    @Transactional
    public BaseResult deleteEmployeeByEid(int eid) {
        logger.info("进入单条删除方法，入参：" + eid);
        BaseResult result = new BaseResult();
        try {
            //删除员工角色表
            EmployeeEroleExample employeeEroleExample = new EmployeeEroleExample();
            EmployeeEroleExample.Criteria criteria = employeeEroleExample.createCriteria();
            criteria.andEidEqualTo(eid);
            employeeEroleMapper.deleteByExample(employeeEroleExample);
            //删除员工表
            employeeMapper.deleteByPrimaryKey(eid);
            result.setSuccess(true);
            result.setMessage("删除成功");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("删除失败");
        }
        return result;
    }

    @Override
    @Transactional
    public BaseResult addEmployee(Employee employee) {
        logger.info("进入新增员工方法，入参：" + employee);
        BaseResult result = new BaseResult();
        try {
            //工号唯一校验
            EmployeeExample employeeExample = new EmployeeExample();
            EmployeeExample.Criteria criteria = employeeExample.createCriteria();
            criteria.andJobnumberEqualTo(employee.getJobnumber());
            List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
            if (employeeList != null && employeeList.size() > 0){
                result.setSuccess(false);
                result.setMessage("工号重复");
            }
            //新增员工基本信息
            //获取随机数（盐）
            String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
            //使用shiro对密码加密
            String newpassword = new Md5Hash(employee.getPassword(), salt, 3).toString();
            employee.setPassword(newpassword);
            employee.setRemark1(salt);
            employeeMapper.insert(employee);
            //新增员工角色表数据
            String[] rids = employee.getRids().split(",");
            for (String rid : rids){
                EmployeeErole employeeErole = new EmployeeErole();
                employeeErole.setEid(employee.getEid());
                employeeErole.setRid(Integer.parseInt(rid));
                employeeEroleMapper.insert(employeeErole);
            }
            result.setSuccess(true);
            result.setMessage("新增成功");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("新增失败");
        }
        return result;
    }

    @Override
    @Transactional
    public BaseResult batchDeleteByEids(String eids) {
        logger.info("进入批量删除方法，入参：" + eids);
        BaseResult result1 = new BaseResult();
        try {
            //获取到每一个eid
            String[] eidArray = eids.split(",");
            for (String eid : eidArray){
                //根据eid执行单条删除方法
                BaseResult result = deleteEmployeeByEid(Integer.parseInt(eid));
                if (!result.isSuccess()){
                    throw new Exception();
                }
            }
            result1.setSuccess(true);
            result1.setMessage("删除成功");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result1.setSuccess(false);
            result1.setMessage("删除失败");
        }
        return result1;
    }

    @Override
    public Employee getUpdateEmployeeByEid(int eid) {
        Employee employee = employeeMapper.selectByPrimaryKey(eid);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        employee.setHireDatestr(dateFormat.format(employee.getHireDate()));
        return employee;
    }

    @Override
    public Employee findEmployeeRoleByEid(int eid) {
        Employee employee = employeeEroleMapper.findEmployeeRoleByEid(eid);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        employee.setHireDatestr(dateFormat.format(employee.getHireDate()));
        return employee;
    }

    @Override
    @Transactional
    public BaseResult updateEmployee(Employee employee) {
        logger.info("进入修改方法，入参：" + employee);
        BaseResult result = new BaseResult();
        try {
            //工号唯一校验
            EmployeeExample employeeExample = new EmployeeExample();
            EmployeeExample.Criteria criteria1 = employeeExample.createCriteria();
            criteria1.andJobnumberEqualTo(employee.getJobnumber());
            List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
            if (employeeList != null && employeeList.size() > 0){
                result.setSuccess(false);
                result.setMessage("工号重复");
            }
            //修改员工基本信息
            employeeMapper.updateByPrimaryKeySelective(employee);
            //修改员工角色关系表数据
            //删除以前的员工角色信息
            EmployeeEroleExample employeeEroleExample = new EmployeeEroleExample();
            EmployeeEroleExample.Criteria criteria = employeeEroleExample.createCriteria();
            criteria.andEidEqualTo(employee.getEid());
            employeeEroleMapper.deleteByExample(employeeEroleExample);
            //批量新增新的员工角色信息
            String[] rids = employee.getRids().split(",");
            for (String rid : rids){
                EmployeeErole employeeErole = new EmployeeErole();
                employeeErole.setRid(Integer.parseInt(rid));
                employeeErole.setEid(employee.getEid());
                employeeEroleMapper.insert(employeeErole);
            }
            result.setSuccess(true);
            result.setMessage("修改成功");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("修改失败");
        }
        return result;
    }
}
