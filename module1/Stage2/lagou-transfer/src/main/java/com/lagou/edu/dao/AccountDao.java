package com.lagou.edu.dao;

import com.lagou.edu.pojo.Account;

/**
 * @author hylu.Ivan
 * @date 2021/7/19 下午3:17
 * @description
 */
public interface AccountDao {

    Account queryAccountByCardNo(String cardNo) throws Exception;

    int updateAccountByCardNo(Account account) throws Exception;
}
