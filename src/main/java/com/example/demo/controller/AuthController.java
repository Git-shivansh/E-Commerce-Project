package  com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest req) {
        return ResponseEntity.ok(authService.signup(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/seller/signup")
    public ResponseEntity<AuthResponse> sellerSignup(@Valid @RequestBody SignupRequest req) {
        return ResponseEntity.ok(authService.sellerSignup(req));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtp(@RequestParam String email,
                                                        @RequestParam(defaultValue = "EMAIL") String type) {
        return ResponseEntity.ok(ApiResponse.success("OTP sent", authService.sendOtp(email, type)));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Boolean>> verifyOtp(@RequestParam String email,
                                                        @RequestParam String otp) {
        boolean ok = authService.verifyOtp(email, otp);

        return ResponseEntity.ok(
                ok ? ApiResponse.success("Verified", true)
                : ApiResponse.error("Invalid OTP")
        );
    }
}