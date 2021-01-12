package com.ujiuye.bean;

import com.ujiuye.utils.BasePage;

public class Efunction extends BasePage {
    private Integer fid;

    private String fcode;

    private String fname;

    private String furl;

    private Integer fparentid;

    private String remark1;

    private String remark2;

    private String remark3;

    //功能级别
    private int flevel;

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getFcode() {
        return fcode;
    }

    public void setFcode(String fcode) {
        this.fcode = fcode == null ? null : fcode.trim();
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname == null ? null : fname.trim();
    }

    public String getFurl() {
        return furl;
    }

    public void setFurl(String furl) {
        this.furl = furl == null ? null : furl.trim();
    }

    public Integer getFparentid() {
        return fparentid;
    }

    public void setFparentid(Integer fparentid) {
        this.fparentid = fparentid;
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

    public int getFlevel() {
        return flevel;
    }

    public void setFlevel(int flevel) {
        this.flevel = flevel;
    }

    @Override
    public String toString() {
        return "Efunction{" +
                "fid=" + fid +
                ", fcode='" + fcode + '\'' +
                ", fname='" + fname + '\'' +
                ", furl='" + furl + '\'' +
                ", fparentid=" + fparentid +
                ", remark1='" + remark1 + '\'' +
                ", remark2='" + remark2 + '\'' +
                ", remark3='" + remark3 + '\'' +
                ", flevel=" + flevel +
                '}';
    }
}