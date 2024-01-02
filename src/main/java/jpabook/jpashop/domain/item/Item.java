package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속관계 전략을 정의 해줘야 함.
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {


    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items", fetch = FetchType.LAZY)
    private List<Category> categories = new ArrayList<>();

    // == 비즈니스 로직 == (ex. 재고를 늘리고 or 줄이고)
    // 도메인 주도 설계시,
    // 엔티티 자체가 해결할 수 있는 것들은
    // 엔티티 안에 비즈니스 로직을 넣는 것이 좋다.
    // (= 객체 지향적. '응집도' 증가)

    // 즉, 핵심 비즈니스 로직은 엔티티에 직접 넣는 것이 좋다!

    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
