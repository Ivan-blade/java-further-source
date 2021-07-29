package com.lagou.edu.utils;

import com.lagou.edu.intf.AutowiredIntf;
import com.lagou.edu.intf.ComponentIntf;

import java.sql.SQLException;

/**
 * @author hylu.Ivan
 * @date 2021/7/19 下午3:17
 * @description 事务管理器类：负责手动事务的开启、提交、回滚
 */
@ComponentIntf
public class TransactionManager {

    @AutowiredIntf
    private ConnectionUtils connectionUtils;

    @Override
    public String toString() {
        return "TransactionManager{" +
                "connectionUtils=" + connectionUtils +
                '}';
    }

    // 开启手动事务控制
    public void beginTransaction() throws SQLException {
        connectionUtils.getCurrentThreadConn().setAutoCommit(false);
    }


    // 提交事务
    public void commit() throws SQLException {
        connectionUtils.getCurrentThreadConn().commit();
    }


    // 回滚事务
    public void rollback() throws SQLException {
        connectionUtils.getCurrentThreadConn().rollback();
    }
}
