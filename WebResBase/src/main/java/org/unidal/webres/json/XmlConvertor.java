package org.unidal.webres.json;

import java.text.ParseException;
import java.util.Iterator;


/**
 * This provides static methods to convert an XML text into a JSONObject,
 * and to covert a JSONObject into an XML text.
 * @author JSON.org
 * @version 0.1
 */
public class XmlConvertor {

    /** The Character '&'. */
    public static final Character AMP   = new Character('&');

    /** The Character '''. */
    public static final Character APOS  = new Character('\'');

    /** The Character '!'. */
    public static final Character BANG  = new Character('!');

    /** The Character '='. */
    public static final Character EQ    = new Character('=');

    /** The Character '>'. */
    public static final Character GT    = new Character('>');

    /** The Character '<'. */
    public static final Character LT    = new Character('<');

    /** The Character '?'. */
    public static final Character QUEST = new Character('?');

    /** The Character '"'. */
    public static final Character QUOT  = new Character('"');

    /** The Character '/'. */
    public static final Character SLASH = new Character('/');

    /**
     * Replace special characters with XML escapes:
     * <pre>
     * &amp; is replaced by &amp;amp;
     * &lt; is replaced by &amp;lt;
     * &gt; is replaced by &amp;gt;
     * &quot; is replaced by &amp;quot;
     * </pre>
     * @param string The string to be escaped.
     * @return The escaped string.
     */
    public static String escape(String string) {
    	if (string.indexOf("&") == -1 &&
			string.indexOf("<") == -1 &&
			string.indexOf(">") == -1 &&
			string.indexOf("\"") == -1) {
			return string;
		}
		
		int size = string.length();
		StringBuffer sb = new StringBuffer(size + 20);
		for (int i = 0; i < size; i++) {
			char c = string.charAt(i);
			switch (c) {
				case '&':
					sb.append("&amp;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '\"':
					sb.append("&quot;");
					break;
				default:
					sb.append(c);
					break;
			}
		}
        return sb.toString();
    }

    /**
     * Scan the content following the named tag, attaching it to the context.
     * @param x       The XMLTokener containing the source string.
     * @param context The JSONObject that will include the new material.
     * @param name    The tag name.
     * @return true if the close tag is processed.
     * @throws ParseException
     */
    private static boolean parse(XmlTokener x, JsonObject context,
                                 String name) throws ParseException {
        char       c;
        int        i;
        String     n;
        JsonObject o;
        String     s;
        Object     t;

// Test for and skip past these forms:
//      <!-- ... -->
//      <!   ...   >
//      <![  ... ]]>
//      <?   ...  ?>
// Report errors for these forms:
//      <>
//      <=
//      <<

        t = x.nextToken();

// <!

        if (t == BANG) {
            c = x.next();
            if (c == '-') {
                if (x.next() == '-') {
                    x.skipPast("-->");
                    return false;
                }
                x.back();
            } else if (c == '[') {
                x.skipPast("]]>");
                return false;
            }
            i = 1;
            do {
                t = x.nextMeta();
                if (t == null) {
                    throw x.syntaxError("Missing '>' after '<!'.");
                } else if (t == LT) {
                    i += 1;
                } else if (t == GT) {
                    i -= 1;
                }
            } while (i > 0);
            return false;
        } else if (t == QUEST) {

// <?

            x.skipPast("?>");
            return false;
        } else if (t == SLASH) {

// Close tag </

            if (name == null || !x.nextToken().equals(name)) {
                throw x.syntaxError("Mismatched close tag");
            }
            if (x.nextToken() != GT) {
                throw x.syntaxError("Misshaped close tag");
            }
            return true;

        } else if (t instanceof Character) {
            throw x.syntaxError("Misshaped tag");

// Open tag <

        } else {
            n = (String)t;
            t = null;
            o = new JsonObject();
            while (true) {
                if (t == null) {
                    t = x.nextToken();
                }

// attribute = value

                if (t instanceof String) {
                    s = (String)t;
                    t = x.nextToken();
                    if (t == EQ) {
                        t = x.nextToken();
                        if (!(t instanceof String)) {
                            throw x.syntaxError("Missing value");
                        }
                        o.accumulate(s, t);
                        t = null;
                    } else {
                        o.accumulate(s, Boolean.TRUE);
                    }

// Empty tag <.../>

                } else if (t == SLASH) {
                    if (x.nextToken() != GT) {
                        throw x.syntaxError("Misshaped tag");
                    }
                    if (o.length() == 0) {
                        context.accumulate(n, Boolean.TRUE);
                    } else {
                        context.accumulate(n, o);
                    }
                    return false;

// Content, between <...> and </...>

                } else if (t == GT) {
                    while (true) {
                        t = x.nextContent();
                        if (t == null) {
                            if (name != null) {
                                throw x.syntaxError("Unclosed tag " + name);
                            }
                            return false;
                        } else if (t instanceof String) {
                            s = (String)t;
                            if (s.length() > 0) {
                                o.accumulate("content", s);
                            }

// Nested element

                        } else if (t == LT) {
                            if (parse(x, o, n)) {
                                if (o.length() == 0) {
                                    context.accumulate(n, Boolean.TRUE);
                                } else if (o.length() == 1 &&
                                           o.opt("content") != null) {
                                    context.accumulate(n, o.opt("content"));
                                } else {
                                    context.accumulate(n, o);
                                }
                                return false;
                            }
                        }
                    }
                } else {
                    throw x.syntaxError("Misshaped tag");
                }
            }
        }
    }


    /**
     * Convert a well-formed (but not necessarily valid) XML string into a
     * JSONObject. Some information may be lost in this transformation
     * because JSON is a data format and XML is a document format. XML uses
     * elements, attributes, and content text, while JSON uses unordered
     * collections of name/value pairs and arrays of values. JSON does not
     * does not like to distinguish between elements and attributes.
     * Sequences of similar elements are represented as JSONArrays. Content
     * text may be placed in a "content" member. Comments, prologs, DTDs, and
     * <code>&lt;[ [ ]]></code> are ignored.
     * @param string The source string.
     * @return A JSONObject containing the structured data from the XML string.
     * @throws ParseException
     */
    public static JsonObject toJSONObject(String string) throws ParseException {
        JsonObject o = new JsonObject();
        XmlTokener x = new XmlTokener(string);
        while (x.more()) {
            x.skipPast("<");
            parse(x, o, null);
        }
        return o;
    }


    /**
     * Convert a JSONObject into a well-formed XML string.
     * @param o A JSONObject.
     * @return A string.
     */
    public static String toString(Object o) {
        return toString(o, null);
    }


    /**
     * Convert a JSONObject into a well-formed XML string.
     * @param o A JSONObject.
     * @param tagName The optional name of the enclosing tag.
     * @return A string.
     */
    public static String toString(Object o, String tagName) {
        StringBuffer a = null; // attributes, inside the <...>
        StringBuffer b = new StringBuffer(); // body, between <...> and </...>
        int          i;
        JsonArray    ja;
        JsonObject   jo;
        String       k;
        Iterator<String>     keys;
        int          len;
        String       s;
        Object       v;
        if (o instanceof JsonObject) {

// Emit <tagName

            if (tagName != null) {
                a = new StringBuffer();
                a.append('<');
                a.append(tagName);
            }

// Loop thru the keys. Some keys will produce attribute material, others
// body material.

            jo = (JsonObject)o;
            keys = jo.keys();
            while (keys.hasNext()) {
                k = keys.next().toString();
                v = jo.get(k);
                if (v instanceof String) {
                    s = (String)v;
                } else {
                    s = null;
                }

// Emit a new tag <k... in body

                if (tagName == null || v instanceof JsonObject ||
                        (s != null && k != "content" && (s.length() > 60 ||
                        (s.indexOf('"') >= 0 && s.indexOf('\'') >= 0)))) {
                    b.append(toString(v, k));

// Emit content in body

                } else if (k.equals("content")) {
                    b.append(escape(v.toString()));

// Emit an array of similar keys in body

                } else if (v instanceof JsonArray) {
                    ja = (JsonArray)v;
                    len = ja.length();
                    for (i = 0; i < len; i += 1) {
                        b.append(toString(ja.get(i), k));
                    }

// Emit an attribute

                } else {
                    a.append(' ');
                    a.append(k);
                    a.append('=');
                    a.append(toString(v));
                }
            }
            if (tagName != null) {

// Close an empty element

                if (b.length() == 0) {
                    a.append("/>");
                } else {

// Close the start tag and emit the body and the close tag

                    a.append('>');
                    a.append(b);
                    a.append("</");
                    a.append(tagName);
                    a.append('>');
                }
                return a.toString();
            }
            return b.toString();

// XML does not have good support for arrays. If an array appears in a place
// where XML is lacking, synthesize an <array> element.

        } else if (o instanceof JsonArray) {
            ja = (JsonArray)o;
            len = ja.length();
            for (i = 0; i < len; ++i) {
                b.append(toString(
                    ja.opt(i), (tagName == null) ? "array" : tagName));
            }
            return b.toString();
        } else {
            s = (o == null) ? "null" : escape(o.toString());
            return (tagName == null) ?
                "\"" + s + "\"" :
                "<" + tagName + ">" + s + "</" + tagName + ">";
        }
    }
}