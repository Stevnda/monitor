package nnu.edu.back.dao.shp;

import nnu.edu.back.pojo.TileBox;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/15/19:10
 * @Description:
 */
@Repository
public interface VectorTileMapper {
    Object getVictorTile(TileBox tileBox);

    Object getVictorTile3D(TileBox tileBox);
}
