package com.adam9e96.JunitStudy.repository;

import com.adam9e96.JunitStudy.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 스프링 데이터 JPA 에서 제공하는 메서드 사용하기
 */
@Slf4j
@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    /**
     * @Sql 어노테이션 :
     * 테스트를 실행하기 전에 SQL 스크립트를 실행 시킬 수 있습니다.
     * 이 메서드는 data.sql로 insert 실행하고 조회가 되는지 테스트해보는 메서드입니다.
     */
    @Sql("/data.sql")
    @Test
    void getAllMembers() {
        // when
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(members.size()).isEqualTo(3);
    }

    // SELECT * FROM member WHERE id =2;
    @Sql("/data.sql")
    @Test
    void getMemberById() {
        // When
        Member member = memberRepository.findById(2L).get();
    }

    @Sql("/data.sql")
    @Test
    void getMemberByName() {
        // when
        Member member = memberRepository.findByName("C").get();

        // then
        assertThat(member.getName()).isEqualTo("C");
        assertThat(member.getId()).isEqualTo(3);
    }


    @Test
    void saveMember() {
        // given
        // ID를 명시하지 않고 생성
        Member member = Member.builder().name("A").build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        // DB에서 생성된 ID가 엔티티에 매핑되어 있어야 함
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getName()).isEqualTo("A");
    }


    @Test
    void saveMember2() {
        Member member = new Member(null, "A");
        memberRepository.save(member);
        log.info(member.toString());
    }

    /**
     * saveMember() 개선
     * <ul>
     *     <li>
     *         엔티티를 save() 한 후 flush() 를 통해 DB에 즉시 반영을 강제
     *     </li>
     *     <li>
     *         inNotNull(), orElseThrow() 를 활용하여 더 명확하게 예외 상황에 대비
     *     </li>
     * </ul>
     */
    @Test
    void saveMember3() {
        // given
        // ID를 지정하지 않고, DB가 자동 생성하게 둡니다.
        Member member = Member.builder().name("A").build();

        // when
        Member savedMember = memberRepository.save(member);
        memberRepository.flush(); // Optional: 즉시 반영을 확인하고 싶다면 flush 호출

        // then
        // 저장 후 엔티티에 DB에서 생성한 ID가 매핑되었는지 확인
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getName()).isEqualTo("A");

        // 다시 한번 DB에서 해당 엔티티를 가져와서 데이터 정합성 확인
        Member foundMember = memberRepository.findById(savedMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        assertThat(foundMember.getName()).isEqualTo("A");
    }


    @Sql("/data.sql")
    @Test
    void deleteMember() {
        Member member = memberRepository.findById(2L).get();
        memberRepository.delete(member);
        log.info(member.toString());
        assertThat(memberRepository.existsById(2L)).isFalse();
    }

    /**
     * 삭제 테스트 개선
     * orElseThrow() 사용: get() 대신 orElseThrow()를 사용하면, 만약 해당 ID를 가진 Member가 없을 경우 즉시 테스트가 실패하게 됩니다.
     * flush() 호출: JPA의 경우 실제 DB 반영(DELETE 쿼리 실행)이 지연될 수 있으므로, memberRepository.flush()를 호출하여 DB 반영을 강제할 수 있습니다.
     * 이는 삭제가 DB에 실제 반영된 상태를 확인하기 위함입니다.
     * 삭제 결과 직접 확인: 삭제 후 다시 findById()로 조회했을 때 비어있는지 검사하거나, existsById()로 존재 여부를 확인하는 방식 모두 가능합니다.
     */
    @Sql("/data.sql")
    @Test
    void deleteMember2() {
        // given
        Member member = memberRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // when
        memberRepository.delete(member);
        // flush를 통해 즉시 DB 반영을 확인
        memberRepository.flush();

        // then
        assertThat(memberRepository.existsById(2L)).isFalse();
        // 또는 다음과 같이 직접 조회하여 비어있는지 확인도 가능
        // assertThat(memberRepository.findById(2L)).isEmpty();
    }


}