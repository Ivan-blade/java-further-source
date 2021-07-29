package com.lagou.edu.service.impl;

import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.dao.impl.JdbcAccountDaoImpl;
import com.lagou.edu.intf.AutowiredIntf;
import com.lagou.edu.intf.ServiceIntf;
import com.lagou.edu.intf.TransactionalIntf;
import com.lagou.edu.pojo.Account;
import com.lagou.edu.service.TransferService;
import com.lagou.edu.utils.ConnectionUtils;
import com.lagou.edu.utils.TransactionManager;

/**
 * @author hylu.Ivan
 * @date 2021/7/19 下午3:17
 * @description
 */
@ServiceIntf("transferServiceImpl")
@TransactionalIntf
public class TransferServiceImpl implements TransferService {


    @AutowiredIntf("accountDao")
    private JdbcAccountDaoImpl accountDao;


    @Override
    public String toString() {
        return "TransferServiceImpl{" +
                "jdbcAccountDaoImpl=" + accountDao +
                '}';
    }

    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {

            Account from = accountDao.queryAccountByCardNo(fromCardNo);
            Account to = accountDao.queryAccountByCardNo(toCardNo);

            from.setMoney(from.getMoney()-money);
            to.setMoney(to.getMoney()+money);

            accountDao.updateAccountByCardNo(to);
//            int c = 1/0;
            accountDao.updateAccountByCardNo(from);


    }
}
