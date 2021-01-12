package com.ujiuye.bean;

import com.ujiuye.utils.BasePage;

public class Erole extends BasePage {
    private Integer rid;

    //所有角色id
    private String rids;

    private String rcode;

    private String rname;

    private String remark1;

    private String remark2;

    //用于接收表单上功能的信息
    private String fids;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode == null ? null : rcode.trim();
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname == null ? null : rname.trim();
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

    public String getFids() {
        return fids;
    }

    public String getRids() {
        return rids;
    }

    public void setRids(String rids) {
        this.rids = rids;
    }

    @Override
    public String toString() {
        return "Erole{" +
                "rid=" + rid +
                ", rids='" + rids + '\'' +
                ", rcode='" + rcode + '\'' +
                ", rname='" + rname + '\'' +
                ", remark1='" + remark1 + '\'' +
                ", remark2='" + remark2 + '\'' +
                ", fids='" + fids + '\'' +
                '}';
    }

    public void setFids(String fids) {
        this.fids = fids;
    }
}