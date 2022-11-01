package cn.yong.gateway.bind;

import cn.yong.gateway.session.GatewaySession;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author Line
 * @desc 映射代理调用
 * @date 2022/10/30
 */
public class MapperProxy implements MethodInterceptor {
    /**
     * RPC 泛化调用服务
     */
    private GatewaySession gatewaySession;
    /**
     * RPC 泛化调用方法
     */
    private final String uri;

    public MapperProxy(GatewaySession gatewaySession, String uri) {
        this.gatewaySession = gatewaySession;
        this.uri = uri;
    }

    /**
     *
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        MapperMethod linkMethod = new MapperMethod(uri, method, gatewaySession.getConfiguration());
        return linkMethod.execute(gatewaySession, args[0]);
    }
}
