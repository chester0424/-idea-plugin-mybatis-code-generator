package zone.pusu.product;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.math.BigDecimal;
import java.util.List;
import java.lang.String;

/**
 * Product Dao
 *
 * @author 
 * @since 2023-08-01 11:53:25
 */
public class ProductQueryParam {
private Map<String, Object> condition = new LinkedHashMap<>();

    public void setId(String id) {
        condition.put("id", id);
    }

    public void setIdArray(String... idArray) {
        condition.put("id_array", idArray);
    }

    public void setName(String name) {
        condition.put("name", name);
    }

    public void setNameArray(String... nameArray) {
        condition.put("name_array", nameArray);
    }

    public void setCode(String code) {
        condition.put("code", code);
    }

    public void setCodeArray(String... codeArray) {
        condition.put("code_array", codeArray);
    }

    public void setWeight(BigDecimal weight) {
        condition.put("weight", weight);
    }

    public void setWeightRange(BigDecimal weightBegin, BigDecimal weightEnd) {
        condition.put("weight_range", new HashMap<String, Object>() {{
            put("begin", weightBegin);
            put("end", weightEnd);
        }});
    }

    public void setPlaceOfProduction(String placeOfProduction) {
        condition.put("placeOfProduction", placeOfProduction);
    }

    public void setPlaceOfProductionArray(String... placeOfProductionArray) {
        condition.put("placeOfProduction_array", placeOfProductionArray);
    }

    public void setPrice(BigDecimal price) {
        condition.put("price", price);
    }

    public void setPriceRange(BigDecimal priceBegin, BigDecimal priceEnd) {
        condition.put("price_range", new HashMap<String, Object>() {{
            put("begin", priceBegin);
            put("end", priceEnd);
        }});
    }

    public void setOrderBy(OrderBy... orders) {
        condition.put("order_array", orders);
    }

    public enum OrderBy {
        id_asc,
        id_desc,
        name_asc,
        name_desc,
        code_asc,
        code_desc,
        weight_asc,
        weight_desc,
        placeOfProduction_asc,
        placeOfProduction_desc,
        price_asc,
        price_desc;
    }
}

