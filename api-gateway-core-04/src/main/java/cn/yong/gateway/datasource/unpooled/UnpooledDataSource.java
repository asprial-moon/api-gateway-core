package cn.yong.gateway.datasource.unpooled;

import cn.yong.gateway.datasource.Connection;
import cn.yong.gateway.datasource.DataSource;

/**
 * @author Allen
 * @desc 无池化的连接池
 * @date 2022/10/31
 */
public class UnpooledDataSource implements DataSource {
    @Override
    public Connection getConnection() {
        return null;
    }
}
