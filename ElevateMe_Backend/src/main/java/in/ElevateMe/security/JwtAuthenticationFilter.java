package in.ElevateMe.security;


import in.ElevateMe.Document.User;
import in.ElevateMe.repository.UserRepository;
import in.ElevateMe.service.AuthService;
import in.ElevateMe.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeder=request.getHeader("Authorization");
        String token =null;
        String userId= null;

        if (authHeder != null && authHeder.startsWith("Bearer")){
            token= authHeder.substring(7);
            try {
                userId=jwtUtil.getUserIdFromToken(token);
            } catch (Exception e) {
                log.error("Token is not valid/available");
            }

        }
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
            try {
                if (jwtUtil.validateToken(token) && !jwtUtil.isTokenExpired(token)){
                    User user=userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User not found"));
                    UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(user,null,new ArrayList<>());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e){
                    log.error("Exception occured while validating the token");
            }

        }
        filterChain.doFilter(request,response);
    }
}
