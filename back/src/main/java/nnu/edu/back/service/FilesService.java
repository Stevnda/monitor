package nnu.edu.back.service;

import com.alibaba.fastjson2.JSONObject;
import nnu.edu.back.pojo.Files;
import nnu.edu.back.pojo.UploadRecord;
import nnu.edu.back.pojo.VisualFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/21:36
 * @Description:
 */
public interface FilesService {
    String addFiles(Files file);

    void downloadFile(String id, HttpServletResponse response);

    String bindVisualData(JSONObject jsonObject);

    void cancelVisualBind(String id);

    List<Object> findByFolderId(String parentId, String role);

    String addFolder(String folderName, String parentId, String role);

    VisualFile getVisualFileByVisualId(String visualId);

    void deleteFilesOrFolders(JSONObject jsonObject, String role);

    List<UploadRecord> getUploadRecord(String role);

    void uploadChunks(MultipartFile file, String number, String id);

    UploadRecord mergeChunks(String parentId, String id, int total, String fileName);

    String visualFileMerge(String id, int total, String type, String name);

    void delAllRecord(String role);

    void delRecord(String id, String role);
}
