package com.example.api_autho.rest;

import com.example.api_autho.dto.AuthenticationRequestDto;
import com.example.api_autho.dto.SignUpRequestDto;
import com.example.api_autho.model.User;
import com.example.api_autho.security.jwt.JwtTokenProvider;
import com.example.api_autho.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/auth/")
public class AuthenticationRestController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    requestDto.getEmail(),
                    requestDto.getPassword());
            authenticationManager.authenticate(authToken);

            User user = userService.findByEmail(requestDto.getEmail());
            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + requestDto.getEmail() + " not found");
            }
            String email = user.getEmail();

            String token = jwtTokenProvider.createToken(email);

            Map<Object, Object> response = new HashMap<>();
            response.put("username", email);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password", e);
        }
    }


    @PostMapping("signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequestDto signUpDto){
        String username = signUpDto.getUsername();
        User user = userService.findByUsername(username);

        if (user != null){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        String email = signUpDto.getEmail();
        user = userService.findByEmail(email);

        if (user != null) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        if (userService.checkEmail(email)) {
            return new ResponseEntity<>("Email is written incorrectly", HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        newUser.setUsername(signUpDto.getUsername());
        newUser.setEmail(signUpDto.getEmail());
        newUser.setRole(signUpDto.getRole());
        newUser.setPassword(signUpDto.getPassword());

        userService.register(newUser);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

    }
}
