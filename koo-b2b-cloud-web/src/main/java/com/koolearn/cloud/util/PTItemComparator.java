package com.koolearn.cloud.util;

import com.koolearn.cloud.composition.entity.RuleItem;
import org.apache.commons.lang.math.RandomUtils;

import java.util.Comparator;

/**
 * Created by haozipu on 2016/8/8.
 */
public class PTItemComparator implements Comparator<RuleItem> {

    @Override
    public int compare(RuleItem o1, RuleItem o2) {
        return o1.getLev()-o2.getLev();
    }


    public static void main(String[] args) {
        Integer randIndex = RandomUtils.nextInt(1);
        System.out.println(randIndex);
    }

}
