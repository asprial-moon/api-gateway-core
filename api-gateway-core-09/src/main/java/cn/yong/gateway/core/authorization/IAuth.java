package cn.yong.gateway.core.authorization;

/**
 * @author Allen
 * @desc 认证服务接口
 * @date 2022/11/2
 */
public interface IAuth {

    boolean validate(String id, String token);

}
