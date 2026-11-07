/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;


import com.github.potatoxf.generals.Log;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author potatoxf
 */
@SuppressWarnings("unused")
public interface GeneralRemote {
    X509TrustManager HTTP_UNCHECK_X509_TRUST_MANAGER = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    };
    HostnameVerifier HTTP_UNCHECK_HOSTNAME_VERIFIER = (hostname, session) -> true;
    String IP_URL = "http://ip.taobao.com/service/getIpInfo.php";

    static String getLocalHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ignored) {
        }
        return "127.0.0.1";
    }

    static String getLocalHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
        }
        return "未知";
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    static String sendHttpGetRequest(String url, String param) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            Log.info("sendGet - %s", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            Log.info("recv - %s", result);
        } catch (ConnectException e) {
            Log.error("Invoke GeneralHTTP.sendGet ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            Log.error("Invoke GeneralHTTP.sendGet SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            Log.error("Invoke GeneralHTTP.sendGet IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            Log.error("Invoke GeneralHTTP.sendGet Exception, url=" + url + ",param=" + param, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                Log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    static String sendHttpPostRequest(String url, String param, Charset charset) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        String urlNameString = url + "?" + param;
        Log.info("sendPost - %s", urlNameString);
        try {
            URL realUrl = new URL(urlNameString);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", charset.name());
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            Log.info("recv - %s", result);
        } catch (ConnectException e) {
            Log.error("Invoke GeneralHTTP.sendPost ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            Log.error("Invoke GeneralHTTP.sendPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            Log.error("Invoke GeneralHTTP.sendPost IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            Log.error("调用HttpsUtil.sendPost Exception, url=" + url + ",param=" + param, e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                Log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    static String sendHttpsPostRequest(String url, String param, Charset charset) {
        StringBuilder result = new StringBuilder();
        String urlNameString = url + "?" + param;
        try {
            Log.info("sendSSLPost - %s", urlNameString);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{HTTP_UNCHECK_X509_TRUST_MANAGER}, new java.security.SecureRandom());
            URL console = new URL(urlNameString);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", charset.name());
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(HTTP_UNCHECK_HOSTNAME_VERIFIER);
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String ret;
            while ((ret = br.readLine()) != null) {
                if (!ret.trim().isEmpty()) {
                    result.append(ret);
                }
            }
            Log.info("recv - %s", result);
            conn.disconnect();
            br.close();
        } catch (ConnectException e) {
            Log.error("Invoke GeneralHTTP.sendSSLPost ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            Log.error("Invoke GeneralHTTP.sendSSLPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            Log.error("Invoke GeneralHTTP.sendSSLPost IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            Log.error("Invoke GeneralHTTP.sendSSLPost Exception, url=" + url + ",param=" + param, e);
        }
        return result.toString();
    }

    static boolean isInternalIp(String ip) {
        if ("127.0.0.1".equals(ip) || "localhost".equals(ip)) return true;
        byte[] addr = parseIPv4(ip);

        final byte b0 = addr[0];
        final byte b1 = addr[1];
        // 10.x.x.x/8
        final byte s1 = 0x0A;
        // 172.16.x.x/12
        final byte s2 = (byte) 0xAC;
        final byte s3 = (byte) 0x10;
        final byte s4 = (byte) 0x1F;
        // 192.168.x.x/16
        final byte s5 = (byte) 0xC0;
        final byte s6 = (byte) 0xA8;
        if (b0 == s1) return true;
        if (b0 == s2 && b1 >= s3 && b1 <= s4) {
            return true;
        }
        return b0 == s5 && b1 == s6;
    }

    /**
     * 将IPv4地址转换成字节
     *
     * @param text IPv4地址
     * @return byte 字节
     */
    static byte[] parseIPv4(String text) {
        if (text == null || text.isEmpty()) return null;
        byte[] bytes = new byte[4];
        String[] elements = text.split("\\.", -1);
        try {
            long l;
            int i;
            switch (elements.length) {
                case 1:
                    l = Long.parseLong(elements[0]);
                    if ((l < 0L) || (l > 4294967295L))
                        return null;
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    l = Integer.parseInt(elements[0]);
                    if ((l < 0L) || (l > 255L))
                        return null;
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elements[1]);
                    if ((l < 0L) || (l > 16777215L))
                        return null;
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    for (i = 0; i < 2; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L))
                            return null;
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elements[2]);
                    if ((l < 0L) || (l > 65535L))
                        return null;
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    for (i = 0; i < 4; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L))
                            return null;
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return bytes;
    }

    static String getRealAddressByIP(String ip) {
        String address = "XX XX";
        // 内网不查询
        if (GeneralRemote.isInternalIp(ip)) {
            return "内网IP";
        }
        String response = sendHttpPostRequest(IP_URL, "ip=" + ip, StandardCharsets.UTF_8);
        if (GeneralIF.isEmpty(response)) {
            Log.error("获取地理位置异常 {}", ip);
            return address;
        }
        response = GeneralString.clean(response, " \t\r\n");

        Pattern compile = Pattern.compile("['\"]([^'\"]*?)['\"]:(\\d+|(['\"]([^'\"]*?)['\"])|true|false)");
        Matcher matcher = compile.matcher(response);
        if (matcher.find()) {
            String region = null;
            String city = null;
            for (int i = 0; i < matcher.groupCount(); i++) {
                String group = matcher.group(i);
                if ("region".equals(group)) {
                    region = matcher.group(i + 1);
                }
                if ("city".equals(group)) {
                    city = matcher.group(i + 1);
                }
            }
            if (region != null && city != null) {
                address = region + " " + city;
            }
        }
        return address;
    }
}
