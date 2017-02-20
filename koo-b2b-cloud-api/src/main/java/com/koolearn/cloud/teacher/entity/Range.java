package com.koolearn.cloud.teacher.entity;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 学段对象
 * Created by dongfangnan on 2016/3/31.

 */
public class Range implements Serializable ,Comparator< Range > {

    private int id ;
    private String name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int compare(Range o1, Range o2) {
        if( null != o1 && null != o2 ){
            if( o1.id > o2.id ){
                return 1;
            }else if( o1.id < o2.id ){
                return -1;
            }else{
                return 0;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        Range ranges = ( Range ) o;
        if( this.id == ranges.id ) {
            return true;
        }
        if (this == o) return true;
        if (!(o instanceof Range)) return false;

        Range range = (Range) o;

        if (id != range.id) return false;
        if (name != null ? !name.equals(range.name) : range.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }


}
