package zone.pusu.product;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Product {
    private String id;
    private String name;
    private BigDecimal bigDecimal;
    private Boolean aBooleanWrap;
    private byte[] bytes;
    private Date date;
    private int anInt;
    private boolean aBoolean;
    private char ef = 'e';
    private List<String> test;
    private List<ArrayList<String>> list;

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

}