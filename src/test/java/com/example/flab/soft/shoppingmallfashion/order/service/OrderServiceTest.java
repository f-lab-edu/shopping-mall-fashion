package com.example.flab.soft.shoppingmallfashion.order.service;

import static org.assertj.core.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemOptionRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import com.example.flab.soft.shoppingmallfashion.order.controller.OrderRequest;
import com.example.flab.soft.shoppingmallfashion.order.domain.DeliveryInfo;
import com.example.flab.soft.shoppingmallfashion.order.domain.Order;
import com.example.flab.soft.shoppingmallfashion.order.domain.OrderStatus;
import com.example.flab.soft.shoppingmallfashion.order.repository.OrderRepository;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemOptionRepository itemOptionRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    ItemOption itemOption;
    ItemOption itemOption2;
    Order order;
    @BeforeEach
    void setUp() {
        Item item = itemRepository.findById(1L).get();

        itemOption = itemOptionRepository.save(ItemOption.builder()
                .name("test product")
                .size("L")
                .optionValue("red")
                .item(item)
                .saleState(SaleState.ON_SALE)
                .stocksCount(1L)
                .build());

        itemOption2 = itemOptionRepository.save(ItemOption.builder()
                .name("test product2")
                .size("XL")
                .optionValue("red")
                .item(item)
                .saleState(SaleState.ON_SALE)
                .stocksCount(1L)
                .build());

        order = Order.builder()
                .orderer(userRepository.findById(1L).get())
                .itemOption(itemOption2)
                .amount(1)
                .deliveryInfo(DeliveryInfo.builder()
                        .recipientName("name")
                        .roadAddress("road123")
                        .addressDetail("1-1")
                        .build())
                .build();
        orderRepository.save(order);
    }

    @Test
    @DisplayName("상품 주문시 재고 이상으로 주문시 예외, 주문은 생성되지 않으며 재고수는 감소하지 않는다")
    void whenOrderAmountIsBiggerThanStocks_thenThrowException() {
        OrderRequest orderRequest = OrderRequest.builder()
                .itemOptionId(itemOption.getId())
                .amount(2)
                .recipientName("name")
                .roadAddress("road123")
                .addressDetail("1-1")
                .build();
        long countBeforeOrder = orderRepository.count();

        assertThatThrownBy(() -> orderService.order(orderRequest, 1L)).isInstanceOf(ApiException.class);
        assertThat(itemOption.getStocksCount()).isEqualTo(1);
        assertThat(orderRepository.count()).isEqualTo(countBeforeOrder);
    }

    @Test
    @DisplayName("상품 주문시 주문 수량만큼 재고수 감소")
    void reduceStocksCountAsMuchAsOrderAmounts() {
        OrderRequest orderRequest = OrderRequest.builder()
                .itemOptionId(itemOption.getId())
                .amount(1)
                .recipientName("name")
                .roadAddress("road123")
                .addressDetail("1-1")
                .build();
        long countBeforeOrder = orderRepository.count();

        orderService.order(orderRequest, 1L);
        assertThat(itemOption.getStocksCount()).isEqualTo(0);
        assertThat(orderRepository.count()).isEqualTo(countBeforeOrder + 1);
    }

    @Test
    @DisplayName("이미 배송이 시작되면 주문 취소 불가")
    void whenDeliveryStarts_thenCannotCancelOrder() {
        long countBeforeOrder = orderRepository.count();
        order.startDelivery();
        assertThatThrownBy(() -> orderService.cancelOrder(order.getId(), 1L))
                .hasMessage(ErrorEnum.ALREADY_ON_DELIVERY.getMessage());

        assertThat(itemOption.getStocksCount()).isEqualTo(1);
        assertThat(orderRepository.count()).isEqualTo(countBeforeOrder);
    }

    @Test
    @DisplayName("주문 취소 성공시 주문 수량만큼 재고수 증가")
    void cancelOrder() {
        Long stocksCountBeforeCancel = itemOption2.getStocksCount();
        orderService.cancelOrder(order.getId(), 1L);

        assertThat(itemOptionRepository.findById(itemOption2.getId()).get().getStocksCount())
                .isEqualTo(stocksCountBeforeCancel + 1);
        assertThat(orderRepository.findById(order.getId()).get().getOrderStatus())
                .isEqualTo(OrderStatus.CANCELLED);
    }
}