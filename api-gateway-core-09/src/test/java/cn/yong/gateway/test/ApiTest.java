package cn.yong.gateway.test;

import cn.yong.gateway.core.mapping.HttpCommandType;
import cn.yong.gateway.core.mapping.HttpStatement;
import cn.yong.gateway.core.session.Configuration;
import cn.yong.gateway.core.session.defaults.DefaultGatewaySessionFactory;
import cn.yong.gateway.core.socket.GatewaySocketServer;
import io.netty.channel.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Line
 * @desc
 * @date 2022/10/30
 */
public class ApiTest {
    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);
/*

    @Test
    public void test() throws Exception {
        // 1. 创建配置信息加载注册
        Configuration configuration = new Configuration();
        configuration.registryConfig("api-gateway-test", "zookeeper://127.0.0.1:2181", "cn.bugstack.gateway.rpc.IActivityBooth", "1.0.0");

        HttpStatement httpStatement01 = new HttpStatement(
                "api-gateway-test",
                "cn.bugstack.gateway.rpc.IActivityBooth",
                "sayHi",
                "java.lang.String",
                "/wg/activity/sayHi",
                HttpCommandType.GET,
                true);

        HttpStatement httpStatement02 = new HttpStatement(
                "api-gateway-test",
                "cn.bugstack.gateway.rpc.IActivityBooth",
                "insert",
                "cn.bugstack.gateway.rpc.dto.XReq",
                "/wg/activity/insert",
                HttpCommandType.POST,
                true);
        configuration.addMapper(httpStatement01);
        configuration.addMapper(httpStatement02);

        // 2. 基于配置构建会话工厂
        DefaultGatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);

        // 3. 创建启动网关网络服务
        GatewaySocketServer server = new GatewaySocketServer(configuration, gatewaySessionFactory);

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
*/

    @Test
    public void test_gateway() throws InterruptedException, ExecutionException {
        // 1.创建配置信息加载注册
        Configuration configuration = new Configuration();
        configuration.setHostName("127.0.0.1");
        configuration.setPort(7397);

        // 2.基于配置构建会话工厂
        DefaultGatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);

        // 3.创建启动网关网络服务
        GatewaySocketServer server = new GatewaySocketServer(configuration, gatewaySessionFactory);


        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();

        if (null == channel) throw new RuntimeException("netty server start error channel is null");

        while (!channel.isActive()) {
            logger.info("netty server gateway start Ing ...");
            Thread.sleep(500);
        }
        logger.info("netty server gateway start Done! {}", channel.localAddress());

        // 4.注册接口
        configuration.registryConfig("api-gateway-test", "zookeeper://127.0.0.1:2181", "cn.bugstack.gateway.rpc.IActivityBooth", "1.0.0");

        HttpStatement httpStatement01 = new HttpStatement(
                "api-gateway-test",
                "cn.bugstack.gateway.rpc.IActivityBooth",
                "sayHi",
                "java.lang.String",
                "/wg/activity/sayHi",
                HttpCommandType.GET,
                true);

        HttpStatement httpStatement02 = new HttpStatement(
                "api-gateway-test",
                "cn.bugstack.gateway.rpc.IActivityBooth",
                "insert",
                "cn.bugstack.gateway.rpc.dto.XReq",
                "/wg/activity/insert",
                HttpCommandType.POST,
                true);

        configuration.addMapper(httpStatement01);
        configuration.addMapper(httpStatement02);

        Thread.sleep(Long.MAX_VALUE);
    }

}
