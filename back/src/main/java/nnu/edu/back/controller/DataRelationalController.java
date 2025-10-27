package nnu.edu.back.controller;

import com.alibaba.fastjson2.JSONObject;
import nnu.edu.back.common.result.JsonResult;
import nnu.edu.back.common.result.ResultUtils;
import nnu.edu.back.service.DataRelationalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/20:45
 * @Description:
 */
@RestController
@RequestMapping("/relational")
public class DataRelationalController {
    @Autowired
    DataRelationalService dataRelationalService;

    @RequestMapping(value = "/addRelational", method = RequestMethod.POST)
    public JsonResult addRelational(@RequestBody JSONObject jsonObject) {
        String dataListId = jsonObject.getString("dataListId");
        List<String> fileIdList = jsonObject.getObject("fileIdList", List.class);
        dataRelationalService.addRelational(dataListId, fileIdList);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/updateRelational", method = RequestMethod.PATCH)
    public JsonResult updateRelational(@RequestBody JSONObject jsonObject) {
        String dataListId = jsonObject.getString("dataListId");
        List<String> fileIdList = jsonObject.getObject("fileIdList", List.class);
        dataRelationalService.updateRelational(dataListId, fileIdList);
        return ResultUtils.success();
    }
}
