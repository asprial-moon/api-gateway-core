package cn.yong.gateway.bind;

import cn.yong.gateway.mapping.HttpCommandType;
import cn.yong.gateway.session.Configuration;
import cn.yong.gateway.session.GatewaySession;

import java.lang.reflect.Method;

/**
 * @author Line
 * @desc 绑定调用方法
 * @date 2022/10/30
 */
public class MapperMethod {

    private String methodName;
    private final HttpCommandType command;

    public MapperMethod(String uri, Method method, Configuration configuration) {
        this.methodName = configuration.getHttpStatement(uri).getMethodName();
        this.command = configuration.getHttpStatement(uri).getHttpCommandType();
    }

    public Object execute(GatewaySession session, Object args) {
        Object result = null;
        switch (command) {
            case GET:
                result = session.get(methodName, args);
                break;
            case POST:
                break;
            case PUT:
                break;
            case DELETE:
                break;
            default:
                throw new RuntimeException("Unknown execution method for: " + command);
        }
        return result;
    }
}
