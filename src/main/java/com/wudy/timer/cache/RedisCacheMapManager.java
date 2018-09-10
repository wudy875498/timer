package com.wudy.timer.cache;

import java.util.Map;

public class RedisCacheMapManager extends AbstractCacheMapManager<Object> {

    /**
     * 转换成对应的Bean
     *
     * @param map 待转换的map
     */
    @Override
    public Map<String, Object> toBean(Map<String, Object> map) {
        return map;
    }
}
