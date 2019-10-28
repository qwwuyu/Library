package com.qwwuyu.example.bean;

import java.util.List;

public class BaseListBean<D> {
    public int total;
    public int pageSize;
    public int pageNum;
    public List<D> list;
}
