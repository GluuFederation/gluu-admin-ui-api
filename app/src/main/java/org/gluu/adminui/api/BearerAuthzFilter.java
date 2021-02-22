package org.gluu.adminui.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.gluu.adminui.api.services.external.IdPService;
import org.gluu.adminui.api.domain.exceptions.RestCallException;
import org.gluu.adminui.api.domain.utils.RestHttpUtil;
import org.gluu.adminui.api.domain.ws.response.IntrospectionResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class BearerAuthzFilter extends BasicAuthenticationFilter {
	
	private final Pattern bearerPattern = Pattern.compile("Bearer\\s+(\\S+)");

    private final IdPService idPService;
    private final ObjectMapper objectMapper;

    public BearerAuthzFilter(AuthenticationManager authManager, IdPService idPService, ObjectMapper objectMapper) {
        super(authManager);
        this.idPService = idPService;
        this.objectMapper = objectMapper;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {

        try {
            String header = Optional.ofNullable(request.getHeader("Authorization")).orElse("");
            Matcher m = bearerPattern.matcher(header);

            if (m.find()) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(getAuthentication(m.group(1)));
                SecurityContextHolder.setContext(context);
            }
            filterChain.doFilter(request, response);
        } catch (RestCallException rce) {
            RestHttpUtil.write(response, objectMapper, rce);
        } catch (HttpClientErrorException rce) {
            RestHttpUtil.write(response, rce);
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
    }

	private UsernamePasswordAuthenticationToken getAuthentication(String token) throws Exception {
        IntrospectionResponse response = idPService.introspection(token);
        if (response != null) {
            List<SimpleGrantedAuthority> authorities = response.getScope().stream()
                    .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken(response, null, authorities);
        }
        return null;
	}
	
}