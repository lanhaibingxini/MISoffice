package com.ujiuye.bean;

import com.ujiuye.utils.BasePage;

import java.util.Date;
import java.util.List;

public class Employee extends BasePage {
    private Integer eid;

    private String ename;

    private Integer esex;

    //员工性别的字符串格式，为了方便转换
    private String esexstr;

    private Integer eage;

    private String phone;

    private Date hireDate;

    //入职日期的字符串格式，为了转换
    private String hireDatestr;

    private String jobnumber;

    private String remark1;

    private String remark2;

    private String remark3;

    private String password;

    //所有角色id
    private String rids;

    //所有角色信息
    private List<Erole> eroleList;

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename == null ? null : ename.trim();
    }

    public Integer getEsex() {
        return esex;
    }

    public void setEsex(Integer esex) {
        this.esex = esex;
    }

    public Integer getEage() {
        return eage;
    }

    public void setEage(Integer eage) {
        this.eage = eage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getJobnumber() {
        return jobnumber;
    }

    public void setJobnumber(String jobnumber) {
        this.jobnumber = jobnumber == null ? null : jobnumber.trim();
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1 == null ? null : remark1.trim();
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2 == null ? null : remark2.trim();
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3 == null ? null : remark3.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEsexstr() {
        return esexstr;
    }

    public void setEsexstr(String esexstr) {
        this.esexstr = esexstr;
    }

    public String getHireDatestr() {
        return hireDatestr;
    }

    public void setHireDatestr(String hireDatestr) {
        this.hireDatestr = hireDatestr;
    }

    public String getRids() {
        return rids;
    }

    public void setRids(String rids) {
        this.rids = rids;
    }

    public List<Erole> getEroleList() {
        return eroleList;
    }

    public void setEroleList(List<Erole> eroleList) {
        this.eroleList = eroleList;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "eid=" + eid +
                ", ename='" + ename + '\'' +
                ", esex=" + esex +
                ", esexstr='" + esexstr + '\'' +
                ", eage=" + eage +
                ", phone='" + phone + '\'' +
                ", hireDate=" + hireDate +
                ", hireDatestr='" + hireDatestr + '\'' +
                ", jobnumber='" + jobnumber + '\'' +
                ", remark1='" + remark1 + '\'' +
                ", remark2='" + remark2 + '\'' +
                ", remark3='" + remark3 + '\'' +
                ", password='" + password + '\'' +
                ", rids='" + rids + '\'' +
                ", eroleList=" + eroleList +
                '}';
    }
}