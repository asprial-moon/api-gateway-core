package cn.yong.gateway.session;

import cn.yong.gateway.authorization.IAuth;
import cn.yong.gateway.authorization.auth.AuthService;
import cn.yong.gateway.bind.IGenericReference;
import cn.yong.gateway.bind.MapperRegistry;
import cn.yong.gateway.datasource.Connection;
import cn.yong.gateway.executor.Executor;
import cn.yong.gateway.executor.SimpleExecutor;
import cn.yong.gateway.mapping.HttpStatement;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Line
 * @desc 会话生命周期配置项
 * @date 2022/10/30
 * <p> 配置项，用于贯穿整个会话周期，添加配置项用于存放代理对象、服务配置等信息
 */
public class Configuration {

    private final MapperRegistry mapperRegistry = new MapperRegistry(this);

    private final Map<String, HttpStatement> httpStatements = new HashMap<>();

    private final IAuth auth = new AuthService();

    /**
     * RPC 应用服务配置项 api-gateway-test
     */
    private final Map<String, ApplicationConfig> applicationConfigMap = new HashMap<>();
    /**
     * RPC 注册中心配置项 zookeeper://127.0.0.1:2181
     */
    private final Map<String, RegistryConfig> registryConfigMap = new HashMap<>();
    /**
     * RPC 泛化服务配置项 cn.yong.gateway.rpc.IActivityBooth
     */
    private final Map<String, ReferenceConfig<GenericService>> referenceConfigMap = new HashMap<>();

    public Configuration() {
    }

    public synchronized void registryConfig(String applicationName, String address, String interfaceName, String version) {
        if (applicationConfigMap.get(applicationName) == null) {
            ApplicationConfig application = new ApplicationConfig();
            application.setName(applicationName);
            application.setQosEnable(false);
            applicationConfigMap.put(applicationName, application);
        }

        if (registryConfigMap.get(applicationName) == null) {
            RegistryConfig registry = new RegistryConfig();
            registry.setAddress(address);
            registry.setRegister(false);
            registryConfigMap.put(applicationName, registry);
        }

        if (referenceConfigMap.get(interfaceName) == null) {
            ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
            reference.setInterface(interfaceName);
            reference.setVersion(version);
            reference.setGeneric("true");
            referenceConfigMap.put(interfaceName, reference);
        }
    }

    public ApplicationConfig getApplicationConfig(String applicationName) {
        return applicationConfigMap.get(applicationName);
    }

    public RegistryConfig getRegistryConfig(String applicationName) {
        return registryConfigMap.get(applicationName);
    }

    public ReferenceConfig<GenericService> getReferenceConfig(String interfaceName) {
        return referenceConfigMap.get(interfaceName);
    }

    public void addMapper(HttpStatement httpStatement) {
        mapperRegistry.addMapper(httpStatement);
    }

    public IGenericReference getMapper(String uri, GatewaySession gatewaySession) {
        return mapperRegistry.getMapper(uri, gatewaySession);
    }

    public void addHttpStatement(HttpStatement httpStatement) {
        httpStatements.put(httpStatement.getUri(), httpStatement);
    }

    public HttpStatement getHttpStatement(String uri) {
        return httpStatements.get(uri);
    }

    public Executor newExecutor(Connection connection) {
        return new SimpleExecutor(this, connection);
    }

    public boolean authValidate(String uId, String token) {
        return auth.validate(uId, token);
    }
}
