package cn.yong.gateway.core.datasource;

import cn.yong.gateway.core.session.Configuration;

/**
 * @author Allen
 * @desc 数据源工厂
 * @date 2022/10/31
 */
public interface DataSourceFactory {

    void setProperties(Configuration configuration, String uri);

    /**
     * 获取数据源
     * @return
     */
    DataSource getDataSource();
}
