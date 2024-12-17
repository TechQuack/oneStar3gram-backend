package techquack.com.onestar3gram.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import techquack.com.onestar3gram.services.AppUserService;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final AppUserService service;

    public CustomAuthenticationSuccessHandler(AppUserService appUserService) {
        this.service = appUserService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        if (service.getUser(oauthUser) == null) {
            service.createUser(oauthUser);
        }
        response.sendRedirect("/");
    }
}

