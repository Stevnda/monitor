package nnu.edu.back.dao.main;

import nnu.edu.back.pojo.DataRelational;
import nnu.edu.back.pojo.Files;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/20:16
 * @Description:
 */
@Repository
public interface DataRelationalMapper {
    void batchDeleteByDataListId(@Param("list") List<String> list);

    void deleteByDataListId(@Param("dataListId") String dataListId);

    List<Files> findFilesByDataListId(@Param("dataListId") String dataListId);

    List<String> findFileIdByDataListId(@Param("dataListId") String dataListId);

    List<String> findDataListIdsByFileId(@Param("fileId") String fileId);

    void batchInsert(@Param("list") List<DataRelational> list);

    void batchDelete(@Param("list") List<String> list, @Param("dataListId") String dataListId);

    void batchDeleteByFileId(@Param("fileIdList") List<String> list);

    void recursionDeleteFileId(@Param("folderList") List<String> list);
}
