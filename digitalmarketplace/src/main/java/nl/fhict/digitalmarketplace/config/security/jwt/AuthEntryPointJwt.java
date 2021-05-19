package nl.fhict.digitalmarketplace.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.fhict.digitalmarketplace.model.response.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        MessageDTO responseMsgObj = new MessageDTO(authException.getMessage(),"ERROR", request.getRequestURI());
        String responseMshObjJson = objectMapper.writeValueAsString(responseMsgObj);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseMshObjJson);
        response.flushBuffer();
    }
}
