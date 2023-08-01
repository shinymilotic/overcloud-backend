package overcloud.blog.infrastructure.security.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import overcloud.blog.application.article.core.exception.InvalidDataException;
import overcloud.blog.application.role.core.RoleEntity;
import overcloud.blog.infrastructure.AuthError;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.authstrategy.AuthStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final Map<String, AuthStrategy> authStrategy;

    public AuthInterceptor(Map<String, AuthStrategy> authStrategy) {
        this.authStrategy = authStrategy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !"anonymousUser".equals(authentication.getPrincipal())) {
            List<RoleEntity> roleEntities = (List<RoleEntity>) authentication.getAuthorities();

            for (RoleEntity role : roleEntities) {
                String authority = role.getAuthority();
                switch (authority) {
                    case "ADMIN" -> authStrategy.get("adminAuthStrategy").auth(request);
                    case "USER" -> authStrategy.get("frontAuthStrategy").auth(request);
                    default -> throw new InvalidDataException(ApiError.from(AuthError.AUTHORIZE_FAILED));
                }
            }
        } else {
            authStrategy.get("guestAuthStrategy").auth(request);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}