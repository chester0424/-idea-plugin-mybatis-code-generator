package zone.pusu.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zone.pusu.product.dao.IProductDao;

import java.util.List;

@Component
public class ProductMgtSvr {

    @Autowired
    IProductDao productDao;

    public int add(Product product) {
        return productDao.insert(product);
    }

    public int add(List<Product> products) {
        return productDao.insertBatch(products);
    }

    public Product get(String id) {
        return productDao.get(id);
    }

    public List<Product> getList(ProductQueryParam param, int pageIndex, int pageSize) {
        if(param==null){
            param = new ProductQueryParam();
        }
        param.setOrderBy(ProductQueryParam.OrderBy.name_desc, ProductQueryParam.OrderBy.id_asc);
        return productDao.getList(param, pageIndex, pageSize);
    }

    public int getCount(ProductQueryParam param) {
        return productDao.getCount(param);
    }

    public void update(Product product) {
        productDao.update(product);
    }

    public int delete(String id) {
        return productDao.delete(id);
    }

}
