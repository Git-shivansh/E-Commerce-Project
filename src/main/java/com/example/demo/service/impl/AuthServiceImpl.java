package com.example.demo.service.impl;

import com.example.demo.domain.USER_ROLE;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.exception.UserException;
import com.example.demo.modals.Cart;
import com.example.demo.modals.Seller;
import com.example.demo.modals.SellerReport;
import com.example.demo.modals.User;
import com.example.demo.modals.VerificationCode;
import com.example.demo.modals.Wishlist;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.SellerReportRepository;
import com.example.demo.repository.SellerRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationCodeRepository;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final SellerRepository sellerRepo;
    private final VerificationCodeRepository vcRepo;
    private final CartRepository cartRepo;
    private final WishlistRepository wishlistRepo;
    private final SellerReportRepository reportRepo;

    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwt;
    private final AuthenticationManager authManager;
    private final JavaMailSender mailer;

    @Override
    public AuthResponse signup(SignUpRequest req) {

        if (userRepo.existsByEmail(req.getEmail())) {
            throw new UserException("Email already registered");
        }

        User user = User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .mobile(req.getMobile())
                .role(USER_ROLE.ROLE_CUSTOMER)
                .build();

        userRepo.save(user);

        cartRepo.save(
                Cart.builder()
                        .user(user)
                        .build()
        );

        wishlistRepo.save(
                Wishlist.builder()
                        .user(user)
                        .build()
        );

        return AuthResponse.builder()
                .token(jwt.generateToken(user.getEmail()))
                .message("Registration successful")
                .role(user.getRole())
                .status(true)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest req) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword()
                )
        );

        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new UserException("User not found"));

        return AuthResponse.builder()
                .token(jwt.generateToken(user.getEmail()))
                .message("Login successful")
                .role(user.getRole())
                .status(true)
                .build();
    }

    @Override
    public AuthResponse sellerSignup(SignUpRequest req) {

        if (sellerRepo.existsByEmail(req.getEmail())) {
            throw new UserException("Seller email already registered");
        }

        Seller seller = Seller.builder()
                .sellerName(req.getFirstName() + " " + req.getLastName())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .mobile(req.getMobile())
                .build();

        sellerRepo.save(seller);

        reportRepo.save(
                SellerReport.builder()
                        .seller(seller)
                        .build()
        );

        return AuthResponse.builder()
                .token(jwt.generateToken(seller.getEmail()))
                .message("Seller registration successful")
                .role(seller.getRole())
                .status(true)
                .build();
    }

    @Override
    public String sendOtp(String email, String type) {

        String otp = String.format(
                "%06d",
                new Random().nextInt(999999)
        );

        VerificationCode vc = VerificationCode.builder()
                .otp(otp)
                .email(email)
                .verificationType(type)
                .build();

        userRepo.findByEmail(email)
                .ifPresent(vc::setUser);

        vcRepo.save(vc);

        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(email);
        msg.setSubject("Your OTP - ECommerce");
        msg.setText("Your OTP: " + otp + " (valid 15 min)");

        mailer.send(msg);

        return "OTP sent to " + email;
    }

    @Override
    public boolean verifyOtp(String email, String otp) {

        return vcRepo.findByEmailAndOtpAndIsUsedFalse(email, otp)
                .map(vc -> {

                    vc.setUsed(true);
                    vcRepo.save(vc);

                    userRepo.findByEmail(email)
                            .ifPresent(u -> {
                                u.setEmailVerified(true);
                                userRepo.save(u);
                            });

                    return true;
                })
                .orElse(false);
    }

    @Override
    public User getUserFromToken(String token) {

        String email = jwt.getEmail(token);

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found"));
    }

    @Override
    public Seller getSellerFromToken(String token) {

        String email = jwt.getEmail(token);

        return sellerRepo.findByEmail(email)
                .orElseThrow(() -> new UserException("Seller not found"));
    }
}