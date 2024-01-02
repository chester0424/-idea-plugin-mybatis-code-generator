package zone.pusu.product.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import zone.pusu.product.Product;
import zone.pusu.product.ProductQueryParam;

import java.util.List;
import java.util.Map;


/**
* Product Dao
*
* @author chester
* @since 2024-01-02 16:24:09
*/
@Mapper
public interface ProductDao {

    int insert(Product product);

    int insertBatch(@Param("coll")List<Product> productList);

    int update(Product product);

    int updateDyn(@Param("id") String id, @Param("keyValues") Map<String, Object> values);

    int delete(@Param("id") String id);

    Product get(@Param("id") String id);

    List<Product> getList(ProductQueryParam param);

    List<Product> getList(ProductQueryParam param, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    int getCount(ProductQueryParam param);
}


