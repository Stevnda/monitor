package nnu.edu.back.controller;

import com.alibaba.fastjson2.JSONObject;
import nnu.edu.back.common.auth.AuthCheck;
import nnu.edu.back.common.resolver.JwtTokenParser;
import nnu.edu.back.common.result.JsonResult;
import nnu.edu.back.common.result.ResultUtils;
import nnu.edu.back.pojo.AnalysisCase;
import nnu.edu.back.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/18/16:52
 * @Description:
 */
@RestController
@RequestMapping("/analysis")
public class AnalysisController {
    @Autowired
    AnalysisService analysisService;

    @AuthCheck
    @RequestMapping(value = "/addAnalysis", method = RequestMethod.POST)
    public JsonResult addAnalysis(@RequestBody AnalysisCase analysisCase, @JwtTokenParser("email") String email) {
        analysisService.addAnalysis(analysisCase, email);
        return ResultUtils.success();
    }

    @RequestMapping(value="/deleteAnalysisCase/{id}", method = RequestMethod.DELETE)
    public JsonResult deleteAnalysisCase(@PathVariable String id, @JwtTokenParser("email") String email) {
        analysisService.deleteAnalysisCase(id, email);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    public JsonResult pageQuery(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        return ResultUtils.success(analysisService.pageQuery(email, jsonObject.getString("keyword"), jsonObject.getIntValue("page"), jsonObject.getIntValue("size")));
    }

    @AuthCheck
    @RequestMapping(value = "/getAnalysisInfo/{caseId}", method = RequestMethod.GET)
    public JsonResult getAnalysisInfo(@PathVariable String caseId, @JwtTokenParser("email") String email) {
        return ResultUtils.success(analysisService.getAnalysisInfo(caseId, email));
    }

    @AuthCheck
    @RequestMapping(value = "/addData", method = RequestMethod.POST)
    public JsonResult addData(@RequestBody JSONObject jsonObject) {
        String caseId = jsonObject.getString("caseId");
        List<Map<String, String>> list = jsonObject.getObject("list", List.class);
        analysisService.addData(caseId, list);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getData/{id}", method = RequestMethod.GET)
    public JsonResult getData(@PathVariable String id) {
        return ResultUtils.success(analysisService.getData(id));
    }

    @AuthCheck
    @RequestMapping(value = "/delData/{caseId}/{dataListId}/{fileId}", method = RequestMethod.DELETE)
    public JsonResult delData(@PathVariable String caseId, @PathVariable String dataListId, @PathVariable String fileId) {
        analysisService.delData(caseId, dataListId, fileId);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/updateLayer/{caseId}", method = RequestMethod.POST)
    public JsonResult updateLayer(@PathVariable String caseId, @RequestBody List<String> list) {
        analysisService.updateLayer(caseId, list);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getLayersInfo/{caseId}", method = RequestMethod.GET)
    public JsonResult getLayersInfo(@PathVariable String caseId) {
        return ResultUtils.success(analysisService.getLayersInfo(caseId));
    }


    @AuthCheck
    @RequestMapping(value = "/updateAnalysisInfo", method = RequestMethod.PATCH)
    public JsonResult updateProjectInfo(@RequestBody AnalysisCase analysisCase, @JwtTokenParser("email") String email) {
        analysisService.updateProjectInfo(analysisCase, email);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/deleteAnalysis/{caseId}", method = RequestMethod.DELETE)
    public JsonResult deleteAnalysis(@PathVariable String caseId, @JwtTokenParser("email") String email) {
        analysisService.deleteAnalysis(caseId, email);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/findParameterByType/{type}", method = RequestMethod.GET)
    public JsonResult findParameterByType(@PathVariable String type) {
        return ResultUtils.success(analysisService.findParameterByType(type));
    }

    @AuthCheck
    @RequestMapping(value = "/getAnalysisResult/{caseId}", method = RequestMethod.GET)
    public JsonResult getAnalysisResult(@PathVariable String caseId) {
        return ResultUtils.success(analysisService.getAnalysisResult(caseId));
    }

    @AuthCheck
    @RequestMapping(value = "/addDraw", method = RequestMethod.POST)
    public JsonResult addDraw(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        return ResultUtils.success(analysisService.addDraw(jsonObject, email));
    }

    @AuthCheck
    @RequestMapping(value = "/delAnalysisResult/{id}", method = RequestMethod.DELETE)
    public JsonResult delAnalysisResult(@PathVariable String id, @JwtTokenParser("email") String email) {
        analysisService.delAnalysisResult(id, email);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/addSection", method = RequestMethod.POST)
    public JsonResult addSection(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String caseId = jsonObject.getString("caseId");
        String sectionId = jsonObject.getString("sectionId");
        String demId = jsonObject.getString("demId");
        String fileName = jsonObject.getString("fileName");
        return ResultUtils.success(analysisService.addSection(caseId, sectionId, demId, email, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/addSectionCompare", method = RequestMethod.POST)
    public JsonResult addSectionCompare(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String caseId = jsonObject.getString("caseId");
        String sectionId = jsonObject.getString("sectionId");
        String fileName = jsonObject.getString("fileName");
        List<String> demList = jsonObject.getObject("demList", List.class);
        return ResultUtils.success(analysisService.addSectionCompare(caseId, sectionId, email, demList, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/addSectionFlush", method = RequestMethod.POST)
    public JsonResult addSectionFlush(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String caseId = jsonObject.getString("caseId");
        String sectionId = jsonObject.getString("sectionId");
        String benchmarkId = jsonObject.getString("benchmarkId");
        String referId = jsonObject.getString("referId");
        String fileName = jsonObject.getString("fileName");
        return ResultUtils.success(analysisService.addSectionFlush(caseId, sectionId, benchmarkId, referId, email, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/addRegionFlush", method = RequestMethod.POST)
    public JsonResult addRegionFlush(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String caseId = jsonObject.getString("caseId");
        String regionId = jsonObject.getString("regionId");
        String benchmarkId = jsonObject.getString("benchmarkId");
        String referId = jsonObject.getString("referId");
        String fileName = jsonObject.getString("fileName");
        return ResultUtils.success(analysisService.addRegionFlush(caseId, regionId, benchmarkId, referId, email, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/computeVolume", method = RequestMethod.POST)
    public JsonResult computeVolume(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        Double deep = jsonObject.getDouble("deep");
        String caseId = jsonObject.getString("caseId");
        String regionId = jsonObject.getString("regionId");
        String demId = jsonObject.getString("demId");
        String fileName = jsonObject.getString("fileName");
        return ResultUtils.success(analysisService.computeVolume(deep, caseId, regionId, demId, email, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/addElevationFlush", method = RequestMethod.POST)
    public JsonResult addElevationFlush(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String caseId = jsonObject.getString("caseId");
        String benchmarkId = jsonObject.getString("benchmarkId");
        String referId = jsonObject.getString("referId");
        String fileName = jsonObject.getString("fileName");
        return ResultUtils.success(analysisService.addElevationFlush(caseId, benchmarkId, referId, email, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/addFlushContour", method = RequestMethod.POST)
    public JsonResult addFlushContour(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String caseId = jsonObject.getString("caseId");
        String benchmarkId = jsonObject.getString("benchmarkId");
        String referId = jsonObject.getString("referId");
        String fileName = jsonObject.getString("fileName");
        return ResultUtils.success(analysisService.addFlushContour(caseId, benchmarkId, referId, email, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/addSlope", method = RequestMethod.POST)
    public JsonResult addSlope(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String caseId = jsonObject.getString("caseId");
        String demId = jsonObject.getString("demId");
        String fileName = jsonObject.getString("fileName");
        return ResultUtils.success(analysisService.addSlope(caseId, demId, email, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/checkState/{key}", method = RequestMethod.GET)
    public JsonResult checkState(@PathVariable String key) {
        return ResultUtils.success(analysisService.checkState(key));
    }

    @AuthCheck
    @RequestMapping(value = "/rename", method = RequestMethod.PATCH)
    public JsonResult rename(@RequestBody JSONObject jsonObject) {
        analysisService.rename(jsonObject.getString("id"), jsonObject.getString("name"));
        return ResultUtils.success();
    }

    @RequestMapping(value = "/downloadAnalysisResult/{id}", method = RequestMethod.GET)
    public void downloadAnalysisResult(@PathVariable String id, HttpServletResponse response) {
        analysisService.downloadAnalysisResult(id, response);
    }


    @RequestMapping(value = "/subscribe/{id}", method = RequestMethod.GET)
    public SseEmitter subscribe(@PathVariable String id) throws IOException {
        return analysisService.subscribe(id);
    }

    @AuthCheck
    @RequestMapping(value = "/over/{id}", method = RequestMethod.GET)
    public JsonResult over(@PathVariable String id, @JwtTokenParser("email") String email) throws IOException {
        analysisService.over(id, email);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public JsonResult test() {
        return ResultUtils.success(analysisService.test());
    }

}
