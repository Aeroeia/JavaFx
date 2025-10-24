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
 * Token工具类
 * 提供JWT Token生成和缓存功能
 */
public class TokenUtil {
    
    // Token缓存
    private static String cachedToken = null;
    private static long tokenExpireTime = 0;
    
    // Token有效期（秒），设置为1小时
    private static final long TOKEN_VALIDITY_SECONDS = 3600;
    
    /**
     * 获取Token，如果缓存的Token仍然有效则直接返回，否则生成新的Token
     */
    public static String getToken() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        long currentTime = System.currentTimeMillis() / 1000;
        
        // 检查缓存的Token是否仍然有效（提前5分钟刷新）
        if (cachedToken != null && currentTime < (tokenExpireTime - 300)) {
            return cachedToken;
        }
        
        // 生成新的Token
        cachedToken = generateNewToken();
        tokenExpireTime = currentTime + TOKEN_VALIDITY_SECONDS;
        
        return cachedToken;
    }
    
    /**
     * 生成新的JWT Token
     */
    private static String generateNewToken() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        System.out.println("生成token");
        // 1️⃣ Header
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "EdDSA");
        header.put("kid", "KKWECXMHXH");

        // 2️⃣ Payload（注意 iat 和 exp 必须是秒级 Unix 时间戳）
        long now = System.currentTimeMillis() / 1000; // 秒级
        JSONObject payload = new JSONObject();
        payload.put("sub", "3DKTNRHP5U");
        payload.put("iat", now - 30);
        payload.put("exp", now + 80000);

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

        // 验证签名（可选，用于确保Token正确性）
        String publicKeyBase64 = "MCowBQYDK2VwAyEACZR/bRIiOUo1bobd7deBp2PbS+nEbXYWYf9JBz0kQBo=";
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        Signature verifier = Signature.getInstance("Ed25519");
        verifier.initVerify(publicKey);
        verifier.update(signingInput.getBytes("UTF-8"));
        boolean isValid = verifier.verify(Base64UrlCodec.BASE64URL.decode(sigBase64Url));

        if (!isValid) {
            throw new SignatureException("Token签名验证失败");
        }
        System.out.println(jwt);
        return jwt;
    }



}