package cn.yong.gateway.core.socket.handlers;

import cn.yong.gateway.core.mapping.HttpStatement;
import cn.yong.gateway.core.session.Configuration;
import cn.yong.gateway.core.socket.BaseHandler;
import cn.yong.gateway.core.socket.agreement.AgreementConstants;
import cn.yong.gateway.core.socket.agreement.GatewayResultMessage;
import cn.yong.gateway.core.socket.agreement.RequestParser;
import cn.yong.gateway.core.socket.agreement.ResponseParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Line
 * @desc 网络协议处理器
 * @date 2022/10/29
 * <ul>
 *     <li> {@link DefaultFullHttpResponse} 相当于就是构建HTTP会话所需的协议信息，包括头信息、编码、响应体长度、跨域访问
 *     <li> 这些信息还包括了我们要向网页端返回的数据，也就是response.content().writeBytes(...)中写入的数据内容
 * </ul>
 */
public class GatewayServerHandler extends BaseHandler<FullHttpRequest> {

    private final Logger logger = LoggerFactory.getLogger(GatewayServerHandler.class);

    private final Configuration configuration;

    public GatewayServerHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) {
        logger.info("网关接收请求【请求】 uri: {}  method: {}", request.uri(), request.method());
        try {
            // 1. 解析请求参数
            RequestParser requestParser = new RequestParser(request);
            String uri = requestParser.getUri();

            // 2. 保存信息；HttpStatement、Header=token
            HttpStatement httpStatement = configuration.getHttpStatement(uri);
            channel.attr(AgreementConstants.HTTP_STATEMENT).set(httpStatement);

            // 3. 放行服务
            request.retain();
            ctx.fireChannelRead(request);
        } catch (Exception e) {
            // 4. 封装返回结果
            DefaultFullHttpResponse response = new ResponseParser().parse(GatewayResultMessage.buildError(AgreementConstants.ResponseCode._500.getCode(), "网关协议调用失败！" + e.getMessage()));
            channel.writeAndFlush(response);
        }
    }
}
