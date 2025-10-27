package nnu.edu.back.service;

import nnu.edu.back.pojo.Project;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/29/14:17
 * @Description:
 */
public interface ProjectService {
    String uploadAvatar(MultipartFile file);

    void multipartUpload(MultipartFile file, String key, String number);

    void mergeMultipartFile(String key, int total);

    Project createProject(Project project);

    Map<String, Object> pageQueryProject(String keyword, String type, int page, int size);

    List<Project> getAllVisualProject();

    void deleteProject(String id, String role);
}
