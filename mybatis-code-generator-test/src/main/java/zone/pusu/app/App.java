package zone.pusu.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import zone.pusu.product.Product;
import zone.pusu.product.ProductMgtSvr;
import zone.pusu.product.ProductQueryParam;

import java.util.List;
import java.util.UUID;

/**
 * Hello world!
 */
@SpringBootApplication
@MapperScan(basePackages = "zone.pusu")
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        ConfigurableApplicationContext ac = SpringApplication.run(App.class, args);
        ProductMgtSvr productMgtSvr = ac.getBean(ProductMgtSvr.class);

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName("书桌");
//        product.setAmount(new BigDecimal(1000));
//        product.setGoodWord(true);
//        product.setContent("内容".getBytes());
//        product.setCreateAt(new Date());
        productMgtSvr.add(product);

        product = productMgtSvr.get(product.getId());

        product.setName("电脑");
        productMgtSvr.update(product);

        ProductQueryParam param = new ProductQueryParam();
        param.setName("电%");
        List<Product> productList = productMgtSvr.getList(param, 0, 30);
        int count = productMgtSvr.getCount(param);

        productMgtSvr.delete(product.getId());
    }
}
