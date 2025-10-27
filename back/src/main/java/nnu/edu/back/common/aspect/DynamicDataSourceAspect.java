package nnu.edu.back.common.aspect;

import lombok.extern.slf4j.Slf4j;
import nnu.edu.back.common.config.DataSourceContextHolder;
import nnu.edu.back.common.config.DynamicDataSource;
import nnu.edu.back.common.exception.MyException;
import nnu.edu.back.common.result.ResultEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.sqlite.JDBC;

import javax.sql.DataSource;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/28/20:46
 * @Description:
 */
@Order(1)
@Aspect
@Component
@Slf4j
public class DynamicDataSourceAspect {

    @Value("${projectDir}")
    String projectDir;

    @Autowired
    @Qualifier("dynamicDataSource")
    private DynamicDataSource dynamicDataSource;

    /**
     * 切换数据源
     */
    @Before("execution(* nnu.edu.back.dao.dynamic.*.*(..))")
    public void switchDataSource(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String datasourceId = (String) args[0];
        if (!DataSourceContextHolder.containDataSourceKey(datasourceId)) {
            String datasourceAddress = projectDir + datasourceId + "/dataset.db";
            File file = new File(datasourceAddress);
            if (!file.exists()) {
                throw new MyException(ResultEnum.DATASOURCE_ERROR);
            }
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.url("jdbc:sqlite:" + datasourceAddress);
            dataSourceBuilder.driverClassName(JDBC.class.getName());
            DataSource source = dataSourceBuilder.build();
            dynamicDataSource.addDataSource(datasourceId, source);
        }
        // 切换数据源
        DataSourceContextHolder.setDataSourceKey(datasourceId);
        log.info(DataSourceContextHolder.getDataSourceKey());
    }

    /**
    * @Description:切换shp数据源
    * @Author: Yiming
    * @Date: 2023/8/15
    */
    @Before("execution(* nnu.edu.back.dao.shp.*.*(..))")
    public void switchShpDataSource() {
        if (!DataSourceContextHolder.getDataSourceKey().equals("shp")) {
            DataSourceContextHolder.setDataSourceKey("shp");
        }
    }

    @Before("execution(* nnu.edu.back.dao.ship.*.*(..))")
    public void switchShipDataSource() {
        if (!DataSourceContextHolder.getDataSourceKey().equals("ship")) {
            DataSourceContextHolder.setDataSourceKey("ship");
        }
    }

    @Before("execution(* nnu.edu.back.dao.waterway.*.*(..))")
    public void switchWaterwayDataSource() {
        if (!DataSourceContextHolder.getDataSourceKey().equals("waterway")) {
            DataSourceContextHolder.setDataSourceKey("waterway");
        }
    }

    @Before("execution(* nnu.edu.back.dao.map.*.*(..))")
    public void switchMapDataSource() {
        if (!DataSourceContextHolder.getDataSourceKey().equals("map")) {
            DataSourceContextHolder.setDataSourceKey("map");
        }
    }


    @Before("execution(* nnu.edu.back.dao.main.*.*(..))")
    public void restoreDataSource() {
        if (!DataSourceContextHolder.getDataSourceKey().equals("default")) {
            DataSourceContextHolder.setDataSourceKey("default");
        }
    }

}
