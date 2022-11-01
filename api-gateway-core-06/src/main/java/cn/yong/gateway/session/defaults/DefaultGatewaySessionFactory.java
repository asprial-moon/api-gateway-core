package cn.yong.gateway.session.defaults;

import cn.yong.gateway.datasource.DataSource;
import cn.yong.gateway.datasource.unpooled.UnpooledDataSourceFactory;
import cn.yong.gateway.session.Configuration;
import cn.yong.gateway.session.GatewaySession;
import cn.yong.gateway.session.GatewaySessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Line
 * @desc 默认网关会话工厂
 * @date 2022/10/30
 */
public class DefaultGatewaySessionFactory implements GatewaySessionFactory {

    private final Logger logger = LoggerFactory.getLogger(DefaultGatewaySessionFactory.class);

    private final Configuration configuration;

    public DefaultGatewaySessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public GatewaySession openSession(String uri) {
        // 获取数据源连接信息：这里把Dubbo、HTTP 抽象为一种连接资源
        UnpooledDataSourceFactory dataSourceFactory = new UnpooledDataSourceFactory();
        dataSourceFactory.setProperties(configuration, uri);
        DataSource dataSource = dataSourceFactory.getDataSource();
        return new DefaultGatewaySession(configuration, uri, dataSource);
    }
}
