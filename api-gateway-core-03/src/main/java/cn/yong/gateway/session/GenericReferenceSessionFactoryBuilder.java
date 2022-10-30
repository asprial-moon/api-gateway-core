package cn.yong.gateway.session;

import cn.yong.gateway.session.defaults.GenericReferencesSessionFactory;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Line
 * @desc 会话工厂构造类
 * @date 2022/10/30
 */
public class GenericReferenceSessionFactoryBuilder {

    public Future<Channel> build(Configuration configuration) {
        GenericReferencesSessionFactory genericReferencesSessionFactory = new GenericReferencesSessionFactory(configuration);
        try {
            return genericReferencesSessionFactory.openSession();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
