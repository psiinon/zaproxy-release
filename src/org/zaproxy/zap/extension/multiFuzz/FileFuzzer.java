/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2014 The ZAP Development Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package org.zaproxy.zap.extension.multiFuzz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
/**
 * Represents a collection of payloads which are read from a file.
 *
 * @param <P>	the payload type
 */
public class FileFuzzer<P extends Payload> {
	public static final String TYPE_SIG_BEG = "#<type=\"";
	public static final String TYPE_SIG_END = "\">";
	public static final String COMMENT = "#";
	private String name = null;
	private File file = null;
	private ArrayList<P> payloads = new ArrayList<>();
	private PayloadFactory<P> factory;
	private int length;
	private static Logger log = Logger.getLogger(FileFuzzer.class);
	/**
	 * Constructor of an empty FileFuzzer
	 * @param s		the FileFuzzers name
	 * @param f		the {@link PayloadFactory} for generation of {@link Payload}
	 */
	public FileFuzzer(String s, PayloadFactory<P> f) {
		this.file = null;
		this.name = s;
		this.factory = f;
	}
	/**
	 * Constructor directly from a file
	 * @param file	the file
	 * @param f		the {@link PayloadFactory} for generation of {@link Payload}
	 */
	public FileFuzzer(File file, PayloadFactory<P> f) {
		this.file = file;
		this.name = file.getName();
		this.factory = f;
		init();
	}
	private void init() {
		this.length = 0;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));
			String line;
			while ((line = in.readLine()) != null) {
				if (!line.startsWith(COMMENT)) {
					this.length++;
				}
			}

		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}
	private void read() {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));

			String line = in.readLine();
			if (line.startsWith(TYPE_SIG_BEG) && line.endsWith(TYPE_SIG_END)) {
				String type = line.substring(8, (line.length() - 2));
				do {
					if (line.trim().length() > 0 && !line.startsWith(COMMENT)) {
						Payload.Type t;
						switch (type) {
						case "FILE":
							t = Payload.Type.FILE;
							break;
						case "REGEX":
							t = Payload.Type.REGEX;
							break;
						case "SCRIPT":
							t = Payload.Type.SCRIPT;
							break;
						default:
							t = Payload.Type.STRING;
							break;
						}
						payloads.add(factory.createPayload(t, line));
					}
				} while ((line = in.readLine()) != null);
			} else {
				do {
					if (line.trim().length() > 0 && !line.startsWith(COMMENT)) {
						payloads.add(factory.createPayload(line));
					}
				} while ((line = in.readLine()) != null);
			}

		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}
	/**
	 * Generates the list of payloads in this FileFuzzer
	 * @return	the list of payloads
	 */
	public ArrayList<P> getList() {
		if (payloads == null) {
			read();
		}
		return this.payloads;
	}
	/**
	 * Generates the iterator over all payloads.
	 * @return	the iterator
	 */
	public Iterator<P> getIterator() {
		if (payloads == null) {
			read();
		}
		return payloads.iterator();
	}
	/**
	 * The FileFuzzers name or file location
	 * @return	The name
	 */
	public String getFileName() {
		return this.name;
	}
}
