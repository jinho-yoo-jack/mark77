package jack.labs.mark77.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jack.labs.mark77.dto.*;
import jack.labs.mark77.global.ApiResponse;
import jack.labs.mark77.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtService jwtService;
    private final boolean postOnly = true;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
        setFilterProcessesUrl("/api/v1/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            try {
                SignInDto requestDto = new ObjectMapper().readValue(request.getInputStream(), SignInDto.class);     // 2.
                String userId = requestDto.getUserId();
                String password = requestDto.getPassword();
                return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(userId, password));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // 로그인 성공 시
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((CustomUserDetails) authResult.getPrincipal()).getUsername();
        String role = ((CustomUserDetails) authResult.getPrincipal()).getAuthorities().stream().findFirst().orElseThrow().getAuthority();
        String token = jwtService.createToken(new CustomUserInfoDto(username, role));
        ApiResponse<String> responseMessage = ApiResponse.success(token);
        String responseJSON = new ObjectMapper().writeValueAsString(responseMessage);
        response.getWriter().write(responseJSON);
    }

    // 로그인 실패 시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }

}
