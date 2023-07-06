package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // JUnit 한테 알려줌. "스프링 관련된 걸 테스트 할꺼야"
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
        // 같은 트랜젝션 안에서 저장하고 조회하면 영속성 컨텍스트가 똑같다.
        // 영속성 컨텍스트 안에서는 id 값이 같으면 같은 엔티티로 식별함. (식별자가 같으면 같은 엔티티로 인식한다)
        // 1차 캐시라고 불리는 곳에서 영속성 컨텍스트에서 관리되고 있는 거기서 같은게 나오는 것.

        System.out.println("findMember == member" + (findMember == member));
    }
}