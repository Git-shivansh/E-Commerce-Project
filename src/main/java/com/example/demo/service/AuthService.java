package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.modals.Seller;
import com.example.demo.modals.User;

public interface AuthService {

    AuthResponse signup(SignUpRequest req);

    AuthResponse login(LoginRequest req);

    AuthResponse sellerSignup(SignUpRequest req);

    String sendOtp(String email, String type);

    boolean verifyOtp(String email, String otp);

    User getUserFromToken(String token);

    Seller getSellerFromToken(String token);
}