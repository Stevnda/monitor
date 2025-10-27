package nnu.edu.back.dao.main;

import nnu.edu.back.pojo.DownloadHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/19:57
 * @Description:
 */
@Repository
public interface DownloadHistoryMapper {
    void addHistory(@Param("history") DownloadHistory downloadHistory);

    List<Map<String, Object>> pageQuery(@Param("size") int size, @Param("start") int start, @Param("dataListId") String dataListId);

    int countByDataId(@Param("dataListId") String dataListId);
}
