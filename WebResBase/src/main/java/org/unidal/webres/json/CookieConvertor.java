package org.unidal.webres.json;

import java.text.ParseException;

/**
 * Convert a web browser cookie specification to a JSONObject and back.
 * JSON and Cookies are both notations for name/value pairs.
 * @author JSON.org
 * @version 0.1
 */
public class CookieConvertor {

    /**
     * Produce a copy of a string in which the characters '+', '%', '=', ';'
     * and control characters are replaced with "%hh". This is a gentle form
     * of URL encoding, attempting to cause as little distortion to the
     * string as possible. The characters '=' and ';' are meta characters in
     * cookies. By convention, they are escaped using the URL-encoding. This is
     * only a convention, not a standard. Often, cookies are expected to have
     * encoded values. We encode '=' and ';' because we must. We encode '%' and
     * '+' because they are meta characters in URL encoding.
     * @param string The source string.
     * @return       The escaped result.
     */
    public static String escape(String string) {
        char         c;
        String       s = string.trim();
        StringBuffer sb = new StringBuffer();
        int          len = s.length();
        for (int i = 0; i < len; i += 1) {
            c = s.charAt(i);
            if (c < ' ' || c == '+' || c == '%' || c == '=' || c == ';') {
                sb.append('%');
                sb.append(Character.forDigit((char)((c >>> 4) & 0x0f), 16));
                sb.append(Character.forDigit((char)(c & 0x0f), 16));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    /**
     * Convert a cookie specification string into a JSONObject. The string
     * will contain a name value pair separated by '='. The name and the value
     * will be unescaped, possibly converting '+' and '%' sequences. The
     * cookie properties may follow, separated by ';', also represented as
     * name=value (except the secure property, which does not have a value).
     * The name will be stored under the key "name", and the value will be
     * stored under the key "value". This method does not do checking or
     * validation of the parameters. It only converts the cookie string into
     * a JSONObject.
     * @param string The cookie specification string.
     * @return A JSONObject containing "name", "value", and possibly other
     *  members.
     * @throws ParseException
     */
    public static JsonObject toJSONObject(String string) throws ParseException {
        String         n;
        JsonObject     o = new JsonObject();
        Object         v;
        JsonTokener x = new JsonTokener(string);
        o.put("name", x.nextTo('='));
        x.next('=');
        o.put("value", x.nextTo(';'));
        x.next();
        while (x.more()) {
            n = JsonTokener.unescape(x.nextTo("=;"));
            if (x.next() != '=') {
                if (n.equals("secure")) {
                    v = Boolean.TRUE;
                } else {
                    throw x.syntaxError("Missing '=' in cookie parameter.");
                }
            } else {
                v = JsonTokener.unescape(x.nextTo(';'));
                x.next();
            }
            o.put(n, v);
        }
        return o;
    }


    /**
     * Convert a JSONObject into a cookie specification string. The JSONObject
     * must contain "name" and "value" members.
     * If the JSONObject contains "expires", "domain", "path", or "secure"
     * members, they will be appended to the cookie specification string.
     * All other members are ignored.
     * @param o A JSONObject
     * @return A cookie specification string
     */
    public static String toString(JsonObject o) {
        StringBuffer sb = new StringBuffer();

        sb.append(escape(o.getString("name")));
        sb.append("=");
        sb.append(escape(o.getString("value")));
        if (o.has("expires")) {
            sb.append(";expires=");
            sb.append(o.getString("expires"));
        }
        if (o.has("domain")) {
            sb.append(";domain=");
            sb.append(escape(o.getString("domain")));
        }
        if (o.has("path")) {
            sb.append(";path=");
            sb.append(escape(o.getString("path")));
        }
        if (o.optBoolean("secure")) {
            sb.append(";secure");
        }
        return sb.toString();
    }
}
