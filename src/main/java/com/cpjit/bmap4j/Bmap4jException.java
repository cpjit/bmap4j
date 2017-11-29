/*
 * Copyright 2011-2017 CPJIT Group.
 * 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.cpjit.bmap4j;

/**
 * @author yonghuan
 *
 */
public class Bmap4jException extends Exception {

	private static final long serialVersionUID = -50917301337385014L;

	public Bmap4jException() {
	}

	public Bmap4jException(String message, Throwable cause) {
		super(message, cause);
	}

	public Bmap4jException(String message) {
		super(message);
	}

	public Bmap4jException(Throwable cause) {
		super(cause);
	}


}
