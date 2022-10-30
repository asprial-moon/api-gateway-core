package cn.yong.gateway.session.defaults;

import cn.yong.gateway.session.Configuration;
import cn.yong.gateway.session.IGenericReferenceSessionFactory;
import cn.yong.gateway.session.SessionServer;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Line
 * @desc 泛化调用会话工厂
 * @date 2022/10/30
 */
public class GenericReferencesSessionFactory implements IGenericReferenceSessionFactory {

    private final Logger logger = LoggerFactory.getLogger(GenericReferencesSessionFactory.class);

    private final Configuration configuration;

    public GenericReferencesSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Future<Channel> openSession() throws ExecutionException, InterruptedException {
        SessionServer server = new SessionServer(configuration);

        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();

        if (null == channel)
            throw new RuntimeException("netty server start error channel is null");

        while (!channel.isActive()) {
            logger.info("netty server gateway start Ing ...");
            Thread.sleep(500);
        }

        logger.info("netty server gateway start done! {}", channel.localAddress());

        return future;
    }
}
