package nnu.edu.back.dao.main;

import nnu.edu.back.pojo.Project;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/28/21:15
 * @Description:
 */
@Repository
public interface ProjectMapper {
    Project queryById(@Param("id") String id);

    void insertProject(@Param("project") Project project);

    List<Project> pageQuery(@Param("keyword") String keyword, @Param("type") String type, @Param("size") int size, @Param("start") int start);

    int getPageCount(@Param("keyword") String keyword, @Param("type") String type);

    List<Project> getAllVisualProject();

    void deleteProject(@Param("id") String id);
}
