package com.lagou.edu.intf;

import java.lang.annotation.*;

/**
 * @author Ivan
 * @date 2021/7/19 上午8:43
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)  // 下面的注解在运行时也能生效
@Documented
@Target(ElementType.TYPE)    // TYPE能够修饰类，接口等等
@Inherited
public @interface ComponentIntf {

    public String value() default "";
}
