package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository // Component Scan 의 대상이 되는 annotation 중에 하나. (자동으로 스프링 빈에 등록됨)
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId(); // Member를 반환하지 않고 id 만 반환하는 이유?
        // Command 랑 쿼리를 분리하라는 원칙
        // 이거는 저장을 하고 나면, 사이드 이펙트를 일으키는 커맨드성이기 때문에 리턴값을 만들지 않음.
        // 대신 id 정도 있으면 다음에 다시 조회할 수 있음.
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
