package com.example.flab.soft.shoppingmallfashion.auth.jwt.dto;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubjectRoleDto {
    private String subject;
    private Role role;
}