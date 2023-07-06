package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 양방향 연관관계
    @ManyToOne
    @JoinColumn(name = "member_id") // Mapping 을 뭘로 할 것인가? (FK 이름)
    private Member member;
}
