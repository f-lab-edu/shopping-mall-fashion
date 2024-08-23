package com.example.flab.soft.shoppingmallfashion.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemOptionRepository;
import com.example.flab.soft.shoppingmallfashion.order.domain.DeliveryInfo;
import com.example.flab.soft.shoppingmallfashion.order.domain.Order;
import com.example.flab.soft.shoppingmallfashion.order.repository.OrderRepository;
import com.example.flab.soft.shoppingmallfashion.order.service.OrderService;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    ItemOptionRepository itemOptionRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    OrderService orderService;

    static final Long USER_ID = 1L;
    static final Long ORDER_ID = 1L;
    static final Order ORDER = Order.builder()
            .orderer(User.builder()
                    .id(USER_ID)
                    .email("valid.email@example.com")
                    .password("ValidPass1#")
                    .realName("Valid Name")
                    .cellphoneNumber("01012345678")
                    .nickname("validNick")
                    .build())
            .itemOption(mock(ItemOption.class))
            .orderAmount(1)
            .totalPrice(10000)
            .discountedAmount(1000)
            .paymentAmount(9000)
            .deliveryInfo(mock(DeliveryInfo.class))
            .build();

    @Test
    @DisplayName("주문자와 주문 취소 요청한 사용자가 다르면 예외")
    void whenCancelOrderOfOtherUser_thenThrowsException() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(ORDER));

        assertThatThrownBy(() -> orderService.cancelOrder(ORDER_ID, USER_ID + 1))
                .hasMessage(ErrorEnum.INVALID_REQUEST.getMessage());
    }
}
