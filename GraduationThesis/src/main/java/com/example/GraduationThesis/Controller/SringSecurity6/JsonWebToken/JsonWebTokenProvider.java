package com.example.GraduationThesis.Controller.SringSecurity6.JsonWebToken;

import com.example.GraduationThesis.Controller.SringSecurity6.UserData.CustomUserDetails;
import com.example.GraduationThesis.Model.Enitity.GlobalConfig.JWT;
import com.example.GraduationThesis.Service.DataBase.InterfaceService.User.JWTService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JsonWebTokenProvider {

    @Autowired
    private JWTService jwtService;


    public String generateToken(CustomUserDetails userDetails) {
        List<JWT> list = jwtService.findAll();
        Long JWT_EXPIRATION = list.get(0).getJwtExpiration();
        String JWT_SECRET = list.get(0).getJwtSecret();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        // generate json web token string from user id
        return Jwts.builder()
                .setSubject(Long.toString(userDetails.getUser().getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {

        List<JWT> list = jwtService.findAll();
        String JWT_SECRET = list.get(0).getJwtSecret();

        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {

        List<JWT> list = jwtService.findAll();
        String JWT_SECRET = list.get(0).getJwtSecret();

        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
