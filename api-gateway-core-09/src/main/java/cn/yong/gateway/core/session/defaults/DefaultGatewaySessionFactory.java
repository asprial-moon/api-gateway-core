package cn.yong.gateway.core.session.defaults;

import cn.yong.gateway.core.datasource.DataSource;
import cn.yong.gateway.core.datasource.unpooled.UnpooledDataSourceFactory;
import cn.yong.gateway.core.executor.Executor;
import cn.yong.gateway.core.session.Configuration;
import cn.yong.gateway.core.session.GatewaySession;
import cn.yong.gateway.core.session.GatewaySessionFactory;
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
        // 创建执行器
        Executor executor = configuration.newExecutor(dataSource.getConnection());
        return new DefaultGatewaySession(configuration, uri, executor);
    }
}
