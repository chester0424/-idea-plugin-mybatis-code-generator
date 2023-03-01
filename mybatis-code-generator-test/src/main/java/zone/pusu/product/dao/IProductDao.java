package zone.pusu.product.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import zone.pusu.product.Product;
import zone.pusu.product.ProductQueryParam;

import java.util.List;


/**
* Product Dao
*
* @author chester
* @since 2023-03-01 21:54:25
*/
@Mapper
public interface IProductDao {

    int insert(Product product);

    int insertBatch(@Param("coll")List<Product> productList);

    int update(Product product);

    int delete(@Param("id") String id);

    Product get(@Param("id") String id);

    List<Product> getList(ProductQueryParam param, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    int getCount(ProductQueryParam param);
}


