package nnu.edu.back.controller;

import nnu.edu.back.common.auth.AuthCheck;
import nnu.edu.back.common.result.JsonResult;
import nnu.edu.back.common.result.ResultUtils;
import nnu.edu.back.service.DownloadHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/20:06
 * @Description:
 */
@RestController
@RequestMapping("/downloadHistory")
public class DownloadHistoryController {

    @Autowired
    DownloadHistoryService downloadHistoryService;

    @AuthCheck
    @RequestMapping(value = "/pageQuery/{dataListId}/{size}/{page}", method = RequestMethod.GET)
    public JsonResult pageQuery(@PathVariable int size, @PathVariable int page, @PathVariable String dataListId) {
        return ResultUtils.success(downloadHistoryService.pageQuery(size, page, dataListId));
    }

}
