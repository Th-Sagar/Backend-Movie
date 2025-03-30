package com.movieapi.Movie_Api.controller;


import com.movieapi.Movie_Api.auth.entities.ForgotPassword;
import com.movieapi.Movie_Api.auth.entities.User;
import com.movieapi.Movie_Api.auth.repositories.ForgotPasswordRepository;
import com.movieapi.Movie_Api.auth.repositories.UserRepository;
import com.movieapi.Movie_Api.auth.services.UserService;
import com.movieapi.Movie_Api.auth.utils.ChangePassword;
import com.movieapi.Movie_Api.dto.MailBody;
import com.movieapi.Movie_Api.service.EmailService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/forgotPassword")
@CrossOrigin(origins = "*")
public class ForgotPasswordController {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private EmailService emailService;

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;


    // send mail for verification

    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email){

        User user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException(" Please enter valid email"));

        int otp = otpGenerator();
        MailBody mailBody = MailBody
                .builder()
                .to(email)
                .text("This is the OTP for your Forgot Password request: " + otp)
                .subject("OTP for Forgot Password request")
                .build();

        ForgotPassword fp = ForgotPassword
                .builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis()+20*100000))
                .user(user)
                .build();
        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);

        return new ResponseEntity<>("Email sent for verification", HttpStatus.OK);
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email){

        User user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException(" Please enter valid email"));

        ForgotPassword fp=forgotPasswordRepository.findByOtpAndUser(otp,user.getUserId()).orElseThrow(()-> new RuntimeException("Invalid OTP for email: "+ email));

        if(fp.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(fp.getFpid());
            return new ResponseEntity<>("OTP has expired!",HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>("OTP verify",HttpStatus.OK);
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword, @PathVariable String email){

        if(!Objects.equals(changePassword.password(), changePassword.repeatPassword())){
            return new ResponseEntity<>("Please enter the password again!",HttpStatus.EXPECTATION_FAILED);
        }

        String encodedePassword = passwordEncoder.encode(changePassword.password());
       boolean isUpdated = userService.updatePassword(email,encodedePassword);

        if (isUpdated) {
            return new ResponseEntity<>("Password updated successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }

    }

    private Integer otpGenerator(){
        Random random = new Random();
        return random.nextInt(100_000,999_999);
    }
}
