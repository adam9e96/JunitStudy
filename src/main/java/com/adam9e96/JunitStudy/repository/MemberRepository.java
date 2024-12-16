package com.adam9e96.JunitStudy.repository;

import com.adam9e96.JunitStudy.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Member 엔티티를 위한 JPA 리포지토리 인터페이스.
 *
 * <p>
 * JpaRepository 를 상속받아 기본적인 CRUD(Create, Read, Update, Delete) 연산과
 * 페이징, 정렬 등의 기능을 자동으로 제공합니다.
 * </p>
 *
 * <p>
 * 제네릭 타입으로 {@link Member} 엔티티와 해당 엔티티의 기본 키 타입인 {@link Long} 을 지정합니다.
 * </p>
 *
 * @author adam9e96
 * @version 1.0.0
 * @see Member
 * @see JpaRepository
 * @since 2024.12.12
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * id가 아닌 name 으로 찾고 싶을때는?
     * 기본키가 아닌 속성들을 찾을 때는 값이 있거나 없을 수도 있으므로 JPA에서
     * 기본으로 name을 찾아주는 메서드를 지원하지 않습니다.
     * JPA에서 메서드 이름으로 쿼리를 작성하는 기능은 지원합니다.
     *
     * <p>
     * name 값이 'C'인 멤버를 찾아야 하는 경우
     * SELECT * FROM member WHERE name = 'C';
     * </p>
     */
    Optional<Member> findByName(String name);

    /*
     * 전체조회 -> findAll() 메서드 사용
     * 아이디로 조회 -> findById() 메서드 사용
     * 특정 컬럼으로 조회 -> 쿼리 메서드 명명 규칙에 맞게 정의후 사용
     */

}

