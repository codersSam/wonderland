package com.senhua.my.wonderland.web.common;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 2019/9/18.
 */
public class CodeValidate {
    public static boolean validateCode(String code,String cacheCode){
        if(StringUtils.isNotBlank(code) && code.equals(cacheCode)){
            return true;
        }
        return false;
    }
}
