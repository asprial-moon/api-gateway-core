package cn.yong.gateway.core.socket;

import cn.yong.gateway.core.session.Configuration;
import cn.yong.gateway.core.session.defaults.DefaultGatewaySessionFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

/**
 * @author Line
 * @desc 服务创建，Netty本身就是一个Socket NIO的包装，所以也要对服务的启动处理，这里我们实现Callable接口，让服务在线程池中启动
 * @date 2022/10/29
 * <ul>
 *     <li> {@link EventLoopGroup} 分别启动的是你的连接等待和数据处理，之后这里的 childHandler 添加的就是绘画的初始信息
 *     <li> 当这些信息创建完成，绑定端口启动服务即可
 * </ul>
 */
public class GatewaySocketServer implements Callable<Channel> {
    private final Logger logger = LoggerFactory.getLogger(GatewaySocketServer.class);
    private final Configuration configuration;
    private DefaultGatewaySessionFactory gatewaySessionFactory;
    private EventLoopGroup boss;
    private EventLoopGroup work;
    private Channel channel;

    public GatewaySocketServer(Configuration configuration, DefaultGatewaySessionFactory gatewaySessionFactory) {
        this.configuration = configuration;
        this.gatewaySessionFactory = gatewaySessionFactory;
        this.initEventLoopGroup();
    }

    private void initEventLoopGroup() {
        boss = new NioEventLoopGroup(configuration.getBossNThreads());
        work = new NioEventLoopGroup(configuration.getWorkNThreads());
    }

    @Override
    public Channel call() throws Exception {
        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new GatewayChannelInitializer(configuration, gatewaySessionFactory));
            // Docker 容器部署会自动分配IP，所以我们只设定端口即可。
            channelFuture = bootstrap.bind(configuration.getPort()).syncUninterruptibly();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            logger.error("socket server start error.", e);
        } finally {
            if (null != channelFuture && channelFuture.isSuccess()) {
                logger.info("socket server start done.");
            } else {
                logger.error("socket server start error.");
            }
        }
        return channel;
    }
}
