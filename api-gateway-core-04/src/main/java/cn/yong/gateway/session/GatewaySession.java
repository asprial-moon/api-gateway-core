package cn.yong.gateway.session;

import cn.yong.gateway.bind.IGenericReference;

/**
 * @author Line
 * @desc 用户处理网关 HTTP 请求
 * @date 2022/10/30
 */
public interface GatewaySession {

    Object get(String methodName, Object parameter);

    IGenericReference getMapper();

    Configuration getConfiguration();
}
