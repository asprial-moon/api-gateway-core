package cn.yong.gateway.bind;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;

/**
 * @author Line
 * @desc 泛化调用静态代理：方便做一些拦截处理。给 http 对应的 Rpc 调用，
 * 做一层代理控制。每调用到一个 http 对应的网关方法，就会代理的方式调用到 RPC 对应的泛化调用方法上
 * @date 2022/10/30
 * <p> 实现了 {@link MethodInterceptor} 接口的 GenericReferenceProxy 代理类，对 RPC 的泛化调用进行了封装
 */
public class GenericReferenceProxy implements MethodInterceptor {
    /**
     * RPC 泛化调用服务
     */
    private final GenericService genericService;
    /**
     * RPC 泛化调用方法
     */
    private final String methodName;

    public GenericReferenceProxy(GenericService genericService, String methodName) {
        this.genericService = genericService;
        this.methodName = methodName;
    }

    /**
     * <p>做一层代理控制，后续不止是可以使用 Dubbo 泛化调用，也可以是其他服务的泛化调用
     * <p>泛化调用文档：<a href="https://dubbo.apache.org/zh/docsv2.7/user/examples/generic-reference/">泛化调用文档</a>
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameters = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = parameterTypes[i].getName();
        }
        // 举例：genericService.$invoke("sayHi", new String[]{"java.land.String}, new Object[]{"world"})
        return genericService.$invoke(methodName, parameters, args);
    }
}
