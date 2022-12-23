package cn.yong.gateway.core.executor;

import cn.yong.gateway.core.mapping.HttpStatement;
import cn.yong.gateway.core.executor.result.SessionResult;

import java.util.Map;

/**
 * @author Line
 * @desc 执行器
 * @date 2022/11/2
 */
public interface Executor {

    SessionResult exec(HttpStatement httpStatement, Map<String, Object> params) throws Exception;

}
