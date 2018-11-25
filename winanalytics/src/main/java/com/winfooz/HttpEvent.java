package com.winfooz;

/**
 * Project: WinAnalytics2 Created: November 23, 2018
 *
 * @author Mohamed Hamdan
 */
public class HttpEvent {

    private String packageName;
    private String className;
    private String method;
    private String name;
    private Class[] parameters;

    public HttpEvent(
            String packageName, String className, String method, String name, Class... parameters) {
        this.className = className;
        this.packageName = packageName;
        this.method = method;
        this.name = name;
        this.parameters = parameters;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class[] getParameters() {
        return parameters;
    }

    public void setParameters(Class[] parameters) {
        this.parameters = parameters;
    }
}
