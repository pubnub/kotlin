package com.pubnub.api.matchers;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class JSONAssert {

    public static void assertJSONArrayHasNo(String item, JSONArray jArray) {
        assertFalse("JSONArray should not contain item \"" + item + "\"", listData(jArray).contains(item));
    }
    public static void assertJSONArrayHas(String item, JSONArray jArray) {
        assertTrue("JSONArray should contain item \"" + item + "\"", listData(jArray).contains(item));
    }

    private static ArrayList listData(JSONArray jArray) {
        ArrayList<String> listData = new ArrayList<String>();

        if (jArray == null) {
            fail("JSON decoding error");
            jArray = new JSONArray();
        }

        for (int i = 0; i < jArray.length(); i++) {
            try {
                listData.add(jArray.get(i).toString());
            } catch (JSONException e) {
                fail("JSON decoding error");
            }
        }

        return listData;
    }
}
