package com.lagou.edu.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author apple
 * @date 2021/7/17 下午9:38
 * @description
 */

@Component
@Aspect
public class LogUtils {

    @Pointcut("execution(public void com.lagou.edu.service.impl.TransferServiceImpl.transfer(String,String,int))")
    public void pt1() {

    }

    @Before("pt1()")
    public void beforeMethod(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        System.out.println(Arrays.toString(args));
        System.out.println("业务逻辑开始执行前执行...");
    }

    @After("pt1()")
    public void afterMethod() {
        System.out.println("业务逻辑结束时执行...");
    }


    @AfterThrowing("pt1()")
    public void exceptionMethod() {
        System.out.println("业务逻辑结束时执行...");
    }

    @AfterReturning(value = "pt1()",returning = "retVal")
    public void successMethod(Object retVal) {
        System.out.println("业务逻辑正常执行...");
    }

    // 环绕通知可以替代前面的几种通知
//    @Around("pt1()")
    public Object arroundMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("before around method");

        Object result = null;
        try{
            // proceed方法负责放行目标方法，如果不写相当于不放行
            result = proceedingJoinPoint.proceed();
        } catch (Exception e) {
            System.out.println("around method failed to execute!");
        } finally {
            System.out.println("around neccessary");
        }
        return result;

    }
}
