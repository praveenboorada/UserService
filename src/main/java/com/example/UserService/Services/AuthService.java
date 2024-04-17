package com.example.UserService.Services;

import com.example.UserService.DTOs.UserDto;
import com.example.UserService.Models.Session;
import com.example.UserService.Models.SessionStatus;
import com.example.UserService.Models.User;
import com.example.UserService.Repositories.SessionRepository;
import com.example.UserService.Repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import java.util.*;

public class AuthService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    private  BCryptPasswordEncoder bCryptPasswordEncoder;
    public AuthService(UserRepository userRepository,SessionRepository sessionRepository,BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public ResponseEntity<UserDto> lauthogin(String email, String password){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            return null;
        }
        User user = userOptional.get();
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw new RuntimeException("wrong password entered");
        }

        UserDto userDto = new UserDto();
        userDto.setEmail(email);

//        String token = "RandomString123";
        MacAlgorithm alg = Jwts.SIG.HS256; //or HS384 or HS256
        SecretKey key = alg.key().build();
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("email",user.getEmail());
        jsonMap.put("roles", List.of(user.getRoles()));
        jsonMap.put("createAt",new Date());
//        jsonMap.put("expiryAt", DateUtils.);

        String message = "Hello World!";
//        byte[] content = message.getBytes(StandardCharsets.UTF_8);

// Create the compact JWS:
//        String jws = Jwts.builder().content(content, "text/plain").signWith(key, alg).compact();
            String jws = Jwts.builder().claims(jsonMap).signWith(key,alg).compact();
// Parse the compact JWS:
//        content = Jwts.parser().verifyWith(key).build().parseSignedContent(jws).getPayload();
//
//        assert message.equals(new String(content, StandardCharsets.UTF_8));
        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(jws);
        session.setUser(user);
        sessionRepository.save(session);
        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + jws);

        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto, headers, HttpStatus.OK);

        return response;

    }
    public UserDto signUp(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password)); // We should store the encrypted password in the DB for a user.

        User savedUser = userRepository.save(user);

        return UserDto.from(savedUser);
    }
    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return null;
        }

        Session session = sessionOptional.get();

        session.setSessionStatus(SessionStatus.ENDED);

        sessionRepository.save(session);

        return ResponseEntity.ok().build();
    }
}
