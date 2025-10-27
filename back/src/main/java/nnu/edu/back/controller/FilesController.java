package nnu.edu.back.controller;

import com.alibaba.fastjson2.JSONObject;
import nnu.edu.back.common.auth.AuthCheck;
import nnu.edu.back.common.resolver.JwtTokenParser;
import nnu.edu.back.common.result.JsonResult;
import nnu.edu.back.common.result.ResultUtils;
import nnu.edu.back.pojo.Files;
import nnu.edu.back.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/21:36
 * @Description:
 */
@RestController
@RequestMapping("/files")
public class FilesController {
    @Autowired
    FilesService filesService;

    @AuthCheck
    @RequestMapping(value = "/addFile", method = RequestMethod.POST)
    public JsonResult addFile(@RequestBody Files file) {
        return ResultUtils.success(filesService.addFiles(file));
    }


    @RequestMapping(value = "/downloadFile/{id}", method = RequestMethod.GET)
    public void downloadFile(@PathVariable String id, HttpServletResponse response) {
        filesService.downloadFile(id, response);
    }

    @AuthCheck
    @RequestMapping(value = "/bindVisualData", method = RequestMethod.POST)
    public JsonResult bindVisualData(@RequestBody JSONObject jsonObject) {
        return ResultUtils.success(filesService.bindVisualData(jsonObject));
    }

    @AuthCheck
    @RequestMapping(value = "/cancelVisualBind/{fileId}", method = RequestMethod.DELETE)
    public JsonResult cancelVisualBind(@PathVariable String fileId) {
        filesService.cancelVisualBind(fileId);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/findByFolderId/{parentId}", method = RequestMethod.GET)
    public JsonResult findByFolderId(@PathVariable String parentId, @JwtTokenParser("role") String role) {
        return ResultUtils.success(filesService.findByFolderId(parentId, role));
    }

    @AuthCheck
    @RequestMapping(value = "/addFolder", method = RequestMethod.POST)
    public JsonResult addFolder(@RequestBody JSONObject jsonObject, @JwtTokenParser("role") String role) {
        return ResultUtils.success(filesService.addFolder(jsonObject.getString("folderName"), jsonObject.getString("parentId"), role));
    }

    @AuthCheck
    @RequestMapping(value = "/getVisualFileByVisualId/{visualId}", method = RequestMethod.GET)
    public JsonResult getVisualFileByVisualId(@PathVariable String visualId) {
        return ResultUtils.success(filesService.getVisualFileByVisualId(visualId));
    }

    @AuthCheck
    @RequestMapping(value = "/deleteFilesOrFolders", method = RequestMethod.POST)
    public JsonResult deleteFilesOrFolders(@RequestBody JSONObject jsonObject, @JwtTokenParser("role") String role) {
        filesService.deleteFilesOrFolders(jsonObject, role);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getUploadRecord", method = RequestMethod.GET)
    public JsonResult getUploadRecord(@JwtTokenParser("role") String role) {
        return ResultUtils.success(filesService.getUploadRecord(role));
    }

    @AuthCheck
    @RequestMapping(value = "/uploadChunks", method = RequestMethod.POST)
    public JsonResult uploadChunks(@RequestParam MultipartFile file, @RequestParam String number, @RequestParam String id) {
        filesService.uploadChunks(file, number, id);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/mergeChunks", method = RequestMethod.POST)
    public JsonResult mergeChunks(@RequestBody JSONObject jsonObject) {
        return ResultUtils.success(filesService.mergeChunks(jsonObject.getString("parentId"), jsonObject.getString("id"), jsonObject.getIntValue("total"), jsonObject.getString("fileName")));
    }

    @AuthCheck
    @RequestMapping(value = "/visualFileMerge/{id}/{total}/{type}/{name}", method = RequestMethod.POST)
    public JsonResult visualFileMerge(@PathVariable String id, @PathVariable int total, @PathVariable String type, @PathVariable String name) {
        return ResultUtils.success(filesService.visualFileMerge(id, total, type, name));
    }

    @AuthCheck
    @RequestMapping(value = "/delAllRecord", method = RequestMethod.DELETE)
    public JsonResult delAllRecord(@JwtTokenParser("role") String role) {
        filesService.delAllRecord(role);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/delRecord/{id}", method = RequestMethod.DELETE)
    public JsonResult delRecord(@PathVariable String id, @JwtTokenParser("role") String role) {
        filesService.delRecord(id, role);
        return ResultUtils.success();
    }

}
