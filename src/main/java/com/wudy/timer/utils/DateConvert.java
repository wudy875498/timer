package com.wudy.timer.utils;

import org.apache.commons.beanutils.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 日期转换器，String->Date
 */
public class DateConvert implements Converter {

    @Override
    public Object convert(Class arg0, Object arg1) {
        String p = (String) arg1;
        if (p == null || p.trim().length() == 0) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return df.parse(p.trim());
        } catch (Exception e) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                return df.parse(p.trim());
            } catch (ParseException ex) {
                return null;
            }
        }

    }

}
