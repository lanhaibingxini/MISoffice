package com.ujiuye.service;

import com.ujiuye.bean.*;
import com.ujiuye.dao.EmployeeEroleMapper;
import com.ujiuye.dao.EroleEfunctionMapper;
import com.ujiuye.dao.EroleMapper;
import com.ujiuye.utils.BaseResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
@Service
public class RoleServiceImpl implements RoleService {
    private static Logger logger = Logger.getLogger(RoleServiceImpl.class);
    @Autowired
    private EroleMapper eroleMapper;
    @Autowired
    private EroleEfunctionMapper eroleEfunctionMapper;
    @Autowired
    private EmployeeEroleMapper employeeEroleMapper;

    //根据条件获取分页列表数据
    @Override
    public List<Erole> getRoleByCondition(Erole erole) {
        erole.setLimitstart((erole.getPage() - 1) * erole.getRows());
        List<Erole> eroleList = eroleMapper.getRoleByCondition(erole);
        return eroleList;
    }

    //根据条件获取数据总条数
    @Override
    public int countRowsByCondition(Erole erole) {
        int total = eroleMapper.countRowsByCondition(erole);
        return total;
    }

    @Override
    @Transactional
    public BaseResult addRole(Erole erole) {
        logger.info("新增方法开始，入参：" + erole);
        BaseResult result = new BaseResult();
        try {
            //新增角色表
            eroleMapper.insert(erole);
            //批量新增角色功能表
            String[] fids = erole.getFids().split(",");
            for (String fid : fids){
                EroleEfunction eroleEfunction = new EroleEfunction();
                eroleEfunction.setRid(erole.getRid());
                eroleEfunction.setFid(Integer.parseInt(fid));
                eroleEfunctionMapper.insert(eroleEfunction);
            }
            result.setSuccess(true);
            result.setMessage("新增成功");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("新增失败");
        }
        logger.info("新增角色方法结束，出参：" + result);
        return result;
    }

    @Override
    @Transactional
    public BaseResult deleteRoleByrid(int rid) {
        logger.info("进入删除方法，入参：" + rid);
        BaseResult result = new BaseResult();
        try {
            //删除角色功能表数据
            EroleEfunctionExample eroleEfunctionExample = new EroleEfunctionExample();
            EroleEfunctionExample.Criteria criteria = eroleEfunctionExample.createCriteria();
            criteria.andRidEqualTo(rid);
            eroleEfunctionMapper.deleteByExample(eroleEfunctionExample);
            //删除员工角色表数据
            EmployeeEroleExample employeeEroleExample = new EmployeeEroleExample();
            EmployeeEroleExample.Criteria criteria1 = employeeEroleExample.createCriteria();
            criteria1.andRidEqualTo(rid);
            employeeEroleMapper.deleteByExample(employeeEroleExample);
            //删除角色表数据
            eroleMapper.deleteByPrimaryKey(rid);
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
    public BaseResult batchDeleteByRids(String rids) {
        logger.info("进入批量删除方法，入参：" + rids);
        BaseResult result = new BaseResult();
        try {
            //获取到每一个rid
            String[] ridArray = rids.split(",");
            for (String rid : ridArray){
                //再根据每一个rid调用单条删除方法
                BaseResult result1 = deleteRoleByrid(Integer.parseInt(rid));
                //如果在执行单条删除方法的时候不成功就抛出异常
                if (!result1.isSuccess()){
                    throw new Exception();
                }
            }
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
    public List<Erole> getAllRoles() {
        List<Erole> eroleList = eroleMapper.selectByExample(new EroleExample());
        return eroleList;
    }

    @Override
    public Erole getUpdateRoleByRid(int rid) {
        return eroleMapper.selectByPrimaryKey(rid);
    }

    @Override
    @Transactional
    public BaseResult updateRole(Erole erole) {
        logger.info("进入修改方法，入参：" + erole);
        BaseResult result = new BaseResult();
        try {
            //校验角色编码唯一
            EroleExample eroleExample = new EroleExample();
            EroleExample.Criteria criteria = eroleExample.createCriteria();
            criteria.andRcodeEqualTo(erole.getRcode());
            criteria.andRidNotEqualTo(erole.getRid());
            List<Erole> eroleList = eroleMapper.selectByExample(eroleExample);
            if (eroleList != null && eroleList.size() > 0){
                result.setSuccess(false);
                result.setMessage("角色编码重复");
                return result;
            }
            //修改角色基本信息
            eroleMapper.updateByPrimaryKey(erole);
            //删除角色以前的功能
            EroleEfunctionExample eroleEfunctionExample = new EroleEfunctionExample();
            EroleEfunctionExample.Criteria criteria1 = eroleEfunctionExample.createCriteria();
            criteria1.andRidEqualTo(erole.getRid());
            eroleEfunctionMapper.deleteByExample(eroleEfunctionExample);
            //新增修改后的角色功能
            String[] fids = erole.getFids().split(",");
            for (String fid : fids){
                EroleEfunction eroleEfunction = new EroleEfunction();
                eroleEfunction.setFid(Integer.parseInt(fid));
                eroleEfunction.setRid(erole.getRid());
                eroleEfunctionMapper.insert(eroleEfunction);
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
