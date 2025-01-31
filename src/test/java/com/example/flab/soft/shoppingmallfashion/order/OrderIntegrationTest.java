package com.example.flab.soft.shoppingmallfashion.order;

import static org.assertj.core.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.coupon.domain.Coupon;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.Discount;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.DiscountType;
import com.example.flab.soft.shoppingmallfashion.coupon.domain.UserCoupon;
import com.example.flab.soft.shoppingmallfashion.coupon.repository.CouponRepository;
import com.example.flab.soft.shoppingmallfashion.coupon.repository.UserCouponRepository;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemOptionRepository;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import com.example.flab.soft.shoppingmallfashion.order.controller.DeliveryInfoUpdateRequest;
import com.example.flab.soft.shoppingmallfashion.order.controller.OrderRequest;
import com.example.flab.soft.shoppingmallfashion.order.domain.DeliveryInfo;
import com.example.flab.soft.shoppingmallfashion.order.domain.Order;
import com.example.flab.soft.shoppingmallfashion.order.domain.OrderStatus;
import com.example.flab.soft.shoppingmallfashion.order.domain.PaymentStatus;
import com.example.flab.soft.shoppingmallfashion.order.repository.OrderRepository;
import com.example.flab.soft.shoppingmallfashion.order.service.OrderInfoDto;
import com.example.flab.soft.shoppingmallfashion.order.service.OrderService;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderIntegrationTest {
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
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;

    Item item;
    ItemOption itemOption;
    ItemOption itemOption2;
    Order order;
    @BeforeEach
    void setUp() {
        item = itemRepository.findById(1L).get();

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
                .item(item)
                .itemOption(itemOption2)
                .orderAmount(1)
                .totalPrice(10000)
                .discountedAmount(0)
                .couponDiscountAmount(0)
                .paymentAmount(10000)
                .couponDiscountAmount(0)
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
                .orderAmount(2)
                .recipientName("name")
                .roadAddress("road123")
                .addressDetail("1-1")
                .isCouponUsed(false)
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
                .orderAmount(1)
                .totalPrice(10000)
                .discountedAmount(0)
                .couponDiscountAmount(0)
                .paymentAmount(10000)
                .recipientName("name")
                .roadAddress("road123")
                .addressDetail("1-1")
                .isCouponUsed(false)
                .build();
        long countBeforeOrder = orderRepository.count();

        orderService.order(orderRequest, 1L);
        assertThat(itemOption.getStocksCount()).isEqualTo(0);
        assertThat(orderRepository.count()).isEqualTo(countBeforeOrder + 1);
    }

    @Test
    @DisplayName("주문시 쿠폰 사용")
    void useCouponWhenOrder() {
        Coupon coupon = couponRepository.save(Coupon.builder()
                .name("welcomeCoupon")
                .amounts(1L)
                .discount(Discount.builder()
                        .discountType(DiscountType.FIXED_DISCOUNT_KRW)
                        .discountAmount(1000)
                        .build())
                .validation(Duration.of(3, ChronoUnit.DAYS))
                .build());
        Long userId = 1L;

        UserCoupon userCoupon = userCouponRepository.save(UserCoupon.builder()
                .coupon(coupon)
                .userId(userId)
                .validation(Duration.ofDays(1))
                .build());

        OrderRequest orderRequest = OrderRequest.builder()
                .itemOptionId(itemOption.getId())
                .orderAmount(1)
                .totalPrice(10000)
                .discountedAmount(0)
                .couponDiscountAmount(1000)
                .paymentAmount(9000)
                .recipientName("name")
                .roadAddress("road123")
                .addressDetail("1-1")
                .isCouponUsed(true)
                .usedCouponId(coupon.getId())
                .build();

        OrderInfoDto orderInfoDto = orderService.order(orderRequest, 1L);

        Order orderWithCoupon = orderRepository.findById(orderInfoDto.getOrderId()).get();
        assertThat(userCoupon.isUsed()).isTrue();
        assertThat(userCoupon.getUsageInfo().getUsedOrderId()).isEqualTo(orderWithCoupon.getId());
        assertThat(orderWithCoupon.getUsedCouponInfo().getUsedUserCouponId()).isEqualTo(userCoupon.getId());
        assertThat(orderWithCoupon.getUsedCouponInfo().getUsedCouponName()).isEqualTo(coupon.getName());
    }

    @Test
    @DisplayName("상품 주문시 계산 금액 = 총 금액 - 할인 금액 - 쿠폰 할인 금액")
    void paymentAmountsNeedsToBeEqualToTotalPriceMinusDiscountedAmounts() {
        OrderRequest orderRequest = OrderRequest.builder()
                .itemOptionId(itemOption.getId())
                .orderAmount(1)
                .totalPrice(10000)
                .discountedAmount(1000)
                .couponDiscountAmount(0)
                .paymentAmount(10000)
                .recipientName("name")
                .roadAddress("road123")
                .addressDetail("1-1")
                .build();
        long countBeforeOrder = orderRepository.count();

        assertThatThrownBy(() -> orderService.order(orderRequest, 1L))
                .hasMessage(ErrorEnum.INVALID_REQUEST.getMessage());
        assertThat(itemOption.getStocksCount()).isEqualTo(0);
        assertThat(orderRepository.count()).isEqualTo(countBeforeOrder);
    }

    @Test
    @DisplayName("이미 배송이 시작되면 주문 취소 불가")
    void whenDeliveryStarts_thenCannotCancelOrder() {
        long countBeforeOrder = orderRepository.count();
        order.setPaid();
        order.startDelivery();
        assertThatThrownBy(() -> orderService.cancelOrder(order.getId(), 1L))
                .hasMessage(ErrorEnum.ALREADY_ON_DELIVERY.getMessage());

        assertThat(itemOption.getStocksCount()).isEqualTo(1);
        assertThat(orderRepository.count()).isEqualTo(countBeforeOrder);
    }

    @Test
    @DisplayName("주문 취소 성공시 주문 수량만큼 재고수 증가, 환불 진행")
    void cancelOrder() {
        Long stocksCountBeforeCancel = itemOption2.getStocksCount();
        order.setPaid();
        orderService.cancelOrder(order.getId(), 1L);

        Order savedOrder = orderRepository.findById(order.getId()).get();
        assertThat(itemOptionRepository.findById(itemOption2.getId()).get().getStocksCount())
                .isEqualTo(stocksCountBeforeCancel + 1);
        assertThat(savedOrder.getOrderStatus())
                .isEqualTo(OrderStatus.CANCELLED);
        assertThat(savedOrder.getPaymentStatus()).isEqualTo(PaymentStatus.ON_REFUND);
    }

    @Test
    @DisplayName("배송이 이미 시작된 경우 배송지 변경 불가")
    void whenAlreadyOnDeliver_thenCannotChangeDeliveryInfo() {
        DeliveryInfo deliveryInfoBefore = order.getDeliveryInfo();
        order.setPaid();
        order.startDelivery();
        assertThatThrownBy(() -> orderService.changeDeliveryInfo(DeliveryInfoUpdateRequest.builder()
                .recipientName("new recipient")
                .roadAddress("new road address")
                .addressDetail("new address detail")
                .build(), order.getId(), 1L))
                .hasMessage(ErrorEnum.ALREADY_ON_DELIVERY.getMessage());
        assertThat(order.getDeliveryInfo()).isSameAs(deliveryInfoBefore);
    }

    @Test
    @DisplayName("배송지 변경")
    void changeDeliveryInfo() {
        DeliveryInfo deliveryInfoBefore = order.getDeliveryInfo();
        orderService.changeDeliveryInfo(DeliveryInfoUpdateRequest.builder()
                .recipientName("new recipient")
                .roadAddress("new road address")
                .addressDetail("new address detail")
                .build(), order.getId(), 1L);
        assertThat(order.getDeliveryInfo()).isNotSameAs(deliveryInfoBefore);
    }
}