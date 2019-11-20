package com.czx.shiro_demo.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Map;

/**
 * jwt-token验证工具类
 */
@Slf4j
public class JWTUtils {

    public static final String JWT_SECRET = "KVjKd6EPc3nF7bzmukixnn9DlCdeTGLI";

    /**
     * 创建jwt
     * @param claim	token保存的数据
     * @param ttlMillis 过期的时间长度
     * @return
     */
    public static String createJWT(Map<String, Object> claim, long ttlMillis){
        long nowMillis = System.currentTimeMillis();//生成JWT的时间
        //开始创建token
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claim)
                .signWith(SignatureAlgorithm.HS256, generalKey());//设置签名使用的签名算法和签名使用的秘钥
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);     //设置过期时间
        }
        return builder.compact();           //就开始压缩为xxxxxxxxxxxxxx.xxxxxxxxxxxxxxx.xxxxxxxxxxxxx这样的jwt
    }


    /**
     * 解密jwt
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt){
        SecretKey key = generalKey();  //签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = null;
        try{
            claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
        }catch (ExpiredJwtException e){
            log.error("token解密失败 <====== token已过期");
        }catch (Exception e2){
            log.error("token解密失败 <====== token已被篡改,无法进行解密");
        }
        return claims;
    }

    /**
     * 由字符串生成加密key
     * @return
     */
    private static SecretKey generalKey(){
        byte[] encodedKey = Base64.decodeBase64(JWT_SECRET);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

}
