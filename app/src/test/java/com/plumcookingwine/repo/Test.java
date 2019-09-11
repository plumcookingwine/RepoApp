package com.plumcookingwine.repo;

/**
 * @author kangf
 * @data 2019/9/10
 * @description class Test
 */
public class Test {

    @org.junit.Test
    public void test(){

        int i = 10;


        do {
            i /= 2;
        } while (i-- > 1);

        System.out.println("i === "+   i);
    }
}
