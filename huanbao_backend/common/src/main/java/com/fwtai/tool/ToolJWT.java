package com.fwtai.tool;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

/**
 * jwt(JSON Web Token)令牌工具类,采用非对称公钥密钥(私钥加密|公钥解密)
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-02-12 23:53
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class ToolJWT implements Serializable{

    //如设置Token过期时间15分钟，建议更换时间设置为Token前5分钟,通过try catch 获取过期
    private final static long access_token = 1000 * 60 * 45;//当 refreshToken 已过期了，再判断 accessToken 是否已过期,

    /**一般更换新的accessToken小于5分钟则提示需要更换新的accessToken*/
    private final static long refresh_token = 1000 * 60 * 40;//仅做token的是否需要更换新的accessToken标识,小于5分钟则提示需要更换新的accessToken

    private final static long app_access_token = 1000 * 60 * 60 * 24 * 20L;//20天

    private final static long app_refresh_token = 1000 * 60 * 60 * 24 * 7L;//7天

    private final static String issuer = "富翁泰科技有限责任公司(www.fwtai.com)";//jwt签发者

    /**2048的密钥位的公钥*/
    private final static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjDUSFkcdgBs5H9ailgQNVMT3tkeWYieB8V9tc20ZsjJ5mgMm23o8FFUZ8kMGKesSxQgthl2Ym5gtX2fa5S96tmOfGOugF4jHryik8dh2nK/GYTdZpL4GcHKPT5z7wR799GxzAn8VZEW9wU6S8GzmtCKbqxjIjic4s6l/01/GqDQ4XG0XjyCEn2a+rn/BdDMUouxhUvreSCEtVwB9KLo5RvZUH+/wIKt8idA5pZIcSBygG5zZIronNrki0DTRf2BJwCbli/hvgrMFq9x+VLjEfdonGghHu0EyGW4AogsH1ZXDqY6xEnm24nK/ooZZlA6jgPeH4FiBFBg96Kk9wNBW3QIDAQAB";
    /**2048的密钥位的私钥*/
    private final static String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCMNRIWRx2AGzkf1qKWBA1UxPe2R5ZiJ4HxX21zbRmyMnmaAybbejwUVRnyQwYp6xLFCC2GXZibmC1fZ9rlL3q2Y58Y66AXiMevKKTx2Hacr8ZhN1mkvgZwco9PnPvBHv30bHMCfxVkRb3BTpLwbOa0IpurGMiOJzizqX/TX8aoNDhcbRePIISfZr6uf8F0MxSi7GFS+t5IIS1XAH0oujlG9lQf7/Agq3yJ0DmlkhxIHKAbnNkiuic2uSLQNNF/YEnAJuWL+G+CswWr3H5UuMR92icaCEe7QTIZbgCiCwfVlcOpjrESebbicr+ihlmUDqOA94fgWIEUGD3oqT3A0FbdAgMBAAECggEAMf0B89trEy+hkE5wLVhIgH6xatXfMiOv2qURWuP9E2GXpWeWRQ4xuXYWCCAJZIyhZoph4NQiAueg1bErhCcHc1vVb4cizwE/YJ33IRryPy8UZbjcRzZRCWpu2g24XMR1+t1nq+4vbE7JNtCS5xGMt5DXO4tchgJJ3xSBtJUjOPjOIhBs+xJ7TmeLZA+fpeCRmP4U5V2JiJF/5HukJVXSpo1weLdZORSSBUAw3cHM45MFYqtQ51LRO71YvC91gdAeyDjb+RgwFwKV08yRA7qW6ve+7k5QNs2RljHCfI/c+BDvrmRIuTYgrnwz00CrwwqG9Qc5n8p6TY9U0kZb6FSX6QKBgQDFYQ8hnt41n5OOg/35uYQOZyrO0e3xeRCzgQStgsYvBK43TnkXUaEOgYP2wDWnnHC3xX7JiDWR90PXG/2/xkY7GC2kvEmrwq5IrAgrgSwDX5PjyIsGJMz4tR49suRGaCQ4t/CO/Bg92cqCoSKRzuQTxbMHHbXouCPixHVVWhIkEwKBgQC12S2LlREAhae9ENCUX1+N+/OOILKorsbCH9Ux6ec+Vw2u+IODR+OAcMt10gb9vPYR3QXlgNcCsXBh1yQYvjCCvj2HhbowmL6WlUQh6kbvYTV6tjS4n9W1C3ybtTY1dApF/llcSq0BV8AegngPVqjzb9Jrx3oDRLAS0EnAlFCXTwKBgQCpeoK0mqYwoxnfSGS+ldXXTCyMs+M88Xguubybg+vKGZnB8Ya2SCgjvX/jUv1/MyvyEAmi4GsnL4q33NgQYB6TxVoD1oznh92Alv7Y5Cw3pUzmTJ9bIiRB9brGvaGW9vsLHJ3ZGIt0KFrjuZpmWqCqfZoQpSk6b0qY/5nENPfYswKBgGzfYuJP1AcQ/jU2zPoYA7iZIN91+iD9yufMcTLaDGWJbxrTlMFTmAR9A4M9xIvDxzs4reAS/CeTnmoliWn67EZKqcHqsWeI+4piQZFglymYdJFMZ1Il96fWAbujiZoFKGVxe4TC/MNyCwb5K/Ja+voMd9mpawZr6FLLO2y+H2YHAoGBAJL3bqRgDaH8ZR5VKCs2zkMFicwaM/Fyd/pxYKaO5vDROlIqCJogX6efZmEcCFmmbWVCPYbrHA2BVRhv+p8flw+2vrUzDPcqH7zjRyTXGR2VLKmMIu8BmJvM7bsIwUyal17IE/aOAAZOmFU80obUNExQhpeoAeztVKGXq2Q3AqbX";

    /**java生成的私钥是pkcs8格式的公钥是x.509格式*/
    private static PublicKey getPublicKey(){
        try {
            final byte[] keyBytes = Base64.getDecoder().decode(publicKey);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static PrivateKey getPrivateKey(){
        try {
            final byte[] keyBytes = Base64.getDecoder().decode(privateKey);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // setSubject 不能和setClaims() 同时使用,如果用不到 userId() 的话可以把setId的值设为 userName !!!
    private static String createToken(final String userId,final Object value,final long expiryDate){
        final ExecutorService threadPool = Executors.newCachedThreadPool();
        final Future<String> future = threadPool.submit(new Callable<String>(){
            @Override
            public String call() throws Exception{
                final long date = System.currentTimeMillis();
                final JwtBuilder builder = Jwts.builder().signWith(getPrivateKey(),SignatureAlgorithm.RS384);
                if(value !=null){
                    builder.claim(userId,value);
                }
                return builder.setId(userId).setIssuer(issuer).setIssuedAt(new Date(date)).setExpiration(new Date(date + expiryDate)).compact();
            }
        });
        try {
            return future.get();
        } catch (final Exception e) {
            return null;
        }finally{
            threadPool.shutdown();
        }
    }

    private static String createToken(final String userId,final Object value,final HashMap<String,Object> claims,final long expiryDate){
        final ExecutorService threadPool = Executors.newCachedThreadPool();
        final Future<String> future = threadPool.submit(new Callable<String>(){
            @Override
            public String call() throws Exception{
                final long date = System.currentTimeMillis();
                final JwtBuilder builder = Jwts.builder().signWith(getPrivateKey(),SignatureAlgorithm.RS384);
                if(value != null){
                    builder.claim(userId,value);
                }
                if(claims !=null && claims.size()>0){
                    for(final String key : claims.keySet()){
                        builder.claim(key,claims.get(key));
                    }
                }
                return builder.setId(userId).setIssuer(issuer).setIssuedAt(new Date(date)).setExpiration(new Date(date + expiryDate)).compact();
            }
        });
        try {
            return future.get();
        } catch (final Exception e) {
            return null;
        }finally{
            threadPool.shutdown();
        }
    }

    public static Claims parser(final String token){
        return Jwts.parserBuilder().requireIssuer(issuer).setSigningKey(getPublicKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * 验证token是否已失效,返回true已失效,否则有效
     * @param token
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020年2月24日 16:19:00
    */
    public static boolean tokenExpired(final String token) {
        try {
            return parser(token).getExpiration().before(new Date());
        } catch (final ExpiredJwtException exp) {
            return true;
        }
    }

    public static boolean validateToken(final String token,final String userId){
        final String uid = extractUserId(token);
        return (uid.equals(userId) && !tokenExpired(token));
    }

    /**仅作为是否需要刷新的accessToken标识,不做任何业务处理*/
    public static String expireRefreshToken(final String userId){
        return createToken(userId,null,refresh_token);
    }

    /**生成带认证实体且有权限的token,最后个参数是含List<String>的角色信息*/
    public static String expireAccessToken(final String userId){
        return createToken(userId,null,access_token);
    }

    public static String buildRefreshToken(final String userId){
        return createToken(userId,null,app_refresh_token);
    }

    public static String buildAccessToken(final String userId){
        return createToken(userId,null,app_access_token);
    }

    /**其实id就是本项目的userId*/
    public static String getId(final String token){
        return parser(token).getId();
    }

    /**生成带认证实体且有权限的token,附加参数,如区域等级*/
    public static String expireAccessToken(final String userId,final HashMap<String,Object> claims){
        return createToken(userId,null,claims,access_token);
    }

    private static <T> T extractObjet(final String token,final Function<Claims,T> claimsResolver){
        final Claims claims = parser(token);
        return claimsResolver.apply(claims);
    }

    public static String extractUserId(final String token){
        return extractObjet(token,Claims::getId);
        /* ok的
            return extractObjet(token,(claims -> {
                return claims.getId();
            }));
        */
        //return extractObjet(token,(claims -> claims.getId()));ok的
    }

    //指定key获取对应的value
    private static Object extractKey(final String token,final String claimKey){
        final Claims claims = parser(token);
        return claims.get(claimKey);
    }

    public static Object getClaimKey(final String token,final String claimKey){
        return extractKey(token,claimKey);
    }
}