package com.avn.weather.api;



import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料
 * @description: token工具
 * @author: 阿星不是程序员
 **/
public class TokenUtil {

    /**
     * 指定签名的时候使用的签名算法，也就是header那部分。
     *
     */
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    /**
     * 用户登录成功后生成Jwt
     * 使用Hs256算法
     *
     * @param info      登录成功的user对象
     * @param ttlMillis jwt过期时间
     * @param tokenSecret 私钥
     * @return
     */
    public static String createToken(Map<String,Object> header, String info, long ttlMillis, String tokenSecret) {
        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();

        //创建一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                .setHeader(header)
                //iat: jwt的签发时间
                .setIssuedAt(new Date(nowMillis))
                //代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串。
                .setSubject(info)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(SIGNATURE_ALGORITHM, tokenSecret);
        if (ttlMillis >= 0) {
            //设置过期时间
            builder.setExpiration(new Date(nowMillis + ttlMillis));
        }
        return builder.compact();
    }


    /**
     * Token的解密
     *
     * @param token 加密后的token
     * @param tokenSecret 私钥
     * @return
     */
    public static String parseToken(String token, String tokenSecret) {
        try {
            return Jwts.parser()
                    //设置签名的秘钥
                    .setSigningKey(tokenSecret)
                    //设置需要解析的jwt
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }catch (ExpiredJwtException jwtException) {
            System.out.println("token已过期");
        }
    }

    public static void main(String[] args) {

    }
}