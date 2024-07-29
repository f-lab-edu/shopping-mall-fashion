package com.example.flab.soft.shoppingmallfashion.order.util;

import com.example.flab.soft.shoppingmallfashion.order.controller.OrderRequest;
import com.example.flab.soft.shoppingmallfashion.order.domain.DeliveryInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeliveryInfoMapper {
    DeliveryInfoMapper INSTANCE = Mappers.getMapper(DeliveryInfoMapper.class);

    @Mapping(source = "recipientName", target = "recipientName")
    @Mapping(source = "roadAddress", target = "roadAddress")
    @Mapping(source = "addressDetail", target = "addressDetail")
    DeliveryInfo toDeliveryInfo(OrderRequest orderRequest);
}
