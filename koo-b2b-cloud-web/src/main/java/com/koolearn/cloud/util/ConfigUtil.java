package com.koolearn.cloud.util;

import com.koolearn.framework.common.utils.PropertiesConfigUtils;

/**
 * Created by haozipu on 2016/8/18.
 */
public class ConfigUtil {

    private ConfigUtil(){}

    public static String tfsDomain = PropertiesConfigUtils.getProperty("tfs.domains");


}
