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
package org.zaproxy.zap.spider.parser;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.FormField;
import net.htmlparser.jericho.FormFields;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.parosproxy.paros.network.HtmlParameter;
import org.parosproxy.paros.network.HtmlParameter.Type;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.spider.SpiderParam;

/**
 * The Class SpiderHtmlFormParser is used for parsing HTML files and for processing forms.
 */
public class SpiderHtmlFormParser extends SpiderParser {

	private static final String ATTR_CHECKED = "checked";
	private static final String ATTR_DISABLED = "disabled";
	private static final String ATTR_SELECTED = "selected";
	private static final String TYPE_TEXT = "text";
	private static final String TYPE_RADIO = "radio";
	private static final String TYPE_CHECKBOX = "checkbox";
	private static final String DEFAULT_ON = "on";
	private static final String ATTR_VALUE = "value";
	private static final String DEFAULT_TEXT_VALUE = "1";
	private static final String METHOD_GET = "GET";
	private static final String METHOD_POST = "POST";
	private static final String ATTR_NAME = "name";
	private static final String ATTR_TYPE = "type";

	/** The spider parameters. */
	SpiderParam param;

	/**
	 * Instantiates a new spider html form parser.
	 * 
	 * @param param the parameters for the spider
	 */
	public SpiderHtmlFormParser(SpiderParam param) {
		super();
		this.param = param;
	}

	/* (non-Javadoc)
	 * 
	 * @see
	 * org.zaproxy.zap.spider.parser.SpiderParser#parseResource(org.parosproxy.paros.network.HttpMessage
	 * , net.htmlparser.jericho.Source, int) */
	@Override
	public void parseResource(HttpMessage message, Source source, int depth) {
		// Prepare the source, if not provided
		if (source == null)
			source = new Source(message.getResponseBody().toString());

		// Get the context (base url)
		String baseURL;
		if (message == null)
			baseURL = "";
		else
			baseURL = message.getRequestHeader().getURI().toString();
		log.info("Base URL: " + baseURL);

		// Go through the forms
		List<Element> forms = source.getAllElements(HTMLElementName.FORM);

		for (Element form : forms) {
			// Get method and action
			String method = form.getAttributeValue("method");
			String action = form.getAttributeValue("action");
			log.info("Found new form with method: '" + method + "' and action: " + action);

			// If no action, skip the form
			if (action == null) {
				log.warn("Skipping form with no 'action' defined at: " + form.getDebugInfo());
				continue;
			}

			// Prepare data set
			List<HtmlParameter> formDataSet = prepareFormDataSet(form.getFormFields());

			// Process the case of a GET method
			if (method == null || method.trim().equalsIgnoreCase(METHOD_GET)) {
				String query = buildGetQuery(formDataSet);
				log.info("Submiting form with GET method and query with form parameters: " + query);

				if (action.contains("?"))
					if (action.endsWith("?"))
						processURL(message, depth, action + query, baseURL);
					else
						processURL(message, depth, action + "&" + query, baseURL);
				else
					processURL(message, depth, action + "?" + query, baseURL);
			}

		}

	}

