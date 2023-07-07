package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded // 내장 타입을 포함 했다.
    private Address address;

    // 누구에 의해서 매핑이 되었는가? "Order 테이블에 있는 member 필드에 의해서 매핑되었다" 는 의미
    // "저 필드에 의해서 매핑된 거울일 뿐이다" 라는 표현. 즉, 읽기 전용.
    // 여기에 값을 넣는다고 해서 Order 가 가리키고 있는 FK 값이 변경되지 않는다.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
