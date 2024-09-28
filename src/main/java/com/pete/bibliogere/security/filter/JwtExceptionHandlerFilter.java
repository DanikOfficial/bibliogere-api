package com.pete.bibliogere.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            System.out.println("🔵 JwtExceptionHandlerFilter - START");
            filterChain.doFilter(request, response);
            System.out.println("🔵 JwtExceptionHandlerFilter - END");

        } catch (ExpiredJwtException ex) {
            // Token expirado
            System.out.println("❌ Token expirado: " + ex.getMessage());
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token expirado. Faça login novamente.");

        } catch (JwtException ex) {
            // Token inválido
            System.out.println("❌ Token inválido: " + ex.getMessage());
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token inválido.");

        } catch (Exception ex) {
            // Erro inesperado
            System.out.println("❌ Erro inesperado: " + ex.getMessage());
            ex.printStackTrace();
            setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor.");
        }
    }

    /**
     * Creates error response matching ApiError<T> interface
     * {
     *   error: boolean,
     *   message: string,
     *   errors?: T
     * }
     */
    private void setErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> body = new HashMap<>();
        body.put("error", true);          // ApiError.error
        body.put("message", message);     // ApiError.message
        // body.put("errors", null);      // ApiError.errors (optional, not needed for JWT errors)

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(body));
    }
}
