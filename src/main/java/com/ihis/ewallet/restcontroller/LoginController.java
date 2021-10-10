package com.ihis.ewallet.restcontroller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ihis.ewallet.responsedtos.LoginRequest;
import com.ihis.ewallet.security.JwtUserDetailsService;
import com.ihis.ewallet.utils.JwtTokenUtil;

@RestController
public class LoginController {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@PostMapping(value = "/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest, HttpServletResponse response) throws Exception {
		
		String username = authenticationRequest.getEmail();
		
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(username);

		final String token = jwtTokenUtil.generateToken(userDetails);
		
		//add token to cookie
		Cookie cookie = new Cookie(JwtTokenUtil.JWT_TOKEN_COOKIE_NAME,token);
		cookie.setMaxAge(-1);
	    cookie.setSecure(true);
	    cookie.setHttpOnly(true);
	    cookie.setPath("/");
	    response.addCookie(cookie);
	    
	    //add csrf to header and cookie
	    String uniqueID = UUID.randomUUID().toString();
		Cookie xsrfCookie = new Cookie("XSRF-TOKEN",uniqueID);
		xsrfCookie.setMaxAge(-1);
	    response.addCookie(xsrfCookie);

		Map<String, String> respBody = new HashMap<>();
		respBody.put("token", token);
		return ResponseEntity.ok(respBody);
	}
	
	@RequestMapping(value = "/csrf", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<CsrfToken> getToken(final HttpServletRequest request) {
        return ResponseEntity.ok().body(new HttpSessionCsrfTokenRepository().generateToken(request));
    }
}
