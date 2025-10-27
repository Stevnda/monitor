package nnu.edu.back.service.impl;

import nnu.edu.back.common.exception.MyException;
import nnu.edu.back.common.result.ResultEnum;
import nnu.edu.back.common.utils.FileUtil;
import nnu.edu.back.dao.main.ProjectMapper;
import nnu.edu.back.pojo.Project;
import nnu.edu.back.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/29/14:17
 * @Description:
 */
@Service
public class ProjectServiceImpl implements ProjectService {
    @Value("${avatarDir}")
    String avatarDir;

    @Value("${tempDir}")
    String tempDir;

    @Value("${baseDir}")
    String baseDir;

    @Autowired
    ProjectMapper projectMapper;

    @Override
    public String uploadAvatar(MultipartFile file) {
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;
        int code = FileUtil.uploadFile(file, fileName, avatarDir);
        if (code == -1) throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        return fileName;
    }

    @Override
    public void multipartUpload(MultipartFile file, String key, String number) {
        String address = tempDir + key;
        int code = FileUtil.uploadFile(file, number, address);
        if (code == -1) throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
    }

    @Override
    public void mergeMultipartFile(String key, int total) {
        String address = tempDir + key;
        String to = tempDir + key + "/test.zip";
        String unpackAddress = baseDir + key;
        new File(unpackAddress).mkdirs();
        File file = new File(address);
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        String[] fileName = file.list();
        if (fileName.length == total) {
            FileUtil.mergeFile(address, to, total);
            FileUtil.unpack(to, unpackAddress);
        } else throw new MyException(-99, "数据缺损!");
    }

    @Override
    public Project createProject(Project project) {
        String id = UUID.randomUUID().toString();
        project.setId(id);
        projectMapper.insertProject(project);
        return project;
    }

    @Override
    public Map<String, Object> pageQueryProject(String keyword, String type, int page, int size) {
        if (!keyword.equals("")) keyword = "%" + keyword + "%";
        int total = projectMapper.getPageCount(keyword, type);
        List<Project> list = projectMapper.pageQuery(keyword, type, size, size * page);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", list);
        return map;
    }

    @Override
    public List<Project> getAllVisualProject() {
        return projectMapper.getAllVisualProject();
    }

    @Override
    public void deleteProject(String id, String role) {
        if (role.equals("admin")) {
            projectMapper.deleteProject(id);
        }
    }
}
