package com.example.flab.soft.shoppingmallfashion.auth.jwt.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TokensMapper {
    TokensMapper INSTANCE = Mappers.getMapper(TokensMapper.class);
    TokenResponse toTokenResponseDto(NewTokensDto newTokensDto);
}
