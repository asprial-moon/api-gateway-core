package cn.yong.gateway.test;

import cn.yong.gateway.session.Configuration;
import cn.yong.gateway.session.GenericReferenceSessionFactoryBuilder;
import io.netty.channel.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Configuration configuration = new Configuration();
        configuration.addGenericReference("api-gateway-test", "cn.bugstack.gateway.rpc.IActivityBooth", "sayHi");

        GenericReferenceSessionFactoryBuilder builder = new GenericReferenceSessionFactoryBuilder();
        Future<Channel> future = builder.build(configuration);

        logger.info("服务启动完成：{}", future.get().id());

        Thread.sleep(Long.MAX_VALUE);
    }

}
