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
 * 
 * ZAP: Based on work by Yasser Ganjisaffar <lastname at gmail dot com> 
 * from project http://code.google.com/p/crawler4j/
 */

package org.zaproxy.zap.spider;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.httpclient.URIException;
import org.apache.log4j.Logger;
import org.zaproxy.zap.spider.SpiderParam.HandleParametersOption;

/**
 * The URLCanonicalizer is used for the process of converting an URL into a canonical (normalized) form. See
 * <a href="http://en.wikipedia.org/wiki/URL_normalization">URL Normalization</a> for a reference. <br/>
 * <br/>
 * 
 * Note: some parts of the code are adapted from: <a
 * href="http://stackoverflow.com/a/4057470/405418">stackoverflow</a>
 */
public class URLCanonicalizer {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(URLCanonicalizer.class);

	/** The Constant IRRELEVANT_PARAMETERS defining the parameter names which are ignored in the URL. */
	private static final HashSet<String> IRRELEVANT_PARAMETERS = new HashSet<String>(3);
	static {
		IRRELEVANT_PARAMETERS.add("jsessionid");
		IRRELEVANT_PARAMETERS.add("phpsessid");
		IRRELEVANT_PARAMETERS.add("aspsessionid");
	}

	/**
	 * Gets the canonical url.
	 * 
	 * @param url the url
	 * @return the canonical url
	 */
	public static String getCanonicalURL(String url) {
		return getCanonicalURL(url, null);
	}

	/**
	 * Gets the canonical url, starting from a relative or absolute url found in a given context (baseURL).
	 * 
	 * @param url the url string defining the reference
	 * @param baseURL the context in which this url was found
	 * @return the canonical url
	 */
	public static String getCanonicalURL(String url, String baseURL) {

		try {
			/* Build the absolute URL, from the url and the baseURL */
			String resolvedURL = URLResolver.resolveUrl(baseURL == null ? "" : baseURL, url);
			log.debug("Resolved URL: " + resolvedURL);
			URI canonicalURI = new URI(resolvedURL);

			/* Some checking. */
			if (canonicalURI.getScheme() == null)
				throw new MalformedURLException("Protocol could not be reliably evaluated from uri: " + canonicalURI
						+ " and base url: " + baseURL);
			if (canonicalURI.getHost() == null)
				throw new MalformedURLException("Host could not be reliably evaluated from: " + canonicalURI);

			/*
			 * Normalize: no empty segments (i.e., "//"), no segments equal to ".", and no segments equal to
			 * ".." that are preceded by a segment not equal to "..".
			 */
			String path = canonicalURI.normalize().getPath();

			/* Convert '//' -> '/' */
			int idx = path.indexOf("//");
			while (idx >= 0) {
				path = path.replace("//", "/");
				idx = path.indexOf("//");
			}

			/* Drop starting '/../' */
			while (path.startsWith("/../")) {
				path = path.substring(3);
			}

			/* Trim */
			path = path.trim();

			/* Process parameters and sort them. */
			final SortedMap<String, String> params = createParameterMap(canonicalURI.getQuery());
			final String queryString;
			String canonicalParams = canonicalize(params);
			queryString = (canonicalParams.isEmpty() ? "" : "?" + canonicalParams);

			/* Add starting slash if needed */
			if (path.length() == 0) {
				path = "/" + path;
			}

			/* Drop default port: example.com:80 -> example.com */
			int port = canonicalURI.getPort();
			if (port == 80) {
				port = -1;
			}

			/* Lowercasing protocol and host */
			String protocol = canonicalURI.getScheme().toLowerCase();
			String host = canonicalURI.getHost().toLowerCase();
			String pathAndQueryString = normalizePath(path) + queryString;

			URL result = new URL(protocol, host, port, pathAndQueryString);
			return result.toExternalForm();

		} catch (MalformedURLException ex) {
			log.warn("Error while Processing URL in the spidering process (on base " + baseURL + "): "
					+ ex.getMessage());
			return null;
		} catch (URISyntaxException ex) {
			log.warn("Error while Processing URI in the spidering process (on base " + baseURL + "): "
					+ ex.getMessage());
			return null;
		}
	}

