package cn.yong.gateway.session;

import cn.yong.gateway.socket.handlers.SessionServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author Line
 * @desc Session初始化，编码器、解码器、流量整形、SSL、自定义服务处理等各类模块
 * @date 2022/10/29
 * <ul>
 *     <li> {@link HttpRequestDecoder}、{@link HttpResponseEncoder}是Netty本身提供的HTTP编辑器，这部分涉及到网络通信中的通信协议和半包粘包处理
 *     <li> {@link HttpObjectAggregator} 用于处理除了GET请求外的POST请求时候的对象信息，否则只有上面的信息，是拿不到POST请求的。*这就很像不断的在管道中添加板子，不同的板子处理不同的功能*
 *     <li> 最后一个{@link SessionServerHandler} 是我们自己实现的会话处理，用于拿到HTTP网络请求后，处理我们自己需要的业务处理
 * </ul>
 */
public class SessionChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpResponseEncoder());
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        pipeline.addLast(new SessionServerHandler());
    }
}
