package br.com.bcp;

import java.time.LocalTime;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {

    private static final Logger LOG = LoggerFactory.getLogger(UserInfoController.class);

    @GetMapping("/api/private")
    public Map<String, Object> privateResource(@AuthenticationPrincipal Jwt jwt) {
        LOG.info("\n***** JWT Headers: {}\n", jwt.getHeaders());
        LOG.info("\n***** JWT Claims: {}\n", jwt.getClaims().toString());
        LOG.info("\n***** JWT Token: {}\n", jwt.getTokenValue());
        LOG.info("\n***** JWT Audience: {}\n", jwt.getAudience());
        LOG.info("\n***** JWT Expires at: {}\n", jwt.getExpiresAt());
        LOG.info("\n***** JWT Issuer: {}\n", jwt.getIssuer());

        return jwt.getClaims();
    }

    @GetMapping("/user/name")
    public Map<String, Object> getUserName(@AuthenticationPrincipal Jwt principal) {
        return Collections.singletonMap("user_name", principal.getClaimAsString("preferred_username"));
    }

    @GetMapping("/user/info")
    public String getUserInfo(@AuthenticationPrincipal Jwt jwt) throws JsonProcessingException {
        LOG.info("\n***** JWT Token: {}\n", jwt.getTokenValue());
        Map<String, Object> claims = jwt.getClaims();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(claims);
    }

    @GetMapping("/api/public")
    public String publicResource() {
        return "Public resource " + LocalTime.now();
    }
}
