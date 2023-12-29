package zone.pusu.product.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import zone.pusu.product.Product;
import zone.pusu.product.ProductQueryParam;

import java.util.List;


/**
* Product Dao 商品
*
* @author 
* @since 2023-12-29 09:28:46
*/
@Mapper
public interface IProductDao {

    /**
     * 插入 商品
     *
     * @param product 商品
     * @return
     */
    int insert(Product product);

    /**
     * 批量插入 商品
     *
     * @param productList 商品 列表
     * @return
     */
    int insertBatch(@Param("coll")List<Product> productList);

    /**
     * 更新
     *
     * @param product 商品
     * @return
     */
    int update(Product product);

    /**
     * 删除
     *
     * @param id 标识符
     * @return
     */
    int delete(@Param("id") String id);

    /**
     * 获取
     *
     * @param id 标识符
     * @return
     */
    Product get(@Param("id") String id);

    /**
     * 获取列表
     *
     * @param param    查询条件
     * @param pageNum  页号
     * @param pageSize 页大小
     * @return
     */
    List<Product> getList(ProductQueryParam param, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    /**
     * 获取条数
     *
     * @param param 查询条件
     * @return
     */
    int getCount(ProductQueryParam param);
}


