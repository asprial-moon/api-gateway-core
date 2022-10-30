package cn.yong.gateway.test;

import cn.yong.gateway.mapping.HttpCommandType;
import cn.yong.gateway.mapping.HttpStatement;
import cn.yong.gateway.session.Configuration;
import cn.yong.gateway.session.defaults.DefaultGatewaySessionFactory;
import cn.yong.gateway.socket.GatewaySocketServer;
import io.netty.channel.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Line
 * @desc
 * @date 2022/10/30
 */
public class ApiTest {
    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test() throws Exception {
        // 1. 创建配置信息加载注册
        Configuration configuration = new Configuration();
        HttpStatement httpStatement = new HttpStatement("api-gateway-test",
                "cn.bugstack.gateway.rpc.IActivityBooth",
                "sayHi",
                "/wg/activity/sayHi",
                HttpCommandType.GET);
        configuration.addMapper(httpStatement);

        // 2. 基于配置构建会话工厂
        DefaultGatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);

        // 3. 创建启动网关网络服务
        GatewaySocketServer server = new GatewaySocketServer(gatewaySessionFactory);

        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();

        if (null == channel) {
            throw new RuntimeException("netty server start error channel is null");
        }


       while (!channel.isActive()) {
           logger.info("netty server gateway start Ing ...");
           Thread.sleep(500);
       }

        logger.info("netty server gateway start done! {}", channel.localAddress());

        Thread.sleep(Long.MAX_VALUE);
    }

}
