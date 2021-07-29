package com.lagou.edu.service;

/**
 * @author hylu.Ivan
 * @date 2021/7/19 下午3:17
 * @description
 */
public interface TransferService {

    void transfer(String fromCardNo,String toCardNo,int money) throws Exception;
}
