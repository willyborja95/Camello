package com.apptec.camello.util;

import com.apptec.camello.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * A class that returns the corresponding resource according to the server code error
 */
public class ErrorDictionary {

    private static HashMap<Integer, Integer> errorMessages = new HashMap<Integer, Integer>() {{
        put(400, R.string.server_error_400);
        put(300, R.string.server_error_300);
        put(301, R.string.server_error_301);
        put(302, R.string.server_error_302);
        put(303, R.string.server_error_303);
        put(304, R.string.server_error_304);
        put(501, R.string.server_error_501);
        put(502, R.string.server_error_502);
        put(404, R.string.server_error_404);
        put(403, R.string.server_error_403);
        put(600, R.string.server_error_600);
        put(601, R.string.server_error_601);
        put(700, R.string.server_error_700);
        put(701, R.string.server_error_701);
        put(801, R.string.server_error_801);
        put(800, R.string.server_error_800);
        put(802, R.string.server_error_802);
        put(803, R.string.server_error_803);
        put(805, R.string.server_error_805);
        put(806, R.string.server_error_806);
        put(804, R.string.server_error_804);
        put(900, R.string.server_error_900);
        put(0, R.string.unknown_error_message);
    }};


    public static int getErrorMessageByCode(@NotNull int server_code) {
        if (errorMessages.get(server_code) != null) {
            return errorMessages.get(server_code);
        }
        return errorMessages.get(0);

    }


}
