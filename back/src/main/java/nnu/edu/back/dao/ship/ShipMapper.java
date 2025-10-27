package nnu.edu.back.dao.ship;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/17/19:24
 * @Description:
 */
@Repository
public interface ShipMapper {
    List<Map<String, Object>> getShipByBox(@Param("tableName") String tableName, @Param("top") double top, @Param("right") double right, @Param("bottom") double bottom, @Param("left") double left);

    Integer existTable(@Param("tableName") String tableName);

    List<Map<String, Object>> record(@Param("tableName") String tableName, @Param("mmsi") String mmsi, @Param("time") String time);

    List<Map<String, Object>> getShipInfoByBoxAndTime(@Param("tableName") String tableName, @Param("top") double top, @Param("right") double right, @Param("bottom") double bottom, @Param("left") double left, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> pageQuery(@Param("size") int size, @Param("start") int start, @Param("keyword") String keyword);

    int count(@Param("keyword") String keyword);
}
