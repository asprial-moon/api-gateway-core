package cn.yong.gateway.core.bind;

import cn.yong.gateway.core.mapping.HttpStatement;
import cn.yong.gateway.core.session.GatewaySession;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import org.objectweb.asm.Type;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Line
 * @desc 泛化调用静态代理工厂
 * @date 2022/10/30
 * <p> 在代理工厂创建对象的实现类中，首先使用 CGLIB 给 RPC 的绑定接口，创建出一个接口出来。并实现我们定义的 IGenericReference 接口和创建的接口，到代理实现类上。
 */
public class MapperProxyFactory {
    private String uri;
    /**
     * 统一泛化调用接口缓存
     */
    private final Map<String, IGenericReference> genericReferenceCache = new ConcurrentHashMap<>();

    public MapperProxyFactory(String uri) {
        this.uri = uri;
    }

    /**
     * 创建新实例
     */
    public IGenericReference newInstance(GatewaySession gatewaySession) {
        return genericReferenceCache.computeIfAbsent(uri, k -> {
            HttpStatement httpStatement = gatewaySession.getConfiguration().getHttpStatement(uri);
            // 泛化调用
            MapperProxy genericReferenceProxy = new MapperProxy(gatewaySession, uri);
            // 创建接口
            InterfaceMaker interfaceMaker = new InterfaceMaker();
            interfaceMaker.add(new Signature(httpStatement.getMethodName(), Type.getType(String.class), new Type[]{Type.getType(String.class)}), null);
            Class interfaceClass = interfaceMaker.create();
            // 代理对象
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Object.class);
            // IGenericReference 统一泛化调用接口
            // interfaceClass    根据泛化调用注册信息创建的接口，建立 http -> rpc 关联
            enhancer.setInterfaces(new Class[]{IGenericReference.class, interfaceClass});
            enhancer.setCallback(genericReferenceProxy);
            return (IGenericReference) enhancer.create();
        });
    }
}
