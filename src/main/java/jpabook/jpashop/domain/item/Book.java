package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B") // DB 입장에서 저장이 될때 구분이 될 수 있는 값.
@Getter
@Setter
public class Book extends Item {

    private String author;
    private String isbn;
}
