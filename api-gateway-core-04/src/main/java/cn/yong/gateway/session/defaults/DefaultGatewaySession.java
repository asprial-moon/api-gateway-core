package cn.yong.gateway.session.defaults;

import cn.yong.gateway.bind.IGenericReference;
import cn.yong.gateway.datasource.Connection;
import cn.yong.gateway.datasource.DataSource;
import cn.yong.gateway.mapping.HttpStatement;
import cn.yong.gateway.session.Configuration;
import cn.yong.gateway.session.GatewaySession;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * @author Line
 * @desc 默认网关会话实现类
 * @date 2022/10/30
 */
public class DefaultGatewaySession implements GatewaySession {
    private Configuration configuration;

    private String uri;

    private DataSource dataSource;

    public DefaultGatewaySession(Configuration configuration, String uri, DataSource dataSource) {
        this.configuration = configuration;
        this.uri = uri;
        this.dataSource = dataSource;
    }

    @Override
    public Object get(String methodName, Object parameter) {
        Connection connection = dataSource.getConnection();

        return connection.execute(methodName, new String[]{"java.lang.String"}, new String[]{"小傅哥"}, new Object[]{parameter});
    }

    @Override
    public IGenericReference getMapper() {
        return configuration.getMapper(uri, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
