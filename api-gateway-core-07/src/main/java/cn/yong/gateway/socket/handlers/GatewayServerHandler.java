package cn.yong.gateway.socket.handlers;

import cn.yong.gateway.bind.IGenericReference;
import cn.yong.gateway.session.GatewaySession;
import cn.yong.gateway.session.defaults.DefaultGatewaySessionFactory;
import cn.yong.gateway.socket.BaseHandler;
import cn.yong.gateway.socket.agreement.RequestParser;
import cn.yong.gateway.socket.agreement.ResponseParser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Line
 * @desc 会话服务处理器
 * @date 2022/10/29
 * <ul>
 *     <li> {@link DefaultFullHttpResponse} 相当于就是构建HTTP会话所需的协议信息，包括头信息、编码、响应体长度、跨域访问
 *     <li> 这些信息还包括了我们要向网页端返回的数据，也就是response.content().writeBytes(...)中写入的数据内容
 * </ul>
 */
public class GatewayServerHandler extends BaseHandler<FullHttpRequest> {

    private final Logger logger = LoggerFactory.getLogger(GatewayServerHandler.class);

    private final DefaultGatewaySessionFactory gatewaySessionFactory;

    public GatewayServerHandler(DefaultGatewaySessionFactory gatewaySessionFactory) {
        this.gatewaySessionFactory = gatewaySessionFactory;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) {
        logger.info("网关接收请求 uri: {}  method: {}", request.uri(), request.method());
        // 1. 解析请求参数
        RequestParser requestParser = new RequestParser(request);
        String uri = requestParser.getUri();
        if (uri == null) return;
        Map<String, Object> args = new RequestParser(request).parse();

        // 2. 调用会话服务
        GatewaySession gatewaySession = gatewaySessionFactory.openSession(uri);
        IGenericReference reference = gatewaySession.getMapper();
        Object result = reference.$invoke(args);

        // 3. 封装返回结果
        DefaultFullHttpResponse response = new ResponseParser().parse(result);
        channel.writeAndFlush(response);
    }
}
