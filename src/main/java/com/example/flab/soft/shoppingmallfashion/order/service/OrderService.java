package com.example.flab.soft.shoppingmallfashion.order.service;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemOptionRepository;
import com.example.flab.soft.shoppingmallfashion.order.controller.OrderRequest;
import com.example.flab.soft.shoppingmallfashion.order.domain.Order;
import com.example.flab.soft.shoppingmallfashion.order.repository.OrderRepository;
import com.example.flab.soft.shoppingmallfashion.order.util.DeliveryInfoMapper;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderInfoDto order(OrderRequest orderRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        if (!itemOptionRepository.existsById(orderRequest.getItemOptionId())) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }
        ItemOption itemOption = itemOptionRepository.findByIdForUpdate(
                orderRequest.getItemOptionId(), orderRequest.getAmount())
                .orElseThrow(() -> new ApiException(ErrorEnum.OUT_OF_STOCK));

        itemOption.reduceStocksCount(orderRequest.getAmount());

        Order order = orderRepository.save(Order.of(itemOption, orderRequest.getAmount(),
                user, DeliveryInfoMapper.INSTANCE.toDeliveryInfo(orderRequest)));
        return OrderInfoDto.builder()
                .order(order)
                .itemOption(itemOption)
                .build();
    }
}
