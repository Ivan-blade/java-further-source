package com.lagou.edu.intf;

import java.lang.annotation.*;

/**
 * @author Ivan
 * @date 2021/7/18 上午11:12
 * @description
 */

@Retention(RetentionPolicy.RUNTIME)  // 下面的注解在运行时也能生效
@Documented
@Target(ElementType.TYPE)    // TYPE能够修饰类，接口等等
@Inherited
public @interface TransactionalIntf {
}
