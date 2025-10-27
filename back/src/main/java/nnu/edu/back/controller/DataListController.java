package nnu.edu.back.controller;

import com.alibaba.fastjson2.JSONObject;
import nnu.edu.back.common.auth.AuthCheck;
import nnu.edu.back.common.resolver.JwtTokenParser;
import nnu.edu.back.common.result.JsonResult;
import nnu.edu.back.common.result.ResultUtils;
import nnu.edu.back.pojo.DataList;
import nnu.edu.back.service.DataListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/16/0:20
 * @Description:
 */
@RestController
@RequestMapping("/dataList")
public class DataListController {
    @Autowired
    DataListService dataListService;

    @AuthCheck
    @RequestMapping(value = "/addDataList", method = RequestMethod.POST)
    public JsonResult addDataList(@RequestBody DataList dataList) {
        dataListService.addDataList(dataList);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/updateDataList", method = RequestMethod.PATCH)
    public JsonResult updateDataList(@RequestBody DataList dataList) {
        dataListService.updateList(dataList);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getFileInfo/{id}", method = RequestMethod.GET)
    public JsonResult getFileInfo(@PathVariable String id) {
        return ResultUtils.success(dataListService.getFileInfo(id));
    }

    @AuthCheck
    @RequestMapping(value = "/addWatchCount/{id}", method = RequestMethod.PATCH)
    public JsonResult addWatchCount(@PathVariable String id, @JwtTokenParser("email") String email) {
        dataListService.addWatchCount(id, email);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/fuzzyQuery", method = RequestMethod.POST)
    public JsonResult fuzzyQuery(@RequestBody JSONObject jsonObject) {
        int page = jsonObject.getIntValue("page");
        int size = jsonObject.getIntValue("size");
        String keyword = jsonObject.getString("titleKeyword");
        String property = jsonObject.getString("property");
        Boolean flag = jsonObject.getBoolean("flag");
        String type = jsonObject.getString("type");
        return ResultUtils.success(dataListService.fuzzyQuery(page, size, keyword, property, flag, type));
    }


    @RequestMapping(value = "/downloadAll/{id}/{email}", method = RequestMethod.GET)
    public void downloadAll(@PathVariable String email, @PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        dataListService.downloadAll(email, id, request ,response);
    }

    @AuthCheck
    @RequestMapping(value = "/findFiles/{dataListId}", method = RequestMethod.GET)
    public JsonResult findFiles(@PathVariable String dataListId) throws IllegalAccessException {
        return ResultUtils.success(dataListService.findFiles(dataListId));
    }

    @RequestMapping(value = "/getHot/{size}", method = RequestMethod.GET)
    public JsonResult getHot(@PathVariable Integer size) {
        return ResultUtils.success(dataListService.getHot(size));
    }

    @RequestMapping(value = "/getSimilarData/{type}/{id}/{size}/{page}", method = RequestMethod.GET)
    public JsonResult getSimilarData(@PathVariable String type, @PathVariable String id, @PathVariable int size, @PathVariable int page) {
        return ResultUtils.success(dataListService.getSimilarData(type, id, size, page));
    }

    @RequestMapping(value = "/getIdAndDataListName/{size}", method = RequestMethod.GET)
    public JsonResult getIdAndDataListName(@PathVariable int size) {
        return ResultUtils.success(dataListService.getIdAndDataListName(size));
    }

    @RequestMapping(value = "/deleteDataList/{id}", method = RequestMethod.DELETE)
    public JsonResult deleteDataList(@PathVariable String id, @JwtTokenParser("role") String role) {
        dataListService.deleteDataList(id, role);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/getStationInfoByDataListId/{id}", method = RequestMethod.GET)
    public JsonResult getStationInfoByDataListId(@PathVariable String id) {
        return ResultUtils.success(dataListService.getStationInfoByDataListId(id));
    }
}
