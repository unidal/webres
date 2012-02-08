package org.unidal.webres.json;

import java.text.ParseException;
import java.util.HashMap;

/**
 * The XMLTokener extends the JSONTokener to provide additional methods
 * for the parsing of XML texts.
 * @author JSON.org
 * @version 0.1
 */
public class XmlTokener extends JsonTokener {


   /** The table of entity values. It initially contains Character values for
    * amp, apos, gt, lt, quot.
    */
   public static final HashMap<String, Character> entity;

   static {
       entity = new HashMap<String, Character>(8);
       entity.put("amp",  XmlConvertor.AMP);
       entity.put("apos", XmlConvertor.APOS);
       entity.put("gt",   XmlConvertor.GT);
       entity.put("lt",   XmlConvertor.LT);
       entity.put("quot", XmlConvertor.QUOT);
   }

    /**
     * Construct an XMLTokener from a string.
     * @param s A source string.
     */
    public XmlTokener(String s) {
        super(s);
    }


    /**
     * Get the next XML outer token, trimming whitespace. There are two kinds
     * of tokens: the '<' character which begins a markup tag, and the content
     * text between markup tags.
     *
     * @return  A string, or a '<' Character, or null if there is no more
     * source text.
     * @throws ParseException
     */
    public Object nextContent() throws ParseException {
        char         c;
        StringBuffer sb;
        do {
            c = next();
        } while (Character.isWhitespace(c));
        if (c == 0) {
            return null;
        }
        if (c == '<') {
            return XmlConvertor.LT;
        }
        sb = new StringBuffer();
        while (true) {
            if (c == '<' || c == 0) {
                back();
                return sb.toString().trim();
            }
            if (c == '&') {
                sb.append(nextEntity(c));
            } else {
                sb.append(c);
            }
            c = next();
        }
    }


    /**
     * Return the next entity. These entities are translated to Characters:
     *     &amp;  &apos;  &gt;  &lt;  &quot;
     * @param a An ampersand character.
     * @return  A Character or an entity String if the entity is not recognized.
     * @throws ParseException Missing ';' in XML entity
     */
    public Object nextEntity(char a) throws ParseException {
        StringBuffer sb = new StringBuffer();
        while (true) {
            char c = next();
            if (Character.isLetter(c)) {
                sb.append(Character.toLowerCase(c));
            } else if (c == ';') {
                break;
            } else {
                throw syntaxError("Missing ';' in XML entity: &" + sb);
            }
        }
        String s = sb.toString();
        Object e = entity.get(s);
        return e != null ? e : a + s + ";";
    }


    /**
     * Returns the next XML meta token. This is used for skipping over <!...>
     * and <?...?> structures.
     * @return Syntax characters (< > / = ! ?) are returned as Character, and
     * strings and names are returned as Boolean. We don't care what the
     * values actually are.
     * @throws ParseException
     */
    public Object nextMeta() throws ParseException {
        char c;
        char q;
        do {
            c = next();
        } while (Character.isWhitespace(c));
        switch (c) {
        case 0:
            throw syntaxError("Misshaped meta tag.");
        case '<':
            return XmlConvertor.LT;
        case '>':
            return XmlConvertor.GT;
        case '/':
            return XmlConvertor.SLASH;
        case '=':
            return XmlConvertor.EQ;
        case '!':
            return XmlConvertor.BANG;
        case '?':
            return XmlConvertor.QUEST;
        case '"':
        case '\'':
            q = c;
            while (true) {
                c = next();
                if (c == 0) {
                    throw syntaxError("Unterminated string.");
                }
                if (c == q) {
                    return Boolean.TRUE;
                }
            }
        default:
            while (true) {
                c = next();
                if (Character.isWhitespace(c)) {
                    return Boolean.TRUE;
                }
                switch (c) {
                case 0:
                case '<':
                case '>':
                case '/':
                case '=':
                case '!':
                case '?':
                case '"':
                case '\'':
                    back();
                    return Boolean.TRUE;
                }
            }
        }
    }


    /**
     * Get the next XML Token. These tokens are found inside of angle
     * brackets. It may be one of these characters: / > = ! ? or it may be a
     * string wrapped in single quotes or double quotes, or it may be a name.
     * @return a String or a Character.
     * @throws ParseException
     */
    public Object nextToken() throws ParseException {
        char c;
        char q;
        StringBuffer sb;
        do {
            c = next();
        } while (Character.isWhitespace(c));
        switch (c) {
        case 0:
            throw syntaxError("Misshaped element.");
        case '<':
            throw syntaxError("Misplaced '<'.");
        case '>':
            return XmlConvertor.GT;
        case '/':
            return XmlConvertor.SLASH;
        case '=':
            return XmlConvertor.EQ;
        case '!':
            return XmlConvertor.BANG;
        case '?':
            return XmlConvertor.QUEST;

// Quoted string

        case '"':
        case '\'':
            q = c;
            sb = new StringBuffer();
            while (true) {
                c = next();
                if (c == 0) {
                    throw syntaxError("Unterminated string.");
                }
                if (c == q) {
                    return sb.toString();
                }
                if (c == '&') {
                    sb.append(nextEntity(c));
                } else {
                    sb.append(c);
                }
            }
        default:

// Name

            sb = new StringBuffer();
            while (true) {
                sb.append(c);
                c = next();
                if (Character.isWhitespace(c)) {
                    return sb.toString();
                }
                switch (c) {
                case 0:
                case '>':
                case '/':
                case '=':
                case '!':
                case '?':
                    back();
                    return sb.toString();
                case '<':
                case '"':
                case '\'':
                    throw syntaxError("Bad character in a name.");
                }
            }
        }
    }
}
