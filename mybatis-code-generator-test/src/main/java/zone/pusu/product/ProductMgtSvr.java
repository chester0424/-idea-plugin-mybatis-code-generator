package zone.pusu.product;

import org.springframework.stereotype.Component;
import zone.pusu.product.dao.IProductDao;

import java.util.List;

@Component
public class ProductMgtSvr {

    IProductDao productDao;

    public ProductMgtSvr(IProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * 增加商品
     *
     * @param product
     * @return
     */
    public int add(Product product) {
        return productDao.insert(product);
    }

    /**
     * 批量增加商品
     *
     * @param products
     * @return
     */
    public int add(List<Product> products) {
        return productDao.insertBatch(products);
    }

    /**
     * 根据Id获取商品
     *
     * @param id
     * @return
     */
    public Product get(String id) {
        return productDao.get(id);
    }

    /**
     * 分页查询商品
     *
     * @param param
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<Product> getList(ProductQueryParam param, int pageIndex, int pageSize) {
        if (param == null) {
            param = new ProductQueryParam();
        }
        param.setOrderBy(ProductQueryParam.OrderBy.name_desc, ProductQueryParam.OrderBy.id_asc);
        return productDao.getList(param, pageIndex, pageSize);
    }

    /**
     * 查询数量
     *
     * @param param
     * @return
     */
    public int getCount(ProductQueryParam param) {
        return productDao.getCount(param);
    }

    /**
     * 更新商品
     *
     * @param product
     */
    public void update(Product product) {
        productDao.update(product);
    }

    /**
     * 删除商品
     *
     * @param id
     * @return
     */
    public int delete(String id) {
        return productDao.delete(id);
    }

}
