package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //@PersistenceContext // JPA에서 제공하는 표준 어노테이션 (@Autowired 로도 되는 이유는 SpringBoot가 지원을 해줘서 그렇다)
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class) // JPQL을 작성해야함
                .getResultList();

        // SQL 과 JPQL 의 차이?
        // SQL : 테이블을 대상으로 쿼리
        // JPQL : 엔티티를 대상으로 쿼리
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name) // name 파라미터 바인딩
                .getResultList();
    }
}
