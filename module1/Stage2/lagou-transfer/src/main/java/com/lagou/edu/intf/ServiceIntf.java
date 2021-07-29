package com.lagou.edu.intf;

import java.awt.*;
import java.lang.annotation.*;

/**
 * @author Ivan
 * @date 2021/7/18 上午11:12
 * @description
 */
// 无成员注解为标记注解
// 注解中只能使用八大基本类型和String，Class，enum和Annotation类型
// 元注解可以标记到其他注解上主要有
// @Retention(生命周期),@Documented(提取源码注释),@Target(指定被修饰的注解能够修饰哪些元素，类型，变量等等)
// @Inherited(被修饰的注解可以被继承),@Repeatable(可以重复使用注解)
@Retention(RetentionPolicy.RUNTIME)  // 下面的注解在运行时也能生效
@Documented
@Target(ElementType.TYPE)    // TYPE能够修饰类，接口等等
@Inherited
public @interface ServiceIntf {

    // 在注解中方法名定义了成员变量的名字，返回值定义类该成员变量的类型
    // 如果只有一个值规范上使用value
    // public String value();
    public String value() default "";  // 声明一个名为value的String类型成员变量并指定默认值

}
