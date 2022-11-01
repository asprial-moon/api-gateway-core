package cn.yong.gateway.session.defaults;

import cn.yong.gateway.bind.IGenericReference;
import cn.yong.gateway.datasource.Connection;
import cn.yong.gateway.datasource.DataSource;
import cn.yong.gateway.executor.Executor;
import cn.yong.gateway.mapping.HttpStatement;
import cn.yong.gateway.session.Configuration;
import cn.yong.gateway.session.GatewaySession;
import cn.yong.gateway.type.SimpleTypeRegistry;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Map;

/**
 * @author Line
 * @desc 默认网关会话实现类
 * @date 2022/10/30
 */
public class DefaultGatewaySession implements GatewaySession {
    private Configuration configuration;

    private String uri;

    private Executor executor;

    public DefaultGatewaySession(Configuration configuration, String uri, Executor executor) {
        this.configuration = configuration;
        this.uri = uri;
        this.executor = executor;
    }

    @Override
    public Object get(String methodName, Map<String, Object> params) {
        HttpStatement httpStatement = configuration.getHttpStatement(uri);
        try {
            return executor.exec(httpStatement, params);
        } catch (Exception e) {
            throw new RuntimeException("Error exec get. Cause: " + e);
        }
    }

    @Override
    public Object post(String methodName, Map<String, Object> params) {
        return get(methodName, params);
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
