package cn.yong.gateway.socket.handlers;

import cn.yong.gateway.bind.IGenericReference;
import cn.yong.gateway.session.BaseHandler;
import cn.yong.gateway.session.Configuration;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Line
 * @desc 会话服务处理器
 * @date 2022/10/29
 * <ul>
 *     <li> {@link DefaultFullHttpResponse} 相当于就是构建HTTP会话所需的协议信息，包括头信息、编码、响应体长度、跨域访问
 *     <li> 这些信息还包括了我们要向网页端返回的数据，也就是response.content().writeBytes(...)中写入的数据内容
 * </ul>
 */
public class SessionServerHandler extends BaseHandler<FullHttpRequest> {

    private final Logger logger = LoggerFactory.getLogger(SessionServerHandler.class);

    private Configuration configuration;

    public SessionServerHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) {
        logger.info("网关接收请求 uri: {}  method: {}", request.uri(), request.method());

        // 返回信息控制：简单处理
        String methodName = request.uri().substring(1);
        if (methodName.equals("favicon.ico")) return;

        // 返回信息处理
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        // 服务泛化调用
        IGenericReference reference = configuration.getGenericReference("sayHi");
        System.out.println("============================================================================");
        String result = reference.$invoke("大勇") + " " + System.currentTimeMillis();

        // 设置回写数据
        response.content().writeBytes(JSON.toJSONBytes(result, SerializerFeature.PrettyFormat));

        // 头部信息设置
        HttpHeaders heads = response.headers();
        // 返回内容类型
        heads.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + "; charset=UTF-8");
        // 响应体的长度
        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        // 配置持久连接
        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        // 配置跨域访问
        heads.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        heads.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        heads.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE");
        heads.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

        channel.writeAndFlush(response);
    }
}
