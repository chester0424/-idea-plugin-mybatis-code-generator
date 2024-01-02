package zone.pusu.product;

import java.util.HashMap;

import java.math.BigDecimal;
import java.lang.String;

/**
 * Product Dao
 *
 * @author chester
 * @since 2024-01-02 16:24:09
 */
public class ProductQueryParam extends HashMap<String, Object> {
    public static final String field_id="id";

    public static final String field_name="name";

    public static final String field_code="code";

    public static final String field_weight="weight";

    public static final String field_placeOfProduction="placeOfProduction";

    public static final String field_price="price";


    public void setId(String id) {
        put("id", id);
    }

    public void setIdArray(String... idArray) {
        put("id_array", idArray);
    }

    public void setName(String name) {
        put("name", name);
    }

    public void setNameArray(String... nameArray) {
        put("name_array", nameArray);
    }

    public void setCode(String code) {
        put("code", code);
    }

    public void setCodeArray(String... codeArray) {
        put("code_array", codeArray);
    }

    public void setWeight(BigDecimal weight) {
        put("weight", weight);
    }

    public void setWeightRange(BigDecimal weightBegin, BigDecimal weightEnd) {
        put("weight_range", new HashMap<String, Object>() {{
            put("begin", weightBegin);
            put("end", weightEnd);
        }});
    }

    public void setPlaceOfProduction(String placeOfProduction) {
        put("placeOfProduction", placeOfProduction);
    }

    public void setPlaceOfProductionArray(String... placeOfProductionArray) {
        put("placeOfProduction_array", placeOfProductionArray);
    }

    public void setPrice(BigDecimal price) {
        put("price", price);
    }

    public void setPriceRange(BigDecimal priceBegin, BigDecimal priceEnd) {
        put("price_range", new HashMap<String, Object>() {{
            put("begin", priceBegin);
            put("end", priceEnd);
        }});
    }

    public void setOrderBy(OrderBy... orders) {
        put("order_array", orders);
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

