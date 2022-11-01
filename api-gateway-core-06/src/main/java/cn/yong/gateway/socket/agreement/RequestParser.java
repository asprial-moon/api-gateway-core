package cn.yong.gateway.socket.agreement;

import com.alibaba.fastjson2.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Line
 * @desc 请求解析器，解析HTTP请求，GET/POST form-data\raw-json
 * @date 2022/11/1
 * <ul>
 *     <Li> 首先通过 FullHttpRequest 获取请求的方法类型 GET/POST 当然还有其他的这里暂时不需要添加
 *     <li> 获取类型后，按照 GET/POST 分别做解析处理。其实在网关中最常用的就是 POST 请求 + application/json 的方式。
 *          同时会含有 token 信息一并传递，这些都是为了避免网关接口被外部滥用的情况
 * </ul>
 */
public class RequestParser {
    private final FullHttpRequest request;

    public RequestParser(FullHttpRequest request) {
        this.request = request;
    }

    public String getUri() {
        String uri = request.uri();
        int idx = uri.indexOf("?");
        uri = idx > 0 ? uri.substring(0, idx) : uri;
        if (uri.equals("/favicon.ico")) return null;
        return uri;
    }

    public Map<String, Object> parse() {
        // 获取Content-type
        String contentType = getContentType();
        // 获取请求类型
        HttpMethod method = request.method();
        if (HttpMethod.GET == method) {
            Map<String, Object> parameterMap = new HashMap<>();
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            decoder.parameters().forEach((key, value) -> parameterMap.put(key, value.get(0)));
            return parameterMap;
        } else if (HttpMethod.POST == method) {
            switch (contentType) {
                case "multipart/form-data":
                    Map<String, Object> parameterMap = new HashMap<>();
                    HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
                    decoder.offer(request);
                    decoder.getBodyHttpDatas().forEach(data -> {
                        Attribute attr = (Attribute) data;
                        try {
                            parameterMap.put(data.getName(), attr.getValue());
                        } catch (IOException ignore) {
                        }
                    });
                    return parameterMap;
                case "application/json":
                    ByteBuf byteBuf = request.content().copy();
                    if (byteBuf.isReadable()) {
                        String content = byteBuf.toString(StandardCharsets.UTF_8);
                        return JSON.parseObject(content);
                    }
                default:
                    throw new RuntimeException("未实现的协议类型 Content-Type: " + contentType);
            }
        }

        throw new RuntimeException("未实现的请求类型 HttpMethod: " + method);
    }

    private String getContentType() {
        Optional<Map.Entry<String, String>> header = request.headers().entries().stream().filter(
                val -> val.getKey().equals("Content-Type")
        ).findAny();

        Map.Entry<String, String> entry = header.orElse(null);
        // application/json、multipart/form-data;
        String contentType = entry.getValue();
        int idx = contentType.indexOf(";");
        if (idx > 0) {
            return contentType.substring(0, idx);
        } else {
            return contentType;
        }
    }
}
