package com.my.column.util;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Map;

/**
 * jwt 工具类
 * 1: 创建jwt
 * 2:解密jwt
 */
@Component
public class JWTUtil {
    private static final String KEY="ksdjfvhwhfowsrhfgdfhgdrygheqweqweqeqweqwreafwseefgwetgffgwsdfgwgweftgsdgdrhg";


    public static String createJWT(Map map) throws JOSEException {
        //第一部分 头部，主要防jwt自我描述部分，比如加密方式
        JWSHeader header=new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT).build();

        //第二部分 载荷部分，主要放用户登录成功后，一些个人信息（注意：不要防敏感信息）
        Payload payload=new Payload(map);

        //第三部分 签名部分，（头部+载荷）通过密钥加密后得到的
        JWSObject jwsObject=new JWSObject(header,payload);
        JWSSigner jwsSigner=new MACSigner(KEY);

        //拿到密钥加密
        jwsObject.sign(jwsSigner);
        return jwsObject.serialize();
    }


    /**
     * 拿到jwt 根据系统密钥，看能不能解开
     * @return
     */
    public static boolean decode(String jwt) throws ParseException, JOSEException {
        //parse() 把字符串转成一个对象
        JWSObject jwsObject=JWSObject.parse(jwt);
        JWSVerifier jwsVerifier=new MACVerifier(KEY);
        //解密方法 verify()
        return jwsObject.verify(jwsVerifier);
    }

    /**
     * 根据jwt 获取其中载荷部分
     * @param jwt
     * @return
     */
    public static Map getPayLoad(String jwt) throws ParseException {
        //parse() 把字符串转成一个对象,并解密
        JWSObject jwsObject=JWSObject.parse(jwt);
        Payload payload=jwsObject.getPayload();
        Map<String,Object> map=payload.toJSONObject();
        return map;
    }
}