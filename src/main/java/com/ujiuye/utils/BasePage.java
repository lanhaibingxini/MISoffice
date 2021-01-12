package com.ujiuye.utils;

public class BasePage {
    //当前页码
    private int page;
    //数据总条数
    private int rows;
    //分页的起始页（（当前页码-1）*数据总条数）
    private int limitstart;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getLimitstart() {
        return limitstart;
    }

    public void setLimitstart(int limitstart) {
        this.limitstart = limitstart;
    }

    @Override
    public String toString() {
        return "BasePage{" +
                "page=" + page +
                ", rows=" + rows +
                ", limitstart=" + limitstart +
                '}';
    }
}