	/**
	 * Builds a String representation of the URI with cleaned parameters, that can be used when checking if an
	 * URI was already visited. The URI provided as a parameter should be already cleaned and canonicalized,
	 * so it should be build with a result from {@link #getCanonicalURL(String)}.
	 * 
	 * @param uri the uri
	 * @param handleParameters the handle parameters option
	 * @return the string representation of the URI
	 * @throws URIException the URI exception
	 */
	public static String buildCleanedParametersURIRepresentation(org.apache.commons.httpclient.URI uri,
			SpiderParam.HandleParametersOption handleParameters) throws URIException {
		// If the option is set to use all the information, just use the default string representation
		if (handleParameters.equals(HandleParametersOption.USE_ALL))
			return uri.toString();

		// If the option is set to ignore parameters completely, ignore the query completely
		if (handleParameters.equals(HandleParametersOption.IGNORE_COMPLETELY)) {
			if (uri.getPath() != null)
				return new StringBuilder().append(uri.getScheme()).append("://").append(uri.getHost()).append(":")
						.append(uri.getPort()).append(uri.getPath()).toString();
			else
				return new StringBuilder().append(uri.getScheme()).append("://").append(uri.getHost()).append(":")
						.append(uri.getPort()).toString();
		}

		// If the option is set to ignore the value, we get the parameters and we only add their name to the
		// query
		if (handleParameters.equals(HandleParametersOption.IGNORE_VALUE)) {
			StringBuilder retVal = new StringBuilder();
			if (uri.getPath() != null)
				retVal.append(uri.getScheme()).append("://").append(uri.getHost()).append(":").append(uri.getPort())
						.append(uri.getPath()).toString();
			else
				retVal.append(uri.getScheme()).append("://").append(uri.getHost()).append(":").append(uri.getPort())
						.toString();

			// Get the parameters' names
			SortedMap<String, String> params = createParameterMap(uri.getQuery());
			StringBuilder sb = new StringBuilder();
			if (params != null && !params.isEmpty()) {
				for (String key : params.keySet()) {
					// Ignore irrelevant parameters
					if (IRRELEVANT_PARAMETERS.contains(key) || key.startsWith("utm_")) {
						continue;
					}
					if (sb.length() > 0) {
						sb.append('&');
					}
					sb.append(key);
				}
			}
			// Add the parameters' names to the uri representation. We can add '?' everytime as it does not
			// have any influence if we are consistent
			retVal.append("?").append(sb);

			return retVal.toString();
		}

		// Should not be reached
		return uri.toString();
	}

	/**
	 * Takes a query string, separates the constituent name-value pairs, and stores them in a SortedMap
	 * ordered by lexicographical order.
	 * 
	 * @param queryString the query string
	 * @return Null if there is no query string.
	 */
	private static SortedMap<String, String> createParameterMap(final String queryString) {
		if (queryString == null || queryString.isEmpty()) {
			return null;
		}

		final String[] pairs = queryString.split("&");
		final SortedMap<String, String> params = new TreeMap<String, String>();

		for (final String pair : pairs) {
			if (pair.length() == 0) {
				continue;
			}

			String[] tokens = pair.split("=", 2);
			switch (tokens.length) {
			case 1:
				if (pair.charAt(0) == '=') {
					params.put("", tokens[0]);
				} else {
					params.put(tokens[0], "");
				}
				break;
			case 2:
				params.put(tokens[0], tokens[1]);
				break;
			}
		}
		return params;
	}

	/**
	 * Canonicalize the query string.
	 * 
	 * @param sortedParamMap Parameter name-value pairs in lexicographical order.
	 * @return Canonical form of query string.
	 */
	private static String canonicalize(final SortedMap<String, String> sortedParamMap) {
		if (sortedParamMap == null || sortedParamMap.isEmpty()) {
			return "";
		}

		final StringBuilder sb = new StringBuilder(100);
		for (Map.Entry<String, String> pair : sortedParamMap.entrySet()) {
			final String key = pair.getKey().toLowerCase();
			// Ignore irrelevant parameters
			if (IRRELEVANT_PARAMETERS.contains(key) || key.startsWith("utm_")) {
				continue;
			}
			if (sb.length() > 0) {
				sb.append('&');
			}
			sb.append(percentEncodeRfc3986(pair.getKey()));
			if (!pair.getValue().isEmpty()) {
				sb.append('=');
				sb.append(percentEncodeRfc3986(pair.getValue()));
			}
		}
		return sb.toString();
	}

	/**
	 * Percent-encode values according the RFC 3986. The built-in Java URLEncoder does not encode according to
	 * the RFC, so we make the extra replacements.
	 * 
	 * @param string Decoded string.
	 * @return Encoded string per RFC 3986.
	 */
	private static String percentEncodeRfc3986(String string) {
		try {
			string = string.replace("+", "%2B");
			string = URLDecoder.decode(string, "UTF-8");
			string = URLEncoder.encode(string, "UTF-8");
			return string.replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
		} catch (Exception e) {
			return string;
		}
	}

	/**
	 * Normalize path.
	 * 
	 * @param path the path
	 * @return the string
	 */
	private static String normalizePath(final String path) {
		return path.replace("%7E", "~").replace(" ", "%20");
	}

}
