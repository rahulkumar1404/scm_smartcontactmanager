package com.scm.SCM.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.SCM.entity.Providers;
import com.scm.SCM.entity.User;
import com.scm.SCM.helpers.AppConstants;
import com.scm.SCM.repository.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.var;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);
    
    @Autowired
    private UserRepo userRepo;

    @SuppressWarnings("null")
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication) throws IOException, ServletException {
        logger.info("onAuthenticationSuccessHandler ");

        //identify the provider
        var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String authroizedClientId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
        logger.info("authroizedClientId " + authroizedClientId);

        var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        oauthUser.getAttributes().forEach((key, value) -> {
            logger.info(key + " : " + value);
        });

        //user details basic
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setPassword(UUID.randomUUID().toString());

        if(authroizedClientId.equalsIgnoreCase("google")){
            //google
            //google attributes
            
            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setName(oauthUser.getAttribute("name").toString());
            user.setProfilePic(oauthUser.getAttribute("picture").toString());
            user.setProviderUserId(oauthUser.getAttribute("name").toString());
            user.setProvider(Providers.GOOGLE);
            user.setAbout("This account is created by google login");
        }
        else if(authroizedClientId.equalsIgnoreCase("github")){
            //github
            //github attributes
            String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString() : oauthUser.getAttribute("login").toString() + "@gmail.com";
            String picture = oauthUser.getAttribute("avatar_url").toString();
            String name = oauthUser.getAttribute("login").toString();
            
            user.setEmail(email);
            user.setName(name);
            user.setProfilePic(picture);
            user.setProviderUserId(oauthUser.getName());
            user.setProvider(Providers.GITHUB);
            user.setAbout("This account is created by github login");
        }
        else{
            logger.info("OAuthAuthenticationSuccessHandler : unknown provider");
        }

        
        User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);

        if(user2 == null){
            userRepo.save(user);
            logger.info("User saved: "+user.getEmail());
        }else{
            logger.info("User already exists: "+user.getEmail());
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }

}

