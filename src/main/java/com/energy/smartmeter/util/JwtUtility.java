package com.energy.smartmeter.util;

import com.energy.smartmeter.dao.AdminDao;
import com.energy.smartmeter.dao.ConsumerDao;
import com.energy.smartmeter.dao.SmartMeterDao;
import com.energy.smartmeter.dto.AdminDto;
import com.energy.smartmeter.dto.ConsumerDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility implements Serializable {

    @Autowired
    private AdminDao adminDao;
    @Autowired
    private ConsumerDao consumerDao;

    @Autowired
    private SmartMeterDao smartMeterDao;

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 5*60*60;

    private String secret = "54w65t5$$#$";

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    private Boolean ignoreTokenExpiration(String token) {
        return false;
    }

    public String generateToken(AdminDto adminDto, long validity) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, adminDto.getUsername(), validity);
    }


    public String generateToken(String username, long validity) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username, validity);
    }

    public String generateToken(ConsumerDto consumerDto, long validity) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, consumerDto.getUsername(), validity);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, long validity) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity*1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public Boolean validateConsumerToken(String token) {
        final String username = getUsernameFromToken(token);
        return (consumerDao.findConsumer(username)!=null && !isTokenExpired(token));
    }


    public Boolean validateAdminToken(String token) {
        final String username = getUsernameFromToken(token);
        return (adminDao.findAdminByUsername(username)!=null && !isTokenExpired(token));
    }

}
