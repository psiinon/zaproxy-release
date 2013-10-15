/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zaproxy.zap.extension.pscanrules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Support utility able to manage multiple string/regex occurrence
 * searching inside a target content
 * 
 * @author yhawke 2013
 */
public class ContentMatcher {

    private static final String TAG_PATTERNS = "Patterns";
    private static final String TAG_PATTERN = "Pattern";
    private static final String TAG_PATTERN_TYPE = "type";
    private static final String TAG_PATTERN_TYPE_STRING = "string";
    private static final String TAG_PATTERN_TYPE_REGEX = "regex";
    
    private List<String> strings;
    private List<Pattern> patterns;
        
    /**
     * Direct method for a complete ContentMatcher instance creation.
     * @param xmlfileName the name of the XML file that need to be used for initialization
     * @return a ContentMatcher instance
     */
    public static ContentMatcher getInstance(String xmlFileName) {
        ContentMatcher cm = new ContentMatcher();

        // Load the pattern definitions from an XML file
        try {
            cm.loadXMLPatternDefinitions(xmlFileName);
            
        } catch (JDOMException | IOException ex) {
            throw new IllegalArgumentException("Failed to initialize the ContentMatcher object using: " + xmlFileName, ex);
        }
        
        return cm;
    }
    
    /**
     * Load a pattern list from an XML formatted file.
     * Pattern should be enclosed around a <Patterns> tag and should be
     * defined as <Pattern type="xxx"></Pattern>. Use "regex" to define
     * a Regex formatted pattern or "string" for an exact matching pattern.
     */
    public void loadXMLPatternDefinitions(String xmlFileName) throws JDOMException, IOException {
        strings = new ArrayList();
        patterns = new ArrayList();
        
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(this.getClass().getResourceAsStream(xmlFileName));
        Element el = doc.getRootElement();
        String value;
        
        // now we have the <root> tag indexed so we can
        // go ahead for boundaries and tests
        for (Object obj : el.getChildren(TAG_PATTERN)) {
            el = (Element)obj;
            value = el.getText();
            
            // Check if the pattern has been set to null
            if (value != null && !value.isEmpty()) {
            
                // Check if a regex expression has been set
                if (el.getAttributeValue(TAG_PATTERN_TYPE).equalsIgnoreCase(TAG_PATTERN_TYPE_REGEX)) {
                    patterns.add(Pattern.compile(el.getText()));
                
                // Otherwise it's by default an exact match model
                } else {
                    strings.add(el.getText());
                }
            }
        }
    }

    /**
     * Search for an occurrence inside a specific content
     * @param content the string content to search into
     * @return the found occurrence or null if no match has been done
     */
    public String findInContent(String content) {
        
        // First check for a simple exact occurrence
        for (String str : strings) {
            if (content.contains(str))
                return str;
        }
        
        // Then check for a regex occurrence
        Matcher matcher;
        for (Pattern pattern : patterns) {
            matcher = pattern.matcher(content);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        
        // No match found return null
        return null;
    }

    /**
     * Search for all possible occurrences inside a specific content
     * @param content the string content to search into
     * @return a list of existing occurrences
     */
    public List<String> findAllInContent(String content) {
        
        List<String> results = new LinkedList();
        
        // First check for all simple exact occurrences
        for (String str : strings) {
            if (content.contains(str))
                results.add(str);
        }
        
        // Then check for all regex occurrences
        Matcher matcher;
        for (Pattern pattern : patterns) {
            matcher = pattern.matcher(content);
            if (matcher.find()) {
                results.add(content);
            }
        }
        
        return results;
    }
}