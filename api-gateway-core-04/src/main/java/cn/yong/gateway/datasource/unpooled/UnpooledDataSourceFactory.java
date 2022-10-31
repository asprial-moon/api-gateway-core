package cn.yong.gateway.datasource.unpooled;

import cn.yong.gateway.datasource.DataSource;
import cn.yong.gateway.datasource.DataSourceFactory;
import cn.yong.gateway.datasource.DataSourceType;
import cn.yong.gateway.session.Configuration;

/**
 * @author Allen
 * @desc 无池化数据源工厂
 * @date 2022/10/31
 */
public class UnpooledDataSourceFactory implements DataSourceFactory {

    protected UnpooledDataSource dataSource;

    public UnpooledDataSourceFactory() {
        this.dataSource = new UnpooledDataSource();
    }

    @Override
    public void setProperties(Configuration configuration, String uri) {
        this.dataSource.setConfiguration(configuration);
        this.dataSource.setDataSourceType(DataSourceType.Dubbo);
        this.dataSource.setHttpStatement(configuration.getHttpStatement(uri));
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
