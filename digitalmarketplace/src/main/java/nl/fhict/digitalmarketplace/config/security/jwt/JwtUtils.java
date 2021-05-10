package nl.fhict.digitalmarketplace.config.security.jwt;

import nl.fhict.digitalmarketplace.config.security.UserDetailsImpl;
import nl.fhict.digitalmarketplace.customException.AccessTokenExpiredException;
import nl.fhict.digitalmarketplace.customException.AccessTokenMalformedException;
import nl.fhict.digitalmarketplace.customException.AccessTokenMissingException;
import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.model.user.Role;
import nl.fhict.digitalmarketplace.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${bludevil.app.jwtSecret}")
    private String jwtSecret;
    @Value("${bludevil.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication){
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        List<String> scopes = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        scopes = scopes.stream().map(s -> {
            if(s.equals("ROLE_USER")){
                return "MEM";
            }
            if(s.equals("ROLE_ADMIN")){
                return "ADM";
            }
            else return "";
        }).collect(Collectors.toList());
        Claims claims = Jwts.claims().setSubject(userPrincipal.getUsername());
        claims.put("scopes", scopes);
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()+ jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateTokenFromUsername(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateTokenFromUser(@NotNull User user){
        List<Role> roles = user.getRoles();
        List<String> scopes = roles.stream().map(r -> {
            if(r.getName() == ERole.ROLE_USER){
                return "MEM";
            }
            if(r.getName() == ERole.ROLE_ADMIN){
                return "ADM";
            }
            else return "";
        }).collect(Collectors.toList());
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("scopes", scopes);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()+ jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            throw new AccessTokenMalformedException(e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            throw new AccessTokenMalformedException(e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
            throw new AccessTokenExpiredException(e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
            throw new AccessTokenMalformedException(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
            throw new AccessTokenMissingException(e.getMessage());
        }
    }
}
