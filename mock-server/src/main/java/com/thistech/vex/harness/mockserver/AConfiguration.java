package com.thistech.vex.harness.mockserver;

import com.alibaba.fastjson.JSON;

/**
 * Created by modongsong on 2014/10/13.
 */
public abstract class AConfiguration {

    @Override
    public String toString() {
        return JSON.toJSONString(this, true);
    }

}
