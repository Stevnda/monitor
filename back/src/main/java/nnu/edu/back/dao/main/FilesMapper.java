package nnu.edu.back.dao.main;

import nnu.edu.back.pojo.Files;
import nnu.edu.back.pojo.Folder;
import nnu.edu.back.pojo.UploadRecord;
import nnu.edu.back.pojo.VisualFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/21:16
 * @Description:
 */
@Repository
public interface FilesMapper {
    void addFile(@Param("files") Files files);

    void batchDelete(@Param("list") List<String> list);

    List<Files> findInfoListById(@Param("list") List<String> list);

    void updateVisualIdAndType(@Param("id") String id, @Param("visualId") String visualId, @Param("type") String type);

    Files findInfoById(@Param("id") String id);

    List<Folder> findFolderByParentId(@Param("parentId") String parentId);

    List<Files> findFilesByParentId(@Param("parentId") String parentId);

    Folder findFolderById(@Param("id") String id);

    void addFolder(@Param("folder") Folder folder);

    VisualFile getVisualFileByVisualId(@Param("visualId") String visualId);

    void recursionDeleteFile(@Param("list") List<String> list);

    void recursionDeleteFolder(@Param("list") List<String> list);

    List<UploadRecord> getUploadRecord();

    void addUploadRecord(@Param("uploadRecord") UploadRecord uploadRecord);

    void delAllRecord();

    void delRecord(@Param("id") String id);
}
