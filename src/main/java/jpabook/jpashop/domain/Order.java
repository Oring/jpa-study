package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.reducing;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 양방향 연관관계
    // 양방향이므로 값을 양쪽에 다 넣어줘야 한다.
    // 양방향이니까 양쪽에서 해당 값을 다 찾을 수 있으니까.
    // 단, DB에 저장 하는 것은 연관관계 주인만 하면 된다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // Mapping 을 뭘로 할 것인가? (FK 이름)
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    // 컬렉션을 필드에서 초기화 하는것이 안전하다 !
    // 1. null 문제에서 안전
    // 2. 하이버네이트 메커니즘에 문제가 발생할 수 있다.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간
    // ? Instant 보다 LocalDateTime 을 보편적으로 쓰는건가? 더 나은가?

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    //== 연관관계 편의 메서드 ==//
    //양방향 연관관계 편의 메서드 위치? 컨트롤 하는쪽이 들고있는게 좋다.
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //== 생성 메서드 ==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직 ==//
    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회 (OrderItem 들 합계)
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
        //return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum(); // stream, 람다를 활용한 간단한 코드
    }
}
