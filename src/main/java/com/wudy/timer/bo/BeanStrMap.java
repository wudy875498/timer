package com.wudy.timer.bo;

import org.apache.commons.beanutils.BeanMap;

import java.util.HashMap;
import java.util.Map;

public class BeanStrMap extends BeanMap {

    /**
     * Constructs a new empty <code>BeanMap</code>.
     */
    public BeanStrMap() {
    }

    /**
     * Constructs a new <code>BeanMap</code> that operates on the
     * specified bean.  If the given bean is <code>null</code>, then
     * this map will be empty.
     *
     * @param bean the bean for this map to operate on
     */
    public BeanStrMap(Object bean) {
        super(bean);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        for (Object key : super.keySet()) {
            map.put(key.toString(), super.get(key));
        }
        return map;
    }
}
