package com.zyh.demo.bean;

import java.io.Serializable;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/9
 */

public class Rate implements Serializable {
    public final String country;
    public final double rate;

    public Rate(String country, double rate) {
        this.country = country;
        this.rate = rate;
    }
}
