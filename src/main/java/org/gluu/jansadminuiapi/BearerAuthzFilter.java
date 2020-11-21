package org.gluu.jansadminuiapi;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.BodyInserters;

@Slf4j
public class BearerAuthzFilter extends BasicAuthenticationFilter /*GenericFilterBean*/ {
	
	private Pattern bearerPattern = Pattern.compile("Bearer\\s+(\\S+)");
	
	@Autowired
    private WebClient webClient;
  
    /*
    */
    public BearerAuthzFilter(AuthenticationManager authManager) {
        super(authManager);
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
    
        try {    	    
            String header = Optional.ofNullable(request.getHeader("Authorization")).orElse("");
            Matcher m = bearerPattern.matcher(header);
        
            if (m.find()) {
			    // Set the security context - as in Spring Security Reference Guide 
    			SecurityContext context = SecurityContextHolder.createEmptyContext();
			    context.setAuthentication(getAuthentication(m.group(1)));
			    SecurityContextHolder.setContext(context);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
    }
          
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		
		try {
			// Issue a POST to introspection endpoint passing token as one FORM parameter, and retrieve
			// response as opaque map. For status codes >= 400 an exception is thrown by bodyToMono
    		Map<String, Object> response = webClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("token", token))
                .header("Authorization", "Bearer " + token)		// Useful if endpoint is protected
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
                
            if (response != null && Boolean.TRUE.equals(response.get("active"))) {
            	// Build stream of authorities from scopes found in response
            	// See https://github.com/GluuFederation/oxAuth/issues/1499
            	Stream<SimpleGrantedAuthority> authorities = Optional.ofNullable(response.get("scope"))
                    .map(Collection.class::cast).map(Collection::stream).orElse(Stream.empty())
                    .map(o -> new SimpleGrantedAuthority(o.toString()));
                
                return new UsernamePasswordAuthenticationToken(token, null, authorities.collect(Collectors.toList()));
                
            } else {
            	log.warn("Token introspection failed"); 
            }
                
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
        return null;

	}
	
}