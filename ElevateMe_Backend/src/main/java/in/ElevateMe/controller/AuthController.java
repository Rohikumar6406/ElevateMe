package in.ElevateMe.controller;


import in.ElevateMe.dto.AuthResponse;
import in.ElevateMe.dto.LoginRequest;
import in.ElevateMe.dto.RegisterRequest;
import in.ElevateMe.service.AuthService;
import in.ElevateMe.service.FileUploadservice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static in.ElevateMe.util.AppConstants.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(AUTH_CONTROLLER)
public class AuthController {


    private final AuthService authService;

    private final FileUploadservice fileUploadservice;

    @PostMapping(REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){
            log.info("Inside AuthController - register(): {}",request);
           AuthResponse response= authService.register(request);
           log.info("Response from service: {}",response);
           return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(VERIFY_EMAIL)
    public ResponseEntity<?> verifyEmail(@RequestParam String token){
        log.info("Inside AuthController - verifyEmail(): {}",token);
        authService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message","Email verified successfully"));
    }

    @PostMapping(UPLOAD_PROFILE)
    public ResponseEntity<?> uploadImage(@RequestPart("image")MultipartFile file) throws IOException {
        log.info("Inside AuthController-uploadImage()");
           Map<String,String> response= fileUploadservice.uploadSingleImage(file);
           return ResponseEntity.ok(response);
    }

    @PostMapping(LOGIN)
    public  ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        AuthResponse response=authService.login(request);
        return  ResponseEntity.ok(response);
    }

    //api endpoint for recent varification

    @PostMapping(RESEND_VERIFICATION)
    public ResponseEntity<?> resendVerification(@RequestBody Map<String,String> body){
        //Step1: Get the email from request
        String email=body.get("email");

        //Step2: Add the validation
        if (Objects.isNull(email)){
            return ResponseEntity.badRequest().body(Map.of("message","Email is required"));
        }

        //step 3: Call the service method to resend verification link

        authService.resendVerification(email);

        //step4: return response
        return ResponseEntity.ok(Map.of("success",true,"message","Verification email sent"));

    }
    @GetMapping(PROFILE)
    public ResponseEntity<?> getProfile(Authentication authentication){

        //step1: Get the principal object
        Object principalObject = authentication.getPrincipal();

        //step2: Call the service method
        AuthResponse currentProfile=authService.getProfile(principalObject);

        //step 3: return the response
        return ResponseEntity.ok(currentProfile);

    }



}


