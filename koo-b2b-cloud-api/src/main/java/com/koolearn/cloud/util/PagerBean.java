package com.koolearn.cloud.util;

import com.koolearn.framework.common.page.ListPager;

import java.io.Serializable;
import java.util.List;

public class PagerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List list;

    private ListPager listPager;

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public ListPager getListPager() {
        return listPager;
    }

    public void setListPager(ListPager listPager) {
        this.listPager = listPager;
    }

}
