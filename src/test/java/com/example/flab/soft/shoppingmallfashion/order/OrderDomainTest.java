package com.example.flab.soft.shoppingmallfashion.order;

import static org.assertj.core.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.category.Category;
import com.example.flab.soft.shoppingmallfashion.category.LargeCategory;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.domain.ItemOption;
import com.example.flab.soft.shoppingmallfashion.item.domain.SaleState;
import com.example.flab.soft.shoppingmallfashion.item.domain.Sex;
import com.example.flab.soft.shoppingmallfashion.order.domain.DeliveryInfo;
import com.example.flab.soft.shoppingmallfashion.order.domain.DeliveryStatus;
import com.example.flab.soft.shoppingmallfashion.order.domain.Order;
import com.example.flab.soft.shoppingmallfashion.order.domain.OrderStatus;
import com.example.flab.soft.shoppingmallfashion.order.domain.PaymentStatus;
import com.example.flab.soft.shoppingmallfashion.store.repository.Store;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderDomainTest {
    private User user;
    private Store store;
    private Category category;
    private LargeCategory largeCategory;
    private Item item;
    private ItemOption itemOption;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("testUser@example.com")
                .password("Password1#")
                .realName("name")
                .cellphoneNumber("01012345678")
                .nickname("nickname")
                .build();

        store = Store.builder()
                .name("name")
                .logo("logo")
                .description("description")
                .businessRegistrationNumber("0123456789")
                .managerId(1L)
                .build();

        largeCategory = LargeCategory.builder()
                .name("large category")
                .build();

        category = Category.builder()
                .name("category")
                .largeCategory(largeCategory)
                .build();

        item = Item.builder()
                .name("test item")
                .originalPrice(1000)
                .salePrice(900)
                .sex(Sex.MEN)
                .saleState(SaleState.PREPARING)
                .store(store)
                .category(category)
                .lastlyModifiedBy(1L)
                .build();

        itemOption = ItemOption.builder()
                .name("test product")
                .size("L")
                .optionValue("red")
                .item(item)
                .saleState(SaleState.PREPARING)
                .stocksCount(10L)
                .build();
        item.addItemOption(itemOption);
    }

    @Test
    @DisplayName("결제 금액은 총 금액에서 할인가를 뺀 금액아어야 한다.")
    void validatePayAmounts() {
        assertThatThrownBy(() -> Order.builder()
                .orderer(user)
                .itemOption(itemOption)
                .orderAmount(1)
                .totalPrice(10000)
                .discountedAmount(1000)
                .paymentAmount(10000 - 3000)
                .deliveryInfo(DeliveryInfo.builder()
                        .recipientName("new recipient")
                        .roadAddress("new road address")
                        .addressDetail("new address detail")
                        .build())
                .build()).hasMessage(ErrorEnum.INVALID_REQUEST.getMessage());
    }

    @Test
    @DisplayName("결제 완료 변경")
    void setPaid() {
        //given
        Order order= Order.builder()
                .orderer(user)
                .itemOption(itemOption)
                .orderAmount(1)
                .totalPrice(10000)
                .discountedAmount(1000)
                .paymentAmount(9000)
                .deliveryInfo(DeliveryInfo.builder()
                        .recipientName("new recipient")
                        .roadAddress("new road address")
                        .addressDetail("new address detail")
                        .build())
                .build();
        //when
        order.setPaid();
        //then
        assertThat(order.getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    @DisplayName("이중결제 시도시 예외")
    void doublePaid() {
        //given
        Order order= Order.builder()
                .orderer(user)
                .itemOption(itemOption)
                .orderAmount(1)
                .totalPrice(10000)
                .discountedAmount(1000)
                .paymentAmount(9000)
                .deliveryInfo(DeliveryInfo.builder()
                        .recipientName("new recipient")
                        .roadAddress("new road address")
                        .addressDetail("new address detail")
                        .build())
                .build();
        //when
        order.setPaid();
        assertThatThrownBy(order::setPaid)
                .hasMessage(ErrorEnum.ALREADY_PAID.getMessage());
    }

    @Test
    @DisplayName("배송 시작")
    void startDelivery() {
        //given
        Order order= Order.builder()
                .orderer(user)
                .itemOption(itemOption)
                .orderAmount(1)
                .totalPrice(10000)
                .discountedAmount(1000)
                .paymentAmount(9000)
                .deliveryInfo(DeliveryInfo.builder()
                        .recipientName("new recipient")
                        .roadAddress("new road address")
                        .addressDetail("new address detail")
                        .build())
                .build();
        order.setPaid();
        //when
        order.startDelivery();
        //then
        assertThat(order.getDeliveryStatus()).isEqualTo(DeliveryStatus.ON_DELIVERY);
    }

    @Test
    @DisplayName("결제가 안된 상품은 배송 시작 불가, 예외를 던진다")
    void whenStartDeliveryNotPaid_thenThrowsException() {
        //given
        Order order= Order.builder()
                .orderer(user)
                .itemOption(itemOption)
                .orderAmount(1)
                .totalPrice(10000)
                .discountedAmount(1000)
                .paymentAmount(9000)
                .deliveryInfo(DeliveryInfo.builder()
                        .recipientName("new recipient")
                        .roadAddress("new road address")
                        .addressDetail("new address detail")
                        .build())
                .build();
        //when, then
        assertThatThrownBy(order::startDelivery).hasMessage(ErrorEnum.NEED_PAYMENT.getMessage());
    }

    @Test
    @DisplayName("이미 배송이 시작된 주문은 배송 시작 불가, 예외를 던진다")
    void whenStartDeliveryAlreadyOnDelivery_thenThrowsException() {
        //given
        Order order= Order.builder()
                .orderer(user)
                .itemOption(itemOption)
                .orderAmount(1)
                .totalPrice(10000)
                .discountedAmount(1000)
                .paymentAmount(9000)
                .deliveryInfo(DeliveryInfo.builder()
                        .recipientName("new recipient")
                        .roadAddress("new road address")
                        .addressDetail("new address detail")
                        .build())
                .build();
        order.setPaid();
        order.startDelivery();
        //when, then
        assertThatThrownBy(order::startDelivery).hasMessage(ErrorEnum.ALREADY_ON_DELIVERY.getMessage());
    }

    @Test
    @DisplayName("이미 배송이 시작된 주문은 배송지 변경이 불가능하다, 변경시 예외")
    void whenChangeDeliveryInfoOfItemOnDelivery_thenThrowsException() {
        //given
        DeliveryInfo originalDeliveryInfo = DeliveryInfo.builder()
                .recipientName("original recipient")
                .roadAddress("original road address")
                .addressDetail("original address detail")
                .build();
        Order order= Order.builder()
                .orderer(user)
                .itemOption(itemOption)
                .orderAmount(1)
                .totalPrice(10000)
                .discountedAmount(1000)
                .paymentAmount(9000)
                .deliveryInfo(originalDeliveryInfo)
                .build();
        order.setPaid();
        order.startDelivery();
        //when, then
        assertThatThrownBy(() -> order.changeDeliveryInfo(DeliveryInfo.builder()
                .recipientName("new recipient")
                .roadAddress("new road address")
                .addressDetail("new address detail")
                .build()))
                .hasMessage(ErrorEnum.ALREADY_ON_DELIVERY.getMessage());
        assertThat(order.getDeliveryInfo()).isEqualTo(originalDeliveryInfo);
    }
}
