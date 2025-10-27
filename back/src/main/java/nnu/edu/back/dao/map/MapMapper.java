package nnu.edu.back.dao.map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/11/04/20:48
 * @Description:
 */
@Repository
public interface MapMapper {
    Object getWaterwayMap(@Param("x") String x, @Param("y") String y, @Param("zoom") String zoom);

    Object getWaterwayMark(@Param("x") String x, @Param("y") String y, @Param("zoom") String zoom);

    Object getRasterTile(@Param("x") String x, @Param("y") String y, @Param("zoom") String zoom, @Param("name") String name);
}
