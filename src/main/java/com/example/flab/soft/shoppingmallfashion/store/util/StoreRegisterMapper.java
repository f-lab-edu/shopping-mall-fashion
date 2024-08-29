package com.example.flab.soft.shoppingmallfashion.store.util;

import com.example.flab.soft.shoppingmallfashion.store.controller.StoreRegisterRequest;
import com.example.flab.soft.shoppingmallfashion.store.repository.NewStoreRegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StoreRegisterMapper {

    StoreRegisterMapper INSTANCE = Mappers.getMapper(StoreRegisterMapper.class);

    NewStoreRegisterRequest toEntity(StoreRegisterRequest dto);

    StoreRegisterRequest toDto(NewStoreRegisterRequest entity);
}
