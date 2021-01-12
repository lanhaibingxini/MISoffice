package com.ujiuye.realm;

import com.ujiuye.bean.Efunction;
import com.ujiuye.bean.Employee;
import com.ujiuye.bean.EmployeeExample;
import com.ujiuye.bean.Erole;
import com.ujiuye.dao.EfunctionMapper;
import com.ujiuye.dao.EmployeeMapper;
import com.ujiuye.dao.EroleMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MyRealm extends AuthorizingRealm {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private EroleMapper eroleMapper;
    @Autowired
    private EfunctionMapper efunctionMapper;

    //真正的授权方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //获取当前用户信息
        Employee employee = (Employee) principalCollection.getPrimaryPrincipal();
        //获取当前用户的角色信息
        List<Erole> eroleList = eroleMapper.findRolesByEid(employee.getEid());
        //将角色信息封装到AuthorizationInfo里面
        for (Erole erole : eroleList){
            authorizationInfo.addRole(erole.getRcode());
        }
        //获取当前用户的权限信息
        List<Efunction> efunctionList = efunctionMapper.getFunctionsByEid(employee.getEid());
        //将功能信息封装到AuthorizationInfo里面
        for (Efunction efunction : efunctionList){
            authorizationInfo.addStringPermission(efunction.getFcode());
        }
        return authorizationInfo;
    }

    //真正的认证（登录）方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //根据用户名去数据库获取用户信息
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        criteria.andJobnumberEqualTo(token.getUsername());
        List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
        if (employeeList != null && employeeList.size() > 0){
            Employee employee = employeeList.get(0);
            //第一个参数，根据用户名获取到的数据
            //第二个参数，根据用户名获取的用户信息中的密码
            //第三个参数，传入token中的用户名
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(employee, employee.getPassword(), token.getUsername());
            //指定加密盐
            authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(employee.getRemark1()));
            return authenticationInfo;
        }

        return null;
    }
}
