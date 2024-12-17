package com.adam9e96.JunitStudy.repository;

import com.adam9e96.JunitStudy.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        // when
        Member member = memberRepository.findById(2L).get();
        memberRepository.delete(member);
        log.info(member.toString());
        assertThat(memberRepository.existsById(2L)).isFalse();
    }

    @Sql("/data.sql")
    @Test
    void deleteMemberById() {
        // when
        memberRepository.deleteById(2L);
        // then
        assertThat(memberRepository.findById(2L).isEmpty()).isTrue();
    }

    /**
     * <code>
     * DELETE FROM member
     * </code>
     * 이 메서드는 모든 데이터를 삭제하므로 실제 서비스 코드에서는 거의 사용되지 않습니다.
     * 테스트 간의 격리를 보장하기 위해 사용됩니다.
     * 테스트의 실행으로 데이터베이스가 변경되었을 때 다른 테스크가 그 데이터베이스를 사용할 때 영향을 주지 않도록 하기 위함입니다.
     */
    @Sql("/data.sql")
    @Test
    void deleteAllMembers() {
        // when
        memberRepository.deleteAll();

        // then
        assertThat(memberRepository.findAll().size()).isZero();
    }

    @AfterEach
    public void cleanUp() {
        memberRepository.deleteAll();
        log.info("Clean up");
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

    /**
     * 엔티티를 한꺼번에 저장
     */
    @Test
    void saveMembers() {
        // given
        List<Member> members = List.of(
                Member.builder().name("B").build(),
                Member.builder().name("C").build());

        // when
        memberRepository.saveAll(members);

        // then
        assertThat(memberRepository.findAll().size()).isEqualTo(2);
    }

    /**
     * 수정 메서드
     * <code>
     * UPDATE member SET name = 'BC' WHERE id = 2;
     * </code>
     * JPA는 트랜잭션 내에서 데이터를 수정해야 합니다.
     * 데이터를 수정할 때는 그냥 메서드만 사용하면 안되고 @Transactional 어노테이션을 메서드에 추가해야 합니다.
     * changeName(String name) 메서드를 Member 클래스에 생성하고 이용한다.
     *
     * @Transactional 어노테이션이 포함된 메서드에서 호출되면 JPA는 변경 감지(dirty checking) 기능을 통해
     * 엔티티의 필드값이 변경될 때 그 변경 사항을 데이터베이스에 자동으로 반영 합니다.
     * 만약 엔티티의 필드값이 변경될 때 그 변경 사항을 데이터베이스에 자동으로 반영합니다.
     * <p>
     * 만약 엔티티가 영속 상태일 때 필드값을 변경하고 트랜잭션이 커밋되면 JPA는 변경 사항을 데이터베이스에 자동으로 적용합니다.
     * <p>
     * data.sql 스크립트로 3명의 멤버를 추가하고 id가 2인 멤버를 찾아 이름을"BC"로 변경한 뒤에
     * 다시 조회해 이름이 "BC"로 변경되었는지 확인합니다
     * <p>
     * 여기서 @Transactional 이 없는데
     * @DataJpaTest 어노테이션안에 포함되어있기 때문이다.
     * 그래서 서비스 메서드에서는 @Transactional 을 사용해야한다
     */
    @Sql("/data.sql")
    @Test
    void updateMember() {
        // given
        Member member = memberRepository.findById(2L).get();

        // when
        member.setName("BC");

        // then
        assertThat(memberRepository.findById(2L).get().getName()).isEqualTo("BC");
        /*
         * memberRepository.findById(2L)를 통해 id=2인 Member 엔티티를 조회할 때,
         * 해당 엔티티는 영속성 컨텍스트에 관리되는 영속 상태가 됩니다.
         * member.setName("BC") 로 엔티티의 필드 값을 변경합니다.
         * 이 시점에 JPA는 엔티티 스냅샷과 비교하여 변경사항이 있음을 감지합니다. 하지만 이 시점에서는 바로 DB에 쿼리를 보내지 않습니다.
         * 트랜잭션 커밋 시점에 변경 사항을 모아서 DB에 반영합니다.
         * 테스트 메서드가 끝나며 트랜잭션이 커밋될 때, JPA는 변경된 엔티티 필드 값을 바탕으로 UPDATE 쿼리를 실행하여 DB에 반영합니다.
         */
    }


}