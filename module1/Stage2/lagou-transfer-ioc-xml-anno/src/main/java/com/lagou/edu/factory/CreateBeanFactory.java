package com.lagou.edu.factory;

import com.lagou.edu.utils.ConnectionUtils;

/**
 * @author apple
 * @date 2021/7/13 下午9:30
 * @description
 */
public class CreateBeanFactory {

    public static ConnectionUtils getInstanceStatic() {
        return new ConnectionUtils();
    }
}
