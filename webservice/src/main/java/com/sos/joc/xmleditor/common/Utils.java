package com.sos.joc.xmleditor.common;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public static List<Object> string2jsonList(String text) throws Exception {
        List<Object> list = new ArrayList<>();
        list.add(Utils.string2json(text));
        return list;
    }

    public static JsonArray string2json(String text) throws Exception {
        try {
            StringReader sr = null;
            JsonReader jr = null;
            try {
                sr = new StringReader(text);
                jr = Json.createReader(sr);
                return jr.readArray();

            } catch (Throwable e) {
                LOGGER.error(String.format("[%s]%s", text, e.toString()), e);
                throw e;
            } finally {
                if (jr != null) {
                    jr.close();
                }
                if (sr != null) {
                    sr.close();
                }
            }
        } catch (Exception ex) {

            throw ex;
        }
    }
}
