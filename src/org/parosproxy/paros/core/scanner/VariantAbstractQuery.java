/*
 *
 * Paros and its related class files.
 * 
 * Paros is an HTTP/HTTPS proxy for assessing web application security.
 * Copyright (C) 2003-2004 Chinotec Technologies Company
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Clarified Artistic License
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Clarified Artistic License for more details.
 * 
 * You should have received a copy of the Clarified Artistic License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
// ZAP: 2012/03/15 Changed the methods (the private) setParameter and paramAppend to 
// use the class StringBuilder instead of StringBuffer. Removed unnecessary 
// castings in the method setParameter. Made a change in the method parse.
// ZAP: 2012/04/25 Added @Override annotation to all appropriate methods.
// ZAP: 2013/05/02 Re-arranged all modifiers into Java coding standard order
// ZAP: 2013/07/02 Changed Vector to ArrayList because obsolete and faster
// ZAP: 2013/08/21 Added a new encoding/decoding model for a correct parameter value interpretation

package org.parosproxy.paros.core.scanner;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.parosproxy.paros.network.HttpMessage;

public abstract class VariantAbstractQuery implements Variant {

    private static Pattern staticPatternParam = Pattern.compile("&", Pattern.CASE_INSENSITIVE);
    private List<NameValuePair> listParam = new ArrayList<>();

    public VariantAbstractQuery() {
    }

    /**
     * Build the message content setting the query string
     * according to the Variant specific implementation
     * 
     * @param msg the message object we need to modify
     * @param query the query string we need to set inside the message
     */
    protected abstract void buildMessage(HttpMessage msg, String query);

    /**
     * Return escaped mutate of the value. To be overridden by subclass.
     *
     * @param msg
     * @param value
     * @return the escaped value
     */
    protected abstract String getEscapedValue(HttpMessage msg, String value);

    /**
     * Return unescaped mutate of the value. To be overridden by subclass.
     * 
     * @param value
     * @return the unescaped value
     */
    protected abstract String getUnescapedValue(String value);

    /**
     * 
     * @param params 
     */
    protected void parse(String params) {

        if (params == null || params.equals("")) {
            return;
        }

        String[] keyValue = staticPatternParam.split(params);
        String key;
        String value;
        int pos;
        
        for (int i = 0; i < keyValue.length; i++) {
            value = null;
            pos = keyValue[i].indexOf('=');
            
            try {
                if (pos > 0) {
                    key = keyValue[i].substring(0, pos);
                    // ZAP: the parameter could be encoded so decode it
                    value = getUnescapedValue(keyValue[i].substring(pos + 1));
                    
                } else {
                    key = keyValue[i];
                    // ZAP: Removed "value = null;" the value is already initialized to null.
                }
                
                listParam.add(new NameValuePair(key, value, i));

            } catch (Exception e) {
            }
        }

    }

    /**
     *
     * @return
     */
    @Override
    public List<NameValuePair> getParamList() {
        return listParam;
    }

    /**
     * If name and value = null, not to append entire parameter.
     */
    @Override
    public String setParameter(HttpMessage msg, NameValuePair originalPair, String name, String value) {
        return this.setParameter(msg, originalPair, name, value, false);
    }

    @Override
    public String setEscapedParameter(HttpMessage msg, NameValuePair originalPair, String name, String value) {
        return this.setParameter(msg, originalPair, name, value, true);
    }

    private String setParameter(HttpMessage msg, NameValuePair originalPair, String name, String value, boolean escaped) {
        StringBuilder sb = new StringBuilder();
        String encodedValue = (escaped) ? value : getEscapedValue(msg, value);
        NameValuePair pair;
        boolean isAppended;
        
        for (int i = 0; i < getParamList().size(); i++) {
            pair = getParamList().get(i);
            if (i == originalPair.getPosition()) {
                isAppended = paramAppend(sb, name, encodedValue);

            } else {
                isAppended = paramAppend(sb, pair.getName(), getEscapedValue(msg, pair.getValue()));
            }

            if (isAppended && i < getParamList().size() - 1) {
                sb.append('&');
            }
        }

        String query = sb.toString();
        buildMessage(msg, query);
        return query;
    }

    /**
     * Set the name value pair into the StringBuilder. If both name and value is
     * null, not to append whole parameter.
     *
     * @param sb
     * @param name Null = not to append parameter.
     * @param value null = not to append parameter value.
     * @return true = parameter changed.
     */
    private boolean paramAppend(StringBuilder sb, String name, String value) {
        boolean isEdited = false;
        
        if (name != null) {
            sb.append(name);
            isEdited = true;
        }
        
        if (value != null) {
            sb.append('=');
            sb.append(value);
            isEdited = true;
        }
        
        return isEdited;
    }
}
