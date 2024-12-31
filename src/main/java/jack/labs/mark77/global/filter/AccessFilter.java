package jack.labs.mark77.global.filter;

import jack.labs.mark77.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
public class AccessFilter extends OncePerRequestFilter {
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String waitingNumber = request.getHeader("WaitingNumber");
            Long rank = redisService.getRank("processingQueue", waitingNumber);
            String encodedRedirectURL = response.encodeRedirectURL(
                    request.getContextPath() + "/");

            response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
            response.setHeader("Location", encodedRedirectURL);
        } catch (Exception e) {
            String encodedRedirectURL = response.encodeRedirectURL(
                    request.getContextPath() + "/waiting");

            response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
            response.setHeader("Location", encodedRedirectURL);
        }
        filterChain.doFilter(request, response);
    }
}
