package io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.filter;

import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.service.JwtService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.service.SecurityUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final SecurityUserDetailsService securityUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            String token = header.substring(7);
            if(jwtService.validate(token)){
                Long userId = Long.parseLong(jwtService.getUserId(token));
                UserDetails userDetails = securityUserDetailsService.loadUserByUserId(userId);
                if(!userDetails.isAccountNonExpired()) throw new AccountExpiredException("Account expired!");
                if(!userDetails.isEnabled()) throw new DisabledException("Account disabled!");
                if(!userDetails.isAccountNonLocked()) throw new LockedException("Account locked!");
                if(!userDetails.isCredentialsNonExpired()) throw new CredentialsExpiredException("Account credentials expired!");
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userId, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
