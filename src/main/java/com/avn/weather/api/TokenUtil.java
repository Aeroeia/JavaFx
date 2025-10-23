package com.avn.weather.api;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.impl.Base64UrlCodec;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料
 * @description: token工具
 * @author: 阿星不是程序员
 **/
public class TokenUtil {

    public static String getToken() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        // 1️⃣ Header
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "EdDSA");
        header.put("kid", "KKWECXMHXH");

        // 2️⃣ Payload（注意 iat 和 exp 必须是秒级 Unix 时间戳）
        long now = System.currentTimeMillis() / 1000; // 秒级
        JSONObject payload = new JSONObject();
        payload.put("sub", "3DKTNRHP5U");
        payload.put("iat", now - 30);
        payload.put("exp", now + 86400); // 24小时有效期

        // 3️⃣ Base64URL 编码 Header 和 Payload
        Base64UrlCodec base64Url = new Base64UrlCodec();
        String headerEncoded = base64Url.encode(JSON.toJSONString(header).getBytes());
        String payloadEncoded = base64Url.encode(payload.toJSONString().getBytes());
        String signingInput = headerEncoded + "." + payloadEncoded;

        // 4️⃣ 解析私钥
        String privateKeyBase64 = "MC4CAQAwBQYDK2VwBCIEICduFtPb/FsoNNvqP1f2+axOWIWjK9wyLHGqvZfduqiy";
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
        KeyFactory keyFactory = KeyFactory.getInstance("Ed25519");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

        // 5️⃣ Ed25519 签名
        Signature signature = Signature.getInstance("Ed25519");
        signature.initSign(privateKey);
        signature.update(signingInput.getBytes("UTF-8"));
        byte[] sigBytes = signature.sign();
        String sigBase64Url = base64Url.encode(sigBytes);

        // 6️⃣ 拼接最终 JWT
        String jwt = signingInput + "." + sigBase64Url;

        System.out.println("JWT Token:");
        System.out.println(jwt);

        // ==== 3️⃣ 公钥解析 ====
        String publicKeyBase64 = "MCowBQYDK2VwAyEACZR/bRIiOUo1bobd7deBp2PbS+nEbXYWYf9JBz0kQBo=";
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        // ==== 4️⃣ 验证签名 ====
        Signature verifier = Signature.getInstance("Ed25519");
        verifier.initVerify(publicKey);
        verifier.update(signingInput.getBytes("UTF-8"));
        boolean isValid = verifier.verify(Base64UrlCodec.BASE64URL.decode(sigBase64Url));

        System.out.println("签名验证结果: " + isValid);
        return jwt;
    }

    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, SignatureException {
        getToken();
    }

}