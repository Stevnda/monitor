package nnu.edu.back.dao.dynamic;

import nnu.edu.back.pojo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/23/11:04
 * @Description:
 */
@Repository
public interface DynamicMapper {
    List<String> getLocusPoint(String id);

    List<Locus> getLocusTable(String id, @Param("name") String name);

    List<Section> getAllSection(String id);

    List<Map<String, Object>> getDistanceAndElevationByName(String id, @Param("sectionName") String sectionName);

    List<String> getTableIdList(String id);

    String getTypeByTableId(String id, @Param("tableId") String tableId);

    List<String> getTimeListByTableId(String id, @Param("tableId") String tableId);

    List<String> getNameListByTableId(String id, @Param("tableId") String tableId);

    List<Double> getFluxValueByTableIdAndName(String id, @Param("tableId") String tableId, @Param("name") String name);


    List<Substrate> getAllSubstrate(String id);

    List<String> getSandTransportTableList(String id);

    String getSandTransportTypeByTableId(String id, @Param("tableId") String tableId);

    List<String> getSandTransportTimeListByTableId(String id, @Param("tableId") String tableId);

    List<String> getSandTransportNameListByTableId(String id, @Param("tableId") String tableId);

    List<Double> getSandTransportValueByTableIdAndName(String id, @Param("tableId") String tableId, @Param("name") String name);

    List<String> getSectionSegment(String id, @Param("name") String name, @Param("type") String type);

    List<Map<String, Object>> getSpeedOrientationNameAndType(String id);

    List<String> getTime(String id, @Param("name") String name, @Param("type") String type);

    List<Double> getSpeedByNameAndTypeAndDistance(String id, @Param("name") String name, @Param("type") String type, @Param("distance") String distance);

    List<Double> getOrientationByNameAndType(String id, @Param("name") String name, @Param("type") String type, @Param("distance") String distance);

    List<String> getSandContentClass(String id);

    List<String> getSandContentTime(String id, @Param("name") String name);

    List<Double> getSandContentValue(String id, @Param("name") String name);

    List<Map<String, Object>> test(String id, @Param("sql") String sql);
}
