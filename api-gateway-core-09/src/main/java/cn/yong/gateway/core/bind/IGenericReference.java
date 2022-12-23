package cn.yong.gateway.core.bind;

import cn.yong.gateway.core.executor.result.SessionResult;

import java.util.Map;

/**
 * @author Line
 * @desc 统一泛化调用接口
 * @date 2022/10/30
 */
public interface IGenericReference {

    SessionResult $invoke(Map<String, Object> params);

}
