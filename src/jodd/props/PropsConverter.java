// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package jodd.props;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Converter of Java Properties to Jodd Props format.
 */
public class PropsConverter {

	public static void main(String[] args) throws Exception {
		// 1. 基础主属性
		Properties mainProps = new Properties();
		mainProps.setProperty("app.name", "demo-app");
		mainProps.setProperty("app.version", "1.0.0");
		mainProps.setProperty("db.url", "jdbc:mysql://127.0.0.1:3306/demo");
		mainProps.setProperty("db.user", "root");

		// 2. 多profile示例（dev / test），第二个重载方法专用
		Map<String, Properties> profiles = new HashMap<>();

		Properties devProfile = new Properties();
		devProfile.setProperty("db.url", "jdbc:mysql://127.0.0.1:3306/dev-db");
		devProfile.setProperty("log.level", "debug");
		profiles.put("dev", devProfile);

		Properties testProfile = new Properties();
		testProfile.setProperty("db.url", "jdbc:mysql://127.0.0.1:3306/test-db");
		testProfile.setProperty("log.level", "info");
		profiles.put("test", testProfile);

		// ===== 测试第一个重载方法：无profile =====
		System.out.println("===== 【测试1：不带Profiles】 =====");
		try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
			//convert(pw, mainProps);
			System.out.println(sw);
		}

		// ===== 测试第二个重载方法：带Profiles =====
		System.out.println("===== 【测试2：带Profiles(dev/test)】 =====");
		try (StringWriter sw = new StringWriter();
			 PrintWriter pw = new PrintWriter(sw)) {
			//convert(pw, mainProps, profiles);
			System.out.println(sw);
		}
	}
}
