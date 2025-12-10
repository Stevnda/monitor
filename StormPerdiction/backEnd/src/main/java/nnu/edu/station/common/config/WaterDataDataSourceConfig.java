package nnu.edu.station.common.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "nnu.edu.station.dao.waterdata", sqlSessionTemplateRef = "waterDataSqlSessionTemplate")
public class WaterDataDataSourceConfig {
    @Bean(name = "waterDataDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.waterdata")
    public DataSource waterDataDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "waterDataSqlSessionFactory")
    public SqlSessionFactory waterDataSqlSessionFactory(@Qualifier("waterDataDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/waterdata/*.xml"));
        bean.getObject().getConfiguration().setCallSettersOnNulls(true);
        return bean.getObject();
    }

    @Bean(name = "waterDataTransactionManager")
    public DataSourceTransactionManager waterDataTransactionManager(@Qualifier("waterDataDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "waterDataSqlSessionTemplate")
    public SqlSessionTemplate waterDataSqlSessionTemplate(@Qualifier("waterDataSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}