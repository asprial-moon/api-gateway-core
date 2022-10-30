package cn.yong.gateway.mapping;

/**
 * @author Line
 * @desc 网关接口映射信息
 * @date 2022/10/30
 */
public class HttpStatement {
    /**
     * 应用名称：
     */
    private String application;
    /**
     * 服务接口：RPC、其他
     */
    private String interfaceName;
    /**
     * 服务方法：RPC#method
     */
    private String methodName;
    /**
     * 网关接口
     */
    private String uri;
    /**
     * GET、POST、PUT、DELETE
     */
    private HttpCommandType httpCommandType;

    public HttpStatement(String application, String interfaceName, String methodName, String uri, HttpCommandType httpCommandType) {
        this.application = application;
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.uri = uri;
        this.httpCommandType = httpCommandType;
    }

    public String getApplication() {
        return application;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getUri() {
        return uri;
    }

    public HttpCommandType getHttpCommandType() {
        return httpCommandType;
    }
}
