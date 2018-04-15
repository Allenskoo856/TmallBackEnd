package com.mmall.test;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author : Administrator
 * @create 2018-04-15 15:20
 */
public class BigDescimalTest {

    @Test
    public void test1() {
        BigDecimal b1 = new BigDecimal(0.05);
        BigDecimal b2 = new BigDecimal(0.02);
        System.out.println(b1.add(b2));
    }

    @Test
    public void test2() {
        BigDecimal b1 = new BigDecimal("0.05");
        BigDecimal b2 = new BigDecimal("0.02");
        System.out.println(b1.add(b2));
    }
}
