package cn.yong.gateway.datasource.unpooled;

import cn.yong.gateway.datasource.Connection;
import cn.yong.gateway.datasource.DataSource;
import cn.yong.gateway.datasource.DataSourceType;
import cn.yong.gateway.datasource.connection.DubboConnection;
import cn.yong.gateway.mapping.HttpStatement;
import cn.yong.gateway.session.Configuration;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * @author Allen
 * @desc 无池化的连接池
 * @date 2022/10/31
 */
public class UnpooledDataSource implements DataSource {

    private Configuration configuration;

    private HttpStatement httpStatement;

    private DataSourceType dataSourceType;

    @Override
    public Connection getConnection() {
        switch (dataSourceType) {
            case HTTP:
                //  TODO 预留接口，暂时不需要实现
                break;
            case Dubbo:
                // 配置信息
                String application = httpStatement.getApplication();
                String interfaceName = httpStatement.getInterfaceName();
                // 获取服务
                ApplicationConfig applicationConfig = configuration.getApplicationConfig(application);
                RegistryConfig registryConfig = configuration.getRegistryConfig(application);
                ReferenceConfig<GenericService> reference = configuration.getReferenceConfig(interfaceName);
                return new DubboConnection(applicationConfig, registryConfig, reference);
            default:
                break;
        }
        throw new RuntimeException("DataSourceType: " + dataSourceType + "没有对应的数据源实现");
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setHttpStatement(HttpStatement httpStatement) {
        this.httpStatement = httpStatement;
    }

    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }
}
