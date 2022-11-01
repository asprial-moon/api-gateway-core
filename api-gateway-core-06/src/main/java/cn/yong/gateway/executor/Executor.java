package cn.yong.gateway.executor;

import cn.yong.gateway.executor.result.GatewayResult;
import cn.yong.gateway.mapping.HttpStatement;

import java.util.Map;

/**
 * @author Line
 * @desc 执行器
 * @date 2022/11/2
 */
public interface Executor {

    GatewayResult exec(HttpStatement httpStatement, Map<String, Object> params) throws Exception;

}
