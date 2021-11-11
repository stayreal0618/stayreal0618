package com.bj58.pay;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

/**
 * @author yy
 * @version 1.0v
 * @description 微信query
 * @date 2021/11/10 10:59
 */
public class WxBalanceQuery {

    @Test
    @DisplayName("微信余额查询")
    public void queryBalance() {

        try {

            String originalKey = new String(Files.readAllBytes(Paths.get("/Users/yy/Desktop/1229238801_20201015_cert/apiclient_key.pem", "utf-8")));
            String privateKeyStr = originalKey
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            PrivateKey privateKey = loadPrivateKey(privateKeyStr);

            WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                    .withMerchant("1229238801", "4B288290E211166287225C48E296E032F21ECD03", privateKey)
                    .withWechatpay(Arrays.asList(getCertificate(new FileInputStream(new File("/Users/yy/Desktop/1229238801_20201015_cert/test.pem/wechatpay_22D7513A448133F1E0B153811F6C1DC710733515.pem")))));

            URIBuilder uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/merchant/fund/balance/OPERATION");
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.addHeader("Accept", "application/json");
            httpGet.addHeader("Content-type","application/json; charset=utf-8");

            HttpClient httpClient = builder.build();


            HttpResponse response = httpClient.execute(httpGet);
            System.out.println(response.getEntity());
            String bodyAsString = EntityUtils.toString(response.getEntity());
            System.out.println(bodyAsString);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static X509Certificate getCertificate(InputStream inputStream) {

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inputStream);
            cert.checkValidity();
            return cert;
        } catch (CertificateExpiredException e) {
            throw new RuntimeException("证书已过期", e);
        } catch (CertificateNotYetValidException e) {
            throw new RuntimeException("证书尚未生效", e);
        } catch (CertificateException e) {
            throw new RuntimeException("无效的证书", e);
        }

    }

    public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }

    }


}
