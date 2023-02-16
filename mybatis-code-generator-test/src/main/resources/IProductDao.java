package zone.pusu.product.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import zone.pusu.product.Product;
import zone.pusu.product.ProductQueryParam;

import java.util.List;

/**
 * Product Dao
 */
@Mapper
public interface IProductDao {

    int insert(Product product);

    int insertBatch(@Param("coll")List<Product> products);

    Product get(String key);

    List<Product> getList(ProductQueryParam param, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    int getCount(ProductQueryParam param);

    int update(Product product);

    int delete(String key);
}

