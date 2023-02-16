package zone.pusu.product;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.math.BigDecimal;
import java.util.Date;
import java.lang.Boolean;
import java.lang.String;

/**
 * Product Dao
 *
 * @author 
 * @since 2023-02-16 20:54:54
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

    public void setBigDecimal(BigDecimal bigDecimal) {
        condition.put("bigDecimal", bigDecimal);
    }

    public void setBigDecimalRange(BigDecimal bigDecimalBegin, BigDecimal bigDecimalEnd) {
        condition.put("bigDecimal_range", new HashMap<String, Object>() {{
            put("begin", bigDecimalBegin);
            put("end", bigDecimalEnd);
        }});
    }

    public void setABooleanWrap(Boolean aBooleanWrap) {
        condition.put("aBooleanWrap", aBooleanWrap);
    }

    public void setDate(Date date) {
        condition.put("date", date);
    }

    public void setDateRange(Date dateBegin, Date dateEnd) {
        condition.put("date_range", new HashMap<String, Object>() {{
            put("begin", dateBegin);
            put("end", dateEnd);
        }});
    }

    public void setAnInt(int anInt) {
        condition.put("anInt", anInt);
    }

    public void setAnIntRange(int anIntBegin, int anIntEnd) {
        condition.put("anInt_range", new HashMap<String, Object>() {{
            put("begin", anIntBegin);
            put("end", anIntEnd);
        }});
    }

    public void setABoolean(boolean aBoolean) {
        condition.put("aBoolean", aBoolean);
    }

    public void setEf(char ef) {
        condition.put("ef", ef);
    }

    public void setEfArray(char... efArray) {
        condition.put("ef_array", efArray);
    }

    public void setOrderBy(OrderBy... orders) {
        condition.put("order_array", orders);
    }

    public enum OrderBy {
        id_asc,
        id_desc,
        name_asc,
        name_desc,
        bigDecimal_asc,
        bigDecimal_desc,
        aBooleanWrap_asc,
        aBooleanWrap_desc,
        date_asc,
        date_desc,
        anInt_asc,
        anInt_desc,
        aBoolean_asc,
        aBoolean_desc,
        ef_asc,
        ef_desc;
    }
}