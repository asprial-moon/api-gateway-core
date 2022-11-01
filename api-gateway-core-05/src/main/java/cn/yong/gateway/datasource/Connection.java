package cn.yong.gateway.datasource;

/**
 * @author Allen
 * @desc 连接接口
 * @date 2022/10/31
 */
public interface Connection {

    Object execute(String method, String[] parameterTypes, String[] parameterNames, Object[] args);

}
