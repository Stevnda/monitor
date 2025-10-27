package nnu.edu.back.dao.main;

import nnu.edu.back.pojo.VisualFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/22:23
 * @Description:
 */
@Repository
public interface VisualFileMapper {
    void addVisualFile(@Param("visualFile") VisualFile visualFile);

    VisualFile findById(@Param("id") String id);

    String getView(@Param("id") String id);
}
