package com.objectica.bbot.behavior;

import java.util.Map;

/**
 * @author curly
 */
public class BehaviorContext {
    Map<String,Object> dataMap;

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }
}
