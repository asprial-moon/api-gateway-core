package cn.yong.gateway.session;

/**
 * @author Line
 * @desc 网关会话工厂接口
 * @date 2022/10/30
 */
public interface GatewaySessionFactory {

    GatewaySession openSession();

}
