package com.example.UserService.Controllers;

import com.example.UserService.DTOs.UserDto;
import com.example.UserService.DTOs.setUserRolesDto;
import com.example.UserService.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable("id") Long userId){
        UserDto userDto = userService.getUserDetails(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
    @PostMapping("/{id}/roles")
    public ResponseEntity<UserDto> getUserRoles(@PathVariable("id") Long userId, @RequestBody setUserRolesDto request){
        UserDto userDto = userService.setUserRoles(userId,request.getRoleIds());
    }

}
