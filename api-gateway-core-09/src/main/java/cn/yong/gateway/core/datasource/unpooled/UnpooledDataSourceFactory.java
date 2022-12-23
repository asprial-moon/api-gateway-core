package cn.yong.gateway.core.datasource.unpooled;

import cn.yong.gateway.core.session.Configuration;
import cn.yong.gateway.core.datasource.DataSource;
import cn.yong.gateway.core.datasource.DataSourceFactory;
import cn.yong.gateway.core.datasource.DataSourceType;

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
