/*
 * Copyright 2008-2009 the original author or authors.
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

package org.broadleafcommerce.common.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.common.security.util.CookieUtils;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

/**
 * This class adds additional features to the token based remember me services provided by
 * Spring security. Specifically, we would like to be able to include the httpOnly parameter
 * to cookie values that are generated by Broadleaf Commerce. Since the default implementation
 * provided by Spring Security does not provide this additional functionality, we override
 * here to use the CookieUtils in Broadleaf that will include the httpOnly value.
 * 
 * Note - this class does not add httpOnly protection for session cookies. Adding httpOnly
 * for session cookies is handled at the application container configuration level, if supported.
 * 
 * @author jfischer
 *
 */
public class EnhancedTokenBasedRememberMeServices extends TokenBasedRememberMeServices {

	@Resource(name="blCookieUtils")
	protected CookieUtils cookieUtils;
	
	@Override
	protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
		MockResponse mockResponse = new MockResponse();
		super.setCookie(tokens, maxAge, request, mockResponse);
		Cookie myCookie = mockResponse.getTempCookie();
		cookieUtils.setCookieValue(response, myCookie.getName(), myCookie.getValue(), myCookie.getPath(), myCookie.getMaxAge(), myCookie.getSecure());
	}
	
	private class MockResponse implements HttpServletResponse {
		
		private Cookie tempCookie;

		public void addCookie(Cookie arg0) {
			this.tempCookie = arg0;
		}
		
		public Cookie getTempCookie() {
			return tempCookie;
		}

		public void addDateHeader(String arg0, long arg1) {
			//do nothing
		}

		public void addHeader(String arg0, String arg1) {
			//do nothing
		}

		public void addIntHeader(String arg0, int arg1) {
			//do nothing
		}

		public boolean containsHeader(String arg0) {
			return false;
		}

		public String encodeRedirectUrl(String arg0) {
			return null;
		}

		public String encodeRedirectURL(String arg0) {
			return null;
		}

		public String encodeUrl(String arg0) {
			return null;
		}

		public String encodeURL(String arg0) {
			return null;
		}

		public void sendError(int arg0, String arg1) throws IOException {
			//do nothing
		}

		public void sendError(int arg0) throws IOException {
			//do nothing
		}

		public void sendRedirect(String arg0) throws IOException {
			//do nothing
		}

		public void setDateHeader(String arg0, long arg1) {
			//do nothing
		}

		public void setHeader(String arg0, String arg1) {
			//do nothing
		}

		public void setIntHeader(String arg0, int arg1) {
			//do nothing
		}

		public void setStatus(int arg0, String arg1) {
			//do nothing
		}

		public void setStatus(int arg0) {
			//do nothing
		}

		public void flushBuffer() throws IOException {
			//do nothing
		}

		public int getBufferSize() {
			return 0;
		}

		public String getCharacterEncoding() {
			return null;
		}

		public String getContentType() {
			return null;
		}

		public Locale getLocale() {
			return null;
		}

		public ServletOutputStream getOutputStream() throws IOException {
			return null;
		}

		public PrintWriter getWriter() throws IOException {
			return null;
		}

		public boolean isCommitted() {
			return false;
		}

		public void reset() {
			//do nothing
		}

		public void resetBuffer() {
			//do nothing
		}

		public void setBufferSize(int arg0) {
			//do nothing
		}

		public void setCharacterEncoding(String arg0) {
			//do nothing
		}

		public void setContentLength(int arg0) {
			//do nothing
		}

		public void setContentType(String arg0) {
			//do nothing
		}

		public void setLocale(Locale arg0) {
			//do nothing
		}
		
	}
}
