/*
 * Copyright 2020-2099 sa-token.com
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
package cn.dev33.satoken.httpauth.basic;

import cn.dev33.satoken.json.SaJsonType;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.util.SaFoxUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Sa-Token Http Basic 账号
 *
 * @author click33
 * @since 1.41.0
 */
@Getter
@Setter
@Accessors(chain = true) // 关键！set返回this
public class SaHttpBasicAccount implements SaJsonType {

	/**
	 * 账号
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 构造函数
	 * @param username 账号
	 * @param password 密码
	 */
	public SaHttpBasicAccount(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * 构造函数
	 * @param usernameAndPassword 账号和密码，冒号隔开
	 */
	public SaHttpBasicAccount(String usernameAndPassword) {
		if(SaFoxUtil.isEmpty(usernameAndPassword)) {
			throw new SaTokenException("UsernameAndPassword 不能为空");
		}
		String[] arr = usernameAndPassword.split(":");
		if(arr.length != 2) {
			throw new SaTokenException("UsernameAndPassword 格式错误，正确格式为：username:password");
		}
		this.username = arr[0];
		this.password = arr[1];
	}

	@Override
	public String toString() {
		return "SaHttpBasicAccount{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				'}';
	}

}
