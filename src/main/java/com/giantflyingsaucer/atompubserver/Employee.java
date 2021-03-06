/*
 * Original code created by the Apache Abdera team
 * http://abdera.apache.org/
 */
package com.giantflyingsaucer.atompubserver;

import java.util.Date;


public class Employee {
    private int id;
    private String name;
    private Date updated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }    
}