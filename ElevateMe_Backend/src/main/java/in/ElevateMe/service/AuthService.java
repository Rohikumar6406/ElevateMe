package in.ElevateMe.service;


import in.ElevateMe.Document.User;
import in.ElevateMe.dto.AuthResponse;
import in.ElevateMe.dto.LoginRequest;
import in.ElevateMe.dto.RegisterRequest;
import in.ElevateMe.exception.ResorceExistsException;
import in.ElevateMe.repository.UserRepository;
import in.ElevateMe.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${app.base.url:http://localhost:8080}")
    private String appBaseUrl;

    private  final EmailService emailService;

    private  final JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);


    public AuthResponse register(RegisterRequest request){
        // So we can easily detect error during debug
        log.info("Inside AuthService: register() {}",request);


        //if user already have account
        if (userRepository.existsByEmail(request.getEmail())){
            throw  new ResorceExistsException("User already exists with this email");
        }
        User newUser= toDocument(request);

        userRepository.save(newUser);

        // Send verification email
        sendVerificationEmail(newUser);

        return toResponse(newUser);
    }

    private void sendVerificationEmail(User newUser) {
        log.info("Inside AuthService - sendVerificationEmail(): {}", newUser);
        try {
            String link = appBaseUrl+"/api/auth/verify-email?token="+newUser.getVerificationToken();
            String html = "<div style='font-family:sans-serif'>" +
                    "<h2>Verify your email</h2>" +
                    "<p>Hi " + newUser.getName() + ", please confirm your email to activate your account.</p>" +
                    "<p><a href='" + link
                    + "' style='display:inline-block;padding:10px 16px;background:#6366f1;color:#fff;border-radius:6px;text-decoration:none'>Verify Email</a></p>"
                    +
                    "<p>Or copy this link: " + link + "</p>" +
                    "<p>This link expires in 24 hours.</p>" +
                    "</div>";
            emailService.sendHtmlEmail(newUser.getEmail(), "Verify your email", html);
        }catch (Exception e) {
            log.error("Exception occured at sendVerificationEmail(): {}", e.getMessage());
            throw new RuntimeException("Failed to send verification email: "+e.getMessage());
        }
    }

    private  AuthResponse toResponse(User newUser){
        return AuthResponse.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .email(newUser.getEmail())
                .profileImageUrl(newUser.getProfileImageUrl())
                .emailVerified(newUser.isEmailVerified())
                .subscriptionPlan(newUser.getSubscriptionPlan())
                .createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())
                .build();

    }

    private User toDocument(RegisterRequest request){
        // new email to be saved in db
        return  User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .profileImageUrl(request.getProfileImageUrl())
                .subscriptionPlan("Basic")
                .emailVerified(false)
                .verificationToken(UUID.randomUUID().toString())
                .verificationExpires(LocalDateTime.now().plusHours(24))
                .build();
    }

    public void verifyEmail(String token){
        log.info("Inside AuthService: verifyEmail(): {}",token);
        User user=userRepository.findByVerificationToken(token)
                .orElseThrow(()->new RuntimeException("Invalid or expired verification token"));

        if (user.getVerificationExpires() != null && user.getVerificationExpires().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Verification token has expired. Please request new one");
        }
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationExpires(null);
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request){
        User existingUser=userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new UsernameNotFoundException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), existingUser.getPassword())){
            throw new UsernameNotFoundException("Invalid email or password");
        }

        // for verification if not throw exception
        if (!existingUser.isEmailVerified()){
            throw new RuntimeException("Please verify your email before loggin in");
        }


        String token = jwtUtil.generateToken(existingUser.getId());

        AuthResponse response=toResponse(existingUser);
        response.setToken(token);
        return response;
    }

    public void resendVerification(String email){
        //Step1: Fetch user account by email
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));

        //step2: Check the email is verified
        if (user.isEmailVerified()){
            throw new RuntimeException("Email is already verified");
        }

        //step 3:Set the new verification token and expires time

        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerificationExpires(LocalDateTime.now().plusHours(24));

        //step4: update the user
        userRepository.save(user);

        //step5: resend the verification email
        sendVerificationEmail(user);
    }

    public AuthResponse getProfile(Object principalObject){
        User existingUser= (User) principalObject;
        return toResponse(existingUser);

    }
}
