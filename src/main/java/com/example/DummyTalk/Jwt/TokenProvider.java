package com.example.DummyTalk.Jwt;


import com.example.DummyTalk.AES.AESUtil;
import com.example.DummyTalk.Exception.TokenException;
import com.example.DummyTalk.User.DTO.TokenDTO;
import com.example.DummyTalk.User.Entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import software.amazon.awssdk.services.kms.KmsClient;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class TokenProvider {


    private static final String BEARER_TYPE = "Bearer";   // Bearer 토큰 사용시 앞에 붙이는 prefix문자열
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 8; // 8시간으로 설정

    private Key key;
    private final UserDetailsService userDetailsService;  // 사용자의 인증 및 권한 정보를 가져올수 있음

    private final AESUtil aesUtil;
    private final KmsClient kmsClient;

    public TokenProvider(UserDetailsService userDetailsService, AESUtil aesUtil, KmsClient kmsClient){

        String secretKey = "ejiSfPXxOMUuMXEU932MCy0adrbtkSlKeWcVZ0app6DpenURBmjaClhGTB4hR2dzzBhbMshXio46kUOtLs3tdw==";

        byte[] keyBytest = Decoders.BASE64.decode(secretKey);      // Decoders.BASE64.decode() : 해당 메소드를 사용하여 secretKey를 디코딩
        this.key = Keys.hmacShaKeyFor(keyBytest);                  // hmacShaKeyFor() : SecretKey를 생성
        this.userDetailsService = userDetailsService;
        this.aesUtil = aesUtil;
        this.kmsClient = kmsClient;
    }


    /* 1. 토큰(xxxxx.yyyyy.zzzzz) 생성 메소드 */
    public TokenDTO generateTokenDTO(User user) throws Exception {

        String decryptJWT = aesUtil.decrypt(kmsClient, user.getUserSecretKey());

        this.key = Keys.hmacShaKeyFor(decryptJWT.getBytes());

        /* 1. 회원 아이디를 "sub"이라는 클레임으로 토큰으로 추가 */
        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getUserId()));    // ex) { sub : memberId }

        claims.put("nickname", user.getNickname());
        claims.put("userName", user.getName());
        claims.put("national_language", user.getNationalLanguage());

        long now = System.currentTimeMillis();  // 현재시간을 밀리세컨드단위로 가져옴

        Date accessTokenExpriesIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME); // java.util.Date로 import

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(accessTokenExpriesIn) // 토큰의 만료기간을 DATE형으로 토큰에 추가(exp라는 클레임으로 long형으로 토큰에 추가)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return new TokenDTO(BEARER_TYPE, user.getUserEmail()
                , accessToken, accessTokenExpriesIn.getTime());
    }

    /* 2. 토큰의 등록된 클레임의 subject에서 해당 회원의 아이디를 추출 */
    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()          // payload의 Clamis 추출
                .getSubject();      // Claim중에 등록 클레임에 해당하는 sub값 추출(회원 아이디)
    }

    /* 3. AccessToken으로 인증 객체 추출 */
    public Authentication getAuthentication(String token){

        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /* 4. 토큰 유효성 검사 */
    public boolean validateToken(String token){

        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("[TokenProvider] 잘못된 JWT 서명입니다.");
            throw new TokenException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e){
            log.info("[TokenProvider] 만료된 JWT 토큰입니다.");
            throw new TokenException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e){
            log.info("[TokenProvider] 지원되지 않는 JWT 토큰입니다.");
            throw new TokenException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e){
            log.info("[TokenProvider] JWT 토큰이 잘못되었습니다.");
            throw new TokenException("JWT 토큰이 잘못되었습니다.");
        }
    }

}