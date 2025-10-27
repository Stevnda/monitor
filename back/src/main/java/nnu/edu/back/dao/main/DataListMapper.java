package nnu.edu.back.dao.main;

import nnu.edu.back.pojo.DataList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/23:30
 * @Description:
 */
@Repository
public interface DataListMapper {
    void addDataList(@Param("dataList") DataList dataList);

    void updateDataList(@Param("dataList") DataList dataList);

    DataList getFileInfo(@Param("id") String id);

    void addWatchCount(@Param("id") String id);

    void addDownloadCount(@Param("id") String id);

    List<DataList> fuzzyQuery(@Param("start") int start, @Param("size") int size, @Param("keyword") String keyword, @Param("property") String property, @Param("flag") Boolean flag, @Param("type") String type);

    int countFuzzyQuery(@Param("keyword") String keyword, @Param("type") String type);

    void deleteById(@Param("id") String id);


    List<Map<String, Object>> getSimilarData(@Param("type") String type, @Param("id") String id, @Param("size") int size, @Param("start") int start);

    int getSimilarCount(@Param("type") String type);

    List<Map<String, Object>> getHot(@Param("size") int size);

    List<Map<String, Object>> getRandom(@Param("size") int size);
}
