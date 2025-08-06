package chen.zhao;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    /**
     * 生成Jwt令牌
     */
    @Test
    public void testGenerateJwt() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", 1);
        dataMap.put("username", "admin");
        String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS256, "chenzhao")//指定加密算法以及密钥
                .addClaims(dataMap)//添加自定义的信息
                .setExpiration(new Date(System.currentTimeMillis() + 3600*1000) )//设置过期时间 毫秒 当前时间过后一个小时
                .compact();//生成令牌
        System.out.println(jwt);
    }

    /**
     * 解析Jwt令牌
     */
    @Test
    public void testParseJWT() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhZG1pbiIsImV4cCI6MTc1NDA5Njk2NH0.68LnT_MYSbepTOB-zZDzMvoGOMp713_hIOBaAbEV104";
        Claims claims = Jwts.parser().setSigningKey("chenzhao") //指定密钥
                .parseClaimsJws(token) //解析令牌
                .getBody(); //获取自定义信息

        System.out.println(claims);
    }
}
