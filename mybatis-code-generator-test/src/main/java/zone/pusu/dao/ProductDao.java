package zone.pusu.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import zone.pusu.Product;

@Mapper
interface IProductDao {

    Product get(String key);

    Page<Product> getProductPageList(String key,@Param("pageNumKey") int pageNum,  @Param("pageSizeKey") int pageSize);

    int Update(Product product);

    int delete(String key);
}