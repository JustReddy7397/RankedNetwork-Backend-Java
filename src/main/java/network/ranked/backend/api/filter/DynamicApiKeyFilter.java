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
import java.io.InputStream;
import java.io.OutputStream;
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

            final String key = request.getHeader("API-KEY");

            if (key == null || !apiKeyService.validateKey(key)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("image/jpeg");
                try (final InputStream is = getClass()
                        .getClassLoader()
                        .getResourceAsStream("static/unwiped_asscheeks.jpg");
                     OutputStream os = response.getOutputStream()) {
                    if (is != null) {
                        is.transferTo(os);
                    }
                }

                return;
            }

        }
        filterChain.doFilter(request, response);
    }
}
