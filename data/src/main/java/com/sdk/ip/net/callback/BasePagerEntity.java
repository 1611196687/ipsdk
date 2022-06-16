package com.sdk.ip.net.callback;


import java.io.Serializable;
import java.util.List;

public class BasePagerEntity<T> implements Serializable {

    private int totalCount;
    private int pageSize;
    private int totalPage;
    private int currPage;
    private String list;
    private List<T> pagerList;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public List<T> getPagerList() {
        return pagerList;
    }

    public void setPagerList(List<T> pagerList) {
        this.pagerList = pagerList;
    }

    @Override
    public String toString() {
        return "BasePagerEntity{" +
                "totalCount=" + totalCount +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", currPage=" + currPage +
                ", list='" + list + '\'' +
                ", pagerList=" + pagerList +
                '}';
    }
}
