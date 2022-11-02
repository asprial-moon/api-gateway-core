package cn.yong.gateway.datasource;

/**
 * @author Allen
 * @desc 数据源接口，RPC、HTTP 都当做连接的数据资源使用
 * @date 2022/10/31
 */
public interface DataSource {

    Connection getConnection();

}
