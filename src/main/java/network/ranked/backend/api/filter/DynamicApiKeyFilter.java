package network.ranked.backend.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import network.ranked.backend.api.service.ApiKeyService;
import network.ranked.backend.util.Common;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author JustReddy
 */
@Component
@RequiredArgsConstructor
public class DynamicApiKeyFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/api/") && !request.getRequestURI().equals("/api/key/request")) {

            final String key = request.getHeader("X-API-KEY");

            if (key == null || !apiKeyService.validateKey(key)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired API key");
                return;
            }

        }
        filterChain.doFilter(request, response);
    }
}
