package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// JPA의 모든 데이터 변경이나 로직들은 가급적 Transaction 안에서 다 실행되어야 한다.
// Transaction 안에서 데이터 변경되는 것은 Transactional 어노테이션이 있어야 한다.
// 그래야 Lazy Loading 같은게 다 된다. (+ Open session in view)
@Transactional // 이미 Spring에 dependency 한 로직이 많이 들어가있기 때문에 Spring의 @Transactional을 쓰는 것이 더 낫다. 쓸 수 있는 옵션들이 많음.
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복회원 검증
     * @param member
     */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }


    }

    // 회원 전체 조회
    @Transactional(readOnly = true) // 조회만 하는 경우, 성능을 최적화 해줌. (영속성 컨텍스트를 flush 안함, 더티체킹을 안함. DB에 읽기 전용이라고 알려줘서 부하를 줄임)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
