package cn.yong.gateway.session;

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
public class SessionServer implements Callable<Channel> {
    private final Logger logger = LoggerFactory.getLogger(SessionServer.class);

    private final EventLoopGroup boss = new NioEventLoopGroup();
    private final EventLoopGroup work = new NioEventLoopGroup();
    private Channel channel;

    private Configuration configuration;

    public SessionServer(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Channel call() throws Exception {
        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new SessionChannelInitializer(configuration));

            channelFuture = bootstrap.bind(new InetSocketAddress(7397)).syncUninterruptibly();
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
