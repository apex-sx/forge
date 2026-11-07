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
package cn.dev33.satoken.httpauth.digest;

import cn.dev33.satoken.json.SaJsonType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Sa-Token Http Digest 认证 - 参数实体类
 *
 * @author click33
 * @since 1.38.0
 */
@Getter
@Setter
@Accessors(chain = true) // 关键！set返回this
public class SaHttpDigestModel implements SaJsonType {

    /**
     * 默认的 Realm 领域名称
     */
    public static final String DEFAULT_REALM = "Sa-Token";

    /**
     * 默认的 qop 值
     */
    public static final String DEFAULT_QOP = "auth";


    /**
     * 用户名
     * -- GETTER --
     *  获取 用户名
     *
     * @return username 用户名

     */
    public String username;

    /**
     * 密码
     * -- GETTER --
     *  获取 密码
     *
     * @return password 密码

     */
    public String password;

    /**
     * 领域
     * -- GETTER --
     *  获取 领域
     *
     * @return realm 领域

     */
    public String realm = DEFAULT_REALM;

    /**
     * 随机数
     * -- GETTER --
     *  获取 随机数
     *
     * @return nonce 随机数

     */
    public String nonce;

    /**
     * 请求 uri
     * -- GETTER --
     *  获取 请求 uri
     *
     * @return uri 请求 uri

     */
    public String uri;

    /**
     * 请求方法
     * -- GETTER --
     *  获取 请求方法
     *
     * @return method 请求方法

     */
    public String method;

    /**
     * 保护质量（auth=默认的，auth-int=增加报文完整性检测），可以为空，但不推荐
     * -- GETTER --
     *  获取 保护质量（auth=默认的，auth-int=增加报文完整性检测），可以为空，但不推荐
     *
     * @return qop 保护质量（auth=默认的，auth-int=增加报文完整性检测），可以为空，但不推荐

     */
    public String qop;

    /**
     * nonce计数器，是一个16进制的数值，表示同一nonce下客户端发送出请求的数量
     * -- GETTER --
     *  获取 nonce计数器，是一个16进制的数值，表示同一nonce下客户端发送出请求的数量
     *
     * @return nc nonce计数器，是一个16进制的数值，表示同一nonce下客户端发送出请求的数量

     */
    public String nc;

    /**
     * 客户端随机数，由客户端提供
     * -- GETTER --
     *  获取 客户端随机数，由客户端提供
     *
     * @return cnonce 客户端随机数，由客户端提供

     */
    public String cnonce;

    /**
     * opaque
     * -- GETTER --
     *  获取 opaque
     *
     * @return opaque opaque

     */
    public String opaque;

    /**
     * 请求摘要，最终计算的摘要结果
     * -- GETTER --
     *  获取 请求摘要，最终计算的摘要结果
     *
     * @return response 请求摘要，最终计算的摘要结果

     */
    public String response;

    // ------------------- 构造函数 -------------------

    public SaHttpDigestModel() {
    }
    public SaHttpDigestModel(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public SaHttpDigestModel(String username, String password, String realm) {
        this.username = username;
        this.password = password;
        this.realm = realm;
    }

    @Override
    public String toString() {
        return "SaHttpDigestModel[" +
                "username=" + username +
                ", password=" + password +
                ", realm=" + realm +
                ", nonce=" + nonce +
                ", uri=" + uri +
                ", method=" + method +
                ", qop=" + qop +
                ", nc=" + nc +
                ", cnonce=" + cnonce +
                ", opaque=" + opaque +
                ", response=" + response +
                "]";
    }

}