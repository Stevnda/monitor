package nnu.edu.back.dao.main;

import nnu.edu.back.pojo.BrowseHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/19:28
 * @Description:
 */
@Repository
public interface BrowseHistoryMapper {
    void addHistory(@Param("history") BrowseHistory browseHistory);

    List<Map<String, Object>> getDataGroupByDate(@Param("dataId") String dataId, @Param("date") String date);
}
