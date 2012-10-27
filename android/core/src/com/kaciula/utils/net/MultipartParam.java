
package com.kaciula.utils.net;

/**
 * MultipartParam.java - Object used in post requests
 * 
 * @author ka
 */
public class MultipartParam {

    public String key;

    public String value;

    public ParamType type;

    public MultipartParam(String key, String value, ParamType type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public enum ParamType {
        STRING, FILE
    }
}
