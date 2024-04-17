package com.example.UserService.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Session extends BaseModel{
    private String token;
    private Date expiringAt;
    private User user;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus sessionStatus;



}
