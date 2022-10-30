package cn.yong.gateway.session.defaults;

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
    public GatewaySession openSession() {
        return new DefaultGatewaySession(configuration);
    }
}
