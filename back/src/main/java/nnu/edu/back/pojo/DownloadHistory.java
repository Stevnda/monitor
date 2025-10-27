package nnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/19:26
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadHistory {
    String id;
    String userId;
    Date downloadTime;
    String dataListId;
    String fileId;
}
