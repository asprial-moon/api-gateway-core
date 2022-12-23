package cn.yong.gateway.core.session;

import cn.yong.gateway.core.bind.IGenericReference;

import java.util.Map;

/**
 * @author Line
 * @desc 用户处理网关 HTTP 请求
 * @date 2022/10/30
 */
public interface GatewaySession {

    Object get(String methodName, Map<String, Object> params);

    /**
     * Post请求
     */
    Object post(String methodName, Map<String, Object> params);

    IGenericReference getMapper();

    Configuration getConfiguration();
}
