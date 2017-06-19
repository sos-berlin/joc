
package com.sos.joc.model.yade;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum Protocol {

    LOCAL("LOCAL"),
    FTP("FTP"),
    FTPS("FTPS"),
    SFTP("SFTP"),
    HTTP("HTTP"),
    HTTPS("HTTPS"),
    WEBDAV("WEBDAV"),
    WEBDAVS("WEBDAVS"),
    SMB("SMB");
    private final String value;
    private final static Map<String, Protocol> CONSTANTS = new HashMap<String, Protocol>();

    static {
        for (Protocol c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private Protocol(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static Protocol fromValue(String value) {
        Protocol constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
