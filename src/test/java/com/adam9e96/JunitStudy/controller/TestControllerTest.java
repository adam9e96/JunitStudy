package com.adam9e96.JunitStudy.controller;

import com.adam9e96.JunitStudy.entity.Member;
import com.adam9e96.JunitStudy.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * {@code TestControllerTest}는 {@code /test} 엔드포인트에 대한 테스트를 수행하는 테스트 클래스입니다.
 * </p>
 *
 * <p>주요 기능:</p>
 * <ul>
 *     <li>애플리케이션 컨텍스트 로드</li>
 *     <li>MockMvc를 사용한 HTTP 요청 시뮬레이션</li>
 *     <li>데이터베이스 초기화 및 정리</li>
 * </ul>
 *
 * @version 1.0.0
 * @since 2024-12-10
 */
@SpringBootTest // 테스트용 애플리케이션 컨텍스트를 생성합니다.
@AutoConfigureMockMvc // MockMvc를 자동으로 구성하고 주입합니다.
@Slf4j // Lombok의 SLF4J 로깅을 활성화합니다.
class TestControllerTest {

    /**
     * 웹 API를 테스트 할 때 사용합니다.
     * 스프링 MVC 테스트의 시작점입니다.
     * 이 클래스를 통해 HTTP, GET, POST 등에 대해 API 테스트를 할 수 있습니다.
     */
    @Autowired
    protected MockMvc mockMvc; // HTTP 요청을 시뮬레이션하기 위한 MockMvc 객체입니다.


    @Autowired
    private WebApplicationContext context; // 웹 애플리케이션 컨텍스트를 주입받습니다.

    @Autowired
    private MemberRepository memberRepository; // Member 엔티티에 대한 리포지토리를 주입받습니다.

    /**
     * 각 테스트 메서드 실행 전에 MockMvc를 설정합니다.
     * 웹 애플리케이션 컨텍스트를 기반으로 MockMvc 인스턴스를 빌드합니다.
     */
    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    /**
     * 각 테스트 메서드 실행 후에 데이터베이스를 정리합니다.
     * 테스트 데이터가 테스트 간에 영향을 미치지 않도록 모든 Member 엔티티를 삭제합니다.
     */
    @AfterEach
    public void cleanUp() {
        this.memberRepository.deleteAll();
    }

    /**
     * <p>
     * 이 테스트는 {@code /test} 엔드포인트에 대한 GET 요청이 모든 회원을 올바르게 반환하는지 검증합니다.
     * </p>
     *
     * <p>테스트 시나리오:</p>
     * <ol>
     *     <li>테스트 데이터로 회원을 저장합니다.</li>
     *     <li>{@code /test} 엔드포인트에 GET 요청을 보냅니다.</li>
     *     <li>응답 상태가 200 OK인지 확인합니다.</li>
     *     <li>응답 JSON이 저장된 회원의 ID와 이름을 포함하는지 확인합니다.</li>
     * </ol>
     *
     * @throws Exception 요청 처리 중 발생할 수 있는 예외
     */
    @DisplayName("getAllMembers: 아티클 조회에 성공한다.")
    @Test
    public void getAllMembers() throws Exception {


        // given: 테스트에 필요한 사전 조건을 설정합니다.
        final String url = "/test";

        /*
         * 테스트용 더미 데이터를 데이터베이스에 저장합니다.
         */
        Member saveMember = memberRepository.save(new Member(1L, "홍길동"));
        log.info("저장된 회원: {}", saveMember); // 저장된 회원 정보 로그 출력

        // when 실제 테스트하려는 동작을 수행합니다.
        final ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON)); // /test 엔드포인트에 GET 요청을 보냅니다.

        // 응답 내용을 문자열로 추출하여 로그에 출력
        String responseContent = result.andReturn().getResponse().getContentAsString();
        log.info("응답 내용: {}", responseContent);

        // then: 예상되는 결과를 검증합니다.
        result
                .andExpect(status().isOk()) // 응답 상태가 200 OK 인지 확인합니다.
                // 응답 JSON의 첫 번째 객체의 id가 저장된 회원의 id와 일치하는지 확인합니다.
                .andExpect(jsonPath("$[0].id").value(saveMember.getId()))
                // 응답 JSON의 첫 번째 객체의 name이 저장된 회원의 이름과 일치하는지 확인합니다.
                .andExpect(jsonPath("$[0].name").value(saveMember.getName()));


    }

}
