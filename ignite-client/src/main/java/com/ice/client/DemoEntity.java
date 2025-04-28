package com.ice.client;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

public class DemoEntity implements Serializable {
    @QuerySqlField(index = true)
    private Integer id;
    @QuerySqlField(index = true)
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
