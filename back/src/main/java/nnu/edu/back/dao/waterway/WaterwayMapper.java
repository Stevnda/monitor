package nnu.edu.back.dao.waterway;

import nnu.edu.back.pojo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/16/22:25
 * @Description:
 */
@Repository
public interface WaterwayMapper {
    List<Anchor> getAllAnchorInfo();

    List<Anchor> pageQueryAnchor(@Param("size") int size, @Param("start") int start, @Param("keyword") String keyword);

    int countAnchor(@Param("keyword") String keyword);

    List<Bridge> getAllBridgeInfo();

    List<Bridge> pageQueryBridge(@Param("size") int size, @Param("start") int start, @Param("keyword") String keyword);

    int countBridge(@Param("keyword") String keyword);

    List<Buoy> getBuoyByBox(@Param("top") double top, @Param("right") double right, @Param("bottom") double bottom, @Param("left") double left);

    List<Buoy> pageQueryBuoy(@Param("size") int size, @Param("start") int start, @Param("keyword") String keyword);

    int countBuoy(@Param("keyword") String keyword);

    List<Park> getAllParkInfo();

    List<Park> pageQueryPark(@Param("size") int size, @Param("start") int start, @Param("keyword") String keyword);

    int countPark(@Param("keyword") String keyword);

    List<Station> getStationByBox(@Param("top") double top, @Param("right") double right, @Param("bottom") double bottom, @Param("left") double left);

    List<Station> getAllStation();

    List<Station> pageQueryStation(@Param("size") int size, @Param("start") int start, @Param("keyword") String keyword);

    int countStation(@Param("keyword") String keyword);

    Object getPhoto(@Param("fileName") String fileName);

    List<Station> getPredictionStation();

    Station getStationInfoByStationId(@Param("stationId") String stationId);
}
