package com.winfooz;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Project: WinAnalytics2 Created: November 23, 2018
 *
 * @author Mohamed Hamdan
 */
public class CallArgumentField {

    private Field field;
    private Object enclosingObject;
    private List<String> endpoints;
    private List<String> names;
    private String className;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Object getEnclosingObject() {
        return enclosingObject;
    }

    public void setEnclosingObject(Object enclosingObject) {
        this.enclosingObject = enclosingObject;
    }

    public List<String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