	/**
	 * Prepares the form data set. A form data set is a sequence of control-name/current-value pairs
	 * constructed from successful controls, which will be sent with a GET/POST request for a form.
	 * 
	 * <br/>
	 * Also see:
	 * http://whatwg.org/specs/web-apps/current-work/multipage/association-of-controls-and-forms.
	 * html
	 * 
	 * @see http://www.w3.org/TR/REC-html40/interact/forms.html#form-data-set
	 * @param form the form
	 * @return the list
	 */
	private List<HtmlParameter> prepareFormDataSet(FormFields form) {
		List<HtmlParameter> formDataSet = new LinkedList<HtmlParameter>();

		// Process each form field
		Iterator<FormField> it = form.iterator();
		while (it.hasNext()) {
			FormField field = it.next();
			if (log.isDebugEnabled())
				log.debug("New form field: " + field.getDebugInfo());

			// Get its value(s)
			List<String> values = field.getValues();
			if (log.isDebugEnabled())
				log.debug("Existing values: " + values);

			// If there are no values at all or only an empty value
			if (values.isEmpty() || (values.size() == 1 && values.get(0).isEmpty())) {
				String finalValue = "";

				// Check if we can use predefined values
				Collection<String> predefValues = field.getPredefinedValues();
				if (!predefValues.isEmpty()) {
					// Try first elements
					Iterator<String> iterator = predefValues.iterator();
					finalValue = iterator.next();

					// If there are more values, don't use the first, as it usually is a "No select"
					// item
					if (iterator.hasNext())
						finalValue = iterator.next();
				} else {
					/* In all cases, according to Jericho documentation, the only left option is for
					 * it to be a TEXT field, without any predefined value. We check if it has only
					 * one userValueCount, and, if so, fill it with a default value. */
					if (field.getUserValueCount() > 0)
						finalValue = getDefaultTextValue(field);
				}

				// Save the finalValue in the FormDataSet
				log.info("No existing value for field " + field.getName() + ". Generated: " + finalValue);
				HtmlParameter p = new HtmlParameter(Type.form, field.getName(), finalValue);
				formDataSet.add(p);
			} else
				for (String v : values) {
					// Save the finalValue in the FormDataSet
					HtmlParameter p = new HtmlParameter(Type.form, field.getName(), v);
					formDataSet.add(p);
				}

		}
		//
		//
		//
		// FormField checkbox=fs.get("type-checkbox");
		// checkbox.setValue("Value2");
		// log.info("vals:" +checkbox.getValues()+"/"+checkbox.getUserValueCount());
		// Map<String, String[]> dataSet=fs.getDataSet();
		// for(String k:dataSet.keySet())
		// {
		// log.warn("Data set entry "+k+": "+Arrays.toString(dataSet.get(k)));
		// }
		// //log.info("Processing new form: "+form.getFormFields().getDataSet());
		// for(FormControl c:form.getFormControls())
		// log.info(c.getFormControlType()+"/"+c.getValues()+"/"+c.getPredefinedValue());
		//
		// // Process INPUT elements
		// elements = form.getAllElements(HTMLElementName.INPUT);
		// for (Element e : elements) {
		//
		// // Check if disabled
		// String disabled = e.getAttributeValue(ATTR_DISABLED);
		// if (disabled != null && !disabled.equalsIgnoreCase("false"))
		// continue;
		//
		// // Get required attributes
		// String type = e.getAttributeValue(ATTR_TYPE);
		// String value = e.getAttributeValue(ATTR_VALUE);
		// String name = e.getAttributeValue(ATTR_NAME);
		// log.debug("New form INPUT element: " + type + "/" + name + "=" + value);
		//
		// // Check for name
		// if (name == null || name.trim().isEmpty())
		// continue;
		//
		// // Text input
		// if (type.equalsIgnoreCase(TYPE_TEXT)) {
		// // If no default value, use a default one
		// if (value == null)
		// value = DEFAULT_TEXT_VALUE;
		//
		// HtmlParameter p = new HtmlParameter(Type.form, name, value);
		// formDataSet.add(p);
		// continue;
		// }
		//
		// if (type.equalsIgnoreCase(TYPE_CHECKBOX) || type.equalsIgnoreCase(TYPE_RADIO)) {
		// // Only add the checked fields
		// String checked = e.getAttributeValue(ATTR_CHECKED);
		// if (checked == null || !checked.equalsIgnoreCase("false"))
		// continue;
		//
		// // If not default value, use a default one
		// if (value == null)
		// value = DEFAULT_ON;
		//
		// HtmlParameter p = new HtmlParameter(Type.form, name, value);
		// formDataSet.add(p);
		// continue;
		// }
		// }
		//
		// // Process SELECT elements
		// elements = form.getAllElements(HTMLElementName.SELECT);
		// for (Element e : elements) {
		//
		// // Check if disabled
		// String disabled = e.getAttributeValue(ATTR_DISABLED);
		// if (disabled != null && !disabled.equalsIgnoreCase("false"))
		// continue;
		//
		// // Get required attributes
		// String name = e.getAttributeValue(ATTR_NAME);
		// log.debug("New form SELECT element: " + name);
		//
		// // Check for name
		// if (name == null || name.trim().isEmpty())
		// continue;
		//
		// // Process OPTION elements and add selected ones
		// List<Element> options = e.getAllElements(HTMLElementName.OPTION);
		// for (Element o : options) {
		// String selected = o.getAttributeValue(ATTR_SELECTED);
		// String value = o.getAttributeValue(ATTR_VALUE);
		//
		// if (selected != null && !selected.equalsIgnoreCase("false"))
		// if (value != null) {
		// HtmlParameter p = new HtmlParameter(Type.form, name, value);
		// formDataSet.add(p);
		// }
		// }
		// }
		//
		// // Process TEXTAREA elements
		// elements = form.getAllElements(HTMLElementName.TEXTAREA);
		// for (Element e : elements) {
		//
		// // Check if disabled
		// String disabled = e.getAttributeValue(ATTR_DISABLED);
		// if (disabled != null && !disabled.equalsIgnoreCase("false"))
		// continue;
		//
		// // Get required attributes
		// String name = e.getAttributeValue(ATTR_NAME);
		// log.debug("New form TEXTAREA element: " + name);
		//
		// // Check for name
		// if (name == null || name.trim().isEmpty())
		// continue;
		//
		// // Process OPTION elements and add selected ones
		// String value = e.getContent().toString();
		// if (value.isEmpty())
		// value = DEFAULT_TEXT_VALUE;
		// HtmlParameter p = new HtmlParameter(Type.form, name, value);
		// formDataSet.add(p);
		//
		// }
		//
		return formDataSet;
	}

	/**
	 * Gets the default value that a field of type TEXT (including its HTML5 child variants), should
	 * have.
	 * 
	 * @param field the field
	 * @return the default text value
	 */
	private String getDefaultTextValue(FormField field) {
		return DEFAULT_TEXT_VALUE;
	}

	/**
	 * Builds the get query.
	 * 
	 * @param formDataSet the form data set
	 * @return the query
	 */
	private String buildGetQuery(List<HtmlParameter> formDataSet) {
		StringBuilder request = new StringBuilder();
		// Build the query
		for (HtmlParameter p : formDataSet) {
			request.append(p.getName());
			request.append("=");
			request.append(p.getValue());
			request.append("&");
		}
		// Delete the last ampersand
		if (request.length() > 0)
			request.deleteCharAt(request.length() - 1);

		return request.toString();
	}
}
