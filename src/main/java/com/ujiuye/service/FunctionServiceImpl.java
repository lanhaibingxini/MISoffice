package com.ujiuye.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ujiuye.bean.Efunction;
import com.ujiuye.bean.EfunctionExample;
import com.ujiuye.bean.Employee;
import com.ujiuye.bean.EroleEfunctionExample;
import com.ujiuye.dao.EfunctionMapper;
import com.ujiuye.dao.EroleEfunctionMapper;
import com.ujiuye.utils.BaseResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
public class FunctionServiceImpl implements FunctionService {
    private static Logger logger = Logger.getLogger(FunctionServiceImpl.class);
    @Autowired
    private EfunctionMapper efunctionMapper;
    @Autowired
    private EroleEfunctionMapper eroleEfunctionMapper;

    //根据员工id获取这个员工拥有的所有功能
    @Override
    public List<Efunction> getFunctionsByEid(int eid) {
        List<Efunction> efunctionList = efunctionMapper.getFunctionsByEid(eid);
        return efunctionList;
    }


    @Override
    public JSONArray functions2Json(List<Efunction> efunctionList) {
        JSONArray jsonArray = new JSONArray();
        //获取所有功能中的一级功能
        for (Efunction efunction : efunctionList){
            if (efunction.getFparentid() == 0){
                //JSONArray中一级功能是对象数组，因此使用Object
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", efunction.getFid());
                jsonObject.put("text", efunction.getFname());
                jsonObject.put("state", "closed");
                JSONArray children = new JSONArray();
                //获取所有功能中的二级功能
                for (Efunction efunction1 : efunctionList){
                    if(efunction1.getFparentid() == efunction.getFid()){
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("id", efunction1.getFid());
                        jsonObject1.put("text", efunction1.getFname());
                        jsonObject1.put("state", "open");
                        jsonObject1.put("url", efunction1.getFurl());
                        //将二级功能的字段定义好之后，加入树的子节点
                        children.add(jsonObject1);
                    }
                }
                jsonObject.put("children", children);
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    //将获取到的员工功能转换为JSONArray格式
    @Override
    public JSONArray functions2Json2(List<Efunction> efunctionList, int fparentid, List<Efunction> roleFunctions) {
        JSONArray jsonArray = new JSONArray();
        //遍历所有功能
        for (Efunction efunction : efunctionList){
            //如果父id与传的父id相同（传入的父id为0，代表从一级功能开始遍历）
            if (efunction.getFparentid() == fparentid){
                //JSONArray中一级功能是对象数组，因此使用Object
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", efunction.getFid());
                jsonObject.put("text", efunction.getFname());
                //如果remark1为yes，代表是叶子节点，状态为open
                if ("YES".equals(efunction.getRemark1())){
                    jsonObject.put("state", "open");
                    jsonObject.put("url", efunction.getFurl());
                    //判断角色功能是否为空
                    if (roleFunctions != null){
                        for (Efunction efunction1 : roleFunctions){
                            if (efunction1.getFid() == efunction.getFid()){
                                jsonObject.put("checked", true);
                            }
                        }
                    }
                    //如果不是叶子节点，那就继续调用本方法，继续循环
                }else {
                    JSONArray children = functions2Json2(efunctionList, efunction.getFid(), roleFunctions);
                    if (roleFunctions != null){
                        jsonObject.put("state", "open");
                    }else {
                        jsonObject.put("state", "closed");
                    }
                    jsonObject.put("children", children);
                }
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    //获取所有权限信息
    @Override
    public List<Efunction> findFunctionByCondition(Efunction efunction) {
        //将分页的起始页存放进efunction对象中，为了之后的SQL语句查询
        efunction.setLimitstart((efunction.getPage() - 1) * efunction.getRows());
        List<Efunction> efunctionList = efunctionMapper.findFunctionByCondition(efunction);
        return efunctionList;
    }

    //数据总条数
    @Override
    public int countFunction(Efunction efunction) {
        int total = efunctionMapper.countFunction(efunction);
        return total;
    }

    //获取一级功能
    @Override
    public List<Efunction> getFirstFunction() {
        EfunctionExample efunctionExample = new EfunctionExample();
        EfunctionExample.Criteria criteria = efunctionExample.createCriteria();
        criteria.andFparentidEqualTo(0);
        List<Efunction> efunctionList = efunctionMapper.selectByExample(efunctionExample);
        return efunctionList;
    }

    //新增功能
    @Override
    public BaseResult addFunction(Efunction efunction) {
        logger.info("新增功能开始，入参：" + efunction);
        BaseResult result = new BaseResult();
        try {
            //校验功能编码唯一
            EfunctionExample efunctionExample = new EfunctionExample();
            EfunctionExample.Criteria criteria = efunctionExample.createCriteria();
            criteria.andFcodeEqualTo(efunction.getFcode());
            List<Efunction> efunctionList = efunctionMapper.selectByExample(efunctionExample);
            if (efunctionList != null && efunctionList.size() > 0){
                result.setSuccess(false);
                result.setMessage("功能编码重复");
                return result;
            }
            //新增功能
            if (efunction.getFlevel() == 0){
                efunction.setFparentid(0);
                efunction.setRemark1("NO");
                efunction.setRemark2("一级功能");
            }else {
                efunction.setRemark1("YES");
                //获取二级功能的一级功能信息
                Efunction efunction1 = efunctionMapper.selectByPrimaryKey(efunction.getFparentid());
                efunction.setRemark2(efunction1.getFname());
            }
            efunctionMapper.insert(efunction);
            result.setSuccess(true);
            result.setMessage("新增成功");
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("新增失败");
        }
        return result;
    }

    //根据fid获取当前功能信息
    @Override
    public Efunction findFunctionByFid(int fid) {
        Efunction efunction = efunctionMapper.selectByPrimaryKey(fid);
        return efunction;
    }

    //修改功能
    @Override
    @Transactional
    public BaseResult updateFunction(Efunction efunction) {
        logger.info("进入修改功能");
        BaseResult result = new BaseResult();
        try {
            //检验编码唯一
            EfunctionExample efunctionExample = new EfunctionExample();
            EfunctionExample.Criteria criteria = efunctionExample.createCriteria();
            criteria.andFcodeEqualTo(efunction.getFcode());
            criteria.andFidNotEqualTo(efunction.getFid());
            List<Efunction> efunctionList = efunctionMapper.selectByExample(efunctionExample);
            if (efunctionList != null && efunctionList.size() > 0){
                result.setSuccess(false);
                result.setMessage("功能编码重复");
                return result;
            }
            //修改功能
            if (efunction.getFlevel() == 1){
                //修改之后的功能为二级功能
                EfunctionExample efunctionExample1 = new EfunctionExample();
                EfunctionExample.Criteria criteria1 = efunctionExample1.createCriteria();
                criteria1.andFparentidEqualTo(efunction.getFid());
                //获取该功能的子功能
                List<Efunction> efunctionList1 = efunctionMapper.selectByExample(efunctionExample1);
                //删除相关角色功能表数据
                for (Efunction efunction1 : efunctionList1){
                    EroleEfunctionExample eroleEfunctionExample = new EroleEfunctionExample();
                    EroleEfunctionExample.Criteria criteria2 = eroleEfunctionExample.createCriteria();
                    criteria2.andFidEqualTo(efunction1.getFid());
                    eroleEfunctionMapper.deleteByExample(eroleEfunctionExample);
                }
                //删除之前这个功能下面的子功能
                efunctionMapper.deleteByExample(efunctionExample1);
                efunction.setRemark1("YES");
                //获取父级功能信息
                Efunction efunction1 = efunctionMapper.selectByPrimaryKey(efunction.getFparentid());
                efunction.setRemark2(efunction1.getFname());

            }else {
                //修改之后的功能为一级功能
                efunction.setFparentid(0);
                efunction.setRemark1("NO");
                efunction.setRemark2("一级功能");
            }
            efunctionMapper.updateByPrimaryKey(efunction);
            result.setSuccess(true);
            result.setMessage("修改成功");
        }catch (Exception e){
            e.printStackTrace();
            //手动调用事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setSuccess(false);
            result.setMessage("修改失败");
        }
        return result;
    }

    @Override
    @Transactional
    public BaseResult deleteFunctionByFid(int fid) {
        logger.info("执行删除方法，入参：" + fid);
        BaseResult result = new BaseResult();
        try {

            //获取子功能的数据
            EfunctionExample efunctionExample = new EfunctionExample();
            EfunctionExample.Criteria criteria1 = efunctionExample.createCriteria();
            criteria1.andFidEqualTo(fid);
            List<Efunction> efunctionList = efunctionMapper.selectByExample(efunctionExample);
            if (efunctionList != null && efunctionList.size() > 0){
                for (Efunction efunction : efunctionList){
                    //删除子功能的角色功能表的数据
                    EroleEfunctionExample eroleEfunctionExample = new EroleEfunctionExample();
                    EroleEfunctionExample.Criteria criteria = eroleEfunctionExample.createCriteria();
                    criteria.andFidEqualTo(efunction.getFid());
                    eroleEfunctionMapper.deleteByExample(eroleEfunctionExample);
                    //删除子功能的数据
                    efunctionMapper.deleteByPrimaryKey(efunction.getFid());
                }
            }
            //删除该功能的角色功能表数据
            EroleEfunctionExample eroleEfunctionExample = new EroleEfunctionExample();
            EroleEfunctionExample.Criteria criteria = eroleEfunctionExample.createCriteria();
            criteria.andFidEqualTo(fid);
            eroleEfunctionMapper.deleteByExample(eroleEfunctionExample);
            //删除该功能的数据
            efunctionMapper.deleteByPrimaryKey(fid);
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
    public BaseResult batchDelete(String fids) {
        BaseResult result = new BaseResult();
            String[] fidArray = fids.split(",");
            try {
                for (String fid : fidArray){
                    BaseResult result1 = deleteFunctionByFid(Integer.parseInt(fid));
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
    public List<Efunction> getFunctionByFparentid(int fparentid) {
        EfunctionExample efunctionExample = new EfunctionExample();
        EfunctionExample.Criteria criteria = efunctionExample.createCriteria();
        criteria.andFparentidEqualTo(fparentid);
        List<Efunction> efunctionList = efunctionMapper.selectByExample(efunctionExample);
        return efunctionList;
    }

    @Override
    public List<Efunction> findAllFunction() {
        List<Efunction> efunctionList = efunctionMapper.selectByExample(new EfunctionExample());
        return efunctionList;
    }

    @Override
    public List<Efunction> findAllFunctionByRid(int rid) {
        List<Efunction> efunctionList = eroleEfunctionMapper.findAllFunctionByRid(rid);
        return efunctionList;
    }

    @Override
    public JSONArray convertRolesFunction(List<Efunction> allFunctions, List<Efunction> roleFunctions) {

        return null;
    }


}
