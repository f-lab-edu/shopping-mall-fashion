package com.example.flab.soft.shoppingmallfashion.order.service;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemOptionRepository;
import com.example.flab.soft.shoppingmallfashion.order.controller.DeliveryInfoUpdateRequest;
import com.example.flab.soft.shoppingmallfashion.order.controller.OrderRequest;
import com.example.flab.soft.shoppingmallfashion.order.domain.Order;
import com.example.flab.soft.shoppingmallfashion.order.repository.OrderRepository;
import com.example.flab.soft.shoppingmallfashion.order.util.DeliveryInfoMapper;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final UserRepository userRepository;
    private final DeliveryInfoMapper deliveryInfoMapper = DeliveryInfoMapper.INSTANCE;

    @Transactional
    public OrderInfoDto order(OrderRequest orderRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        ItemOption itemOption = itemOptionRepository.findByIdForUpdate(orderRequest.getItemOptionId())
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        itemOption.reduceStocksCount(orderRequest.getOrderAmount());

        Order order = orderRepository.save(Order.of(orderRequest, itemOption,
                user, deliveryInfoMapper.toDeliveryInfo(orderRequest)));
        //TODO 결제 로직 추가
        order.setPaid();
        //TODO 주문 성공 알림 추가
        return OrderInfoDto.builder()
                .order(order)
                .itemOption(itemOption)
                .build();
    }

    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        if (!Objects.equals(order.getOrderer().getId(), userId)) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }

        order.cancel();
        itemOptionRepository.updateStocksCount(
                order.getItemOption().getId(), order.getOrderAmount());
    }

    @Transactional
    public void changeDeliveryInfo(DeliveryInfoUpdateRequest request,
                                   Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));

        if (!Objects.equals(order.getOrderer().getId(), userId)) {
            throw new ApiException(ErrorEnum.INVALID_REQUEST);
        }

        order.changeDeliveryInfo(deliveryInfoMapper.toDeliveryInfo(request));
    }
}
