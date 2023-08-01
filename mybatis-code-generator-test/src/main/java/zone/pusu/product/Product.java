package zone.pusu.product;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品
 */
public class Product {

//    /**
//     * 属性
//     */
//    private String[] properties;
//
//    private List<String> test1;

    /**
     * 商品ID
     */
    private String id;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品编码
     */
    private String code;
    /**
     * 商品重量
     */
    private BigDecimal weight;
    /**
     * 产地
     */
    private String placeOfProduction;
    /**
     * 商品价格
     */
    private BigDecimal price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getPlaceOfProduction() {
        return placeOfProduction;
    }

    public void setPlaceOfProduction(String placeOfProduction) {
        this.placeOfProduction = placeOfProduction;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}