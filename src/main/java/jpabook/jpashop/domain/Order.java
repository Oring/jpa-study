package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
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

    // 컬렉션을 필드에서 초기화 하는것이 안전하다 !
    // 1. null 문제에서 안전
    // 2. 하이버네이트 메커니즘에 문제가 발생할 수 있다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
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

}
