package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.modals.Address;
import com.example.demo.modals.User;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthService auth;
    private final UserRepository userRepo;
    private final AddressRepository addrRepo;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<User>> profile(@RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok(
                ApiResponse.success("OK", auth.getUserFromToken(jwt.substring(7)))
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<User>> update(
            @RequestHeader("Authorization") String jwt,
            @RequestBody User u) {

        User user = auth.getUserFromToken(jwt.substring(7));
        user.setFirstName(u.getFirstName());
        user.setLastName(u.getLastName());
        user.setMobile(u.getMobile());

        return ResponseEntity.ok(ApiResponse.success("Updated", userRepo.save(user)));
    }

    @PostMapping("/address")
    public ResponseEntity<ApiResponse<Address>> addAddr(
            @RequestHeader("Authorization") String jwt,
            @RequestBody Address a) {

        User user = auth.getUserFromToken(jwt.substring(7));
        a.setUser(user);

        return ResponseEntity.ok(ApiResponse.success("Added", addrRepo.save(a)));
    }

    @GetMapping("/addresses")
    public ResponseEntity<ApiResponse<List<Address>>> getAddrs(
            @RequestHeader("Authorization") String jwt) {

        User user = auth.getUserFromToken(jwt.substring(7));
        return ResponseEntity.ok(ApiResponse.success("OK", addrRepo.findByUserId(user.getId())));
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<ApiResponse<String>> delAddr(@PathVariable Long id) {
        addrRepo.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted", "success"));
    }
}