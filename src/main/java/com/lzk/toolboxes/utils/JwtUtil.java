package com.lzk.toolboxes.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.*;

/**
 * @author
 * @module
 * @date 2021/12/15 14:31
 */
public class JwtUtil {

    private static final Integer expirationTime = 120;

    private static final String SECRET = "dsjkzlkadmdzkl20384asjka1";

    private JwtUtil() {}

    /**
     * 创建jwt  token
     * @param token  需要保存的信息
     * @return
     */
    public static String createToken(String token){
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,expirationTime);
        HashMap<String, Object> headerClaims = new HashMap<>();
        headerClaims.put("type","JWT");
        String jwtToken = JWT.create()
                .withHeader(headerClaims)   //头部（header），可以写token类型、加密类型
                .withClaim("token", token)   //载荷（payload）  存储需要传递的信息，如用户id、用户名等
                .withSubject("")    //jwt所面向的用户，即他的所有人
                .withIssuer("auth0")    //签发者
                .withIssuedAt(new Date())   //签发时间
                .withExpiresAt(calendar.getTime())    //有效时间
                .sign(Algorithm.HMAC256(SECRET));   //签名（signature） = 加密后的（编码后的header + 编码后的payload + 盐（服务端秘钥））
        return jwtToken;
    }


    /**
     * 校验jwt  token
     * @param token
     * @return  保存的信息
     */
    public static String verifyToken(String token){
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("token").asString();
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            System.out.println("token无效");
            return null;
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            System.out.println("token已过期");
            return null;
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return null;
        }
    }


}
