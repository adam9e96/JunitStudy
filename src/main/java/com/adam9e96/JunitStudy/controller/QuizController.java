package com.adam9e96.JunitStudy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h1>
 * 테스트 코드 연습용 컨트롤러
 * </h1>
 * <p>
 * {@code QuizController}는 "/quiz" 엔드포인트에 대한 HTTP 요청을 처리하는 REST 컨트롤러입니다.
 * 두 가지 주요 작업을 제공합니다:
 * </p>
 * <ul>
 *     <li>"code" 쿼리 매개변수를 포함한 GET 요청 처리.</li>
 *     <li>"value"를 포함하는 JSON 본문을 처리하는 POST 요청.</li>
 * </ul>
 * <p>
 * 이 컨트롤러는 입력 값에 따라 다양한 응답 시나리오를 보여주기 위해 설계되었으며,
 * 테스트 및 학습 목적으로 유용할 수 있습니다.
 * </p>
 *
 * <p><strong>사용 예시:</strong></p>
 * <pre>{@code
 * // GET 요청 예시
 * GET /quiz?code=1
 * 응답: 201 Created, 본문 "Created!"
 *
 * // POST 요청 예시
 * POST /quiz
 * 본문: { "value": 1 }
 * 응답: 403 Forbidden, 본문 "Forbidden!"
 * }</pre>
 *
 * @version 1.0
 * @since 2024-04-27
 */
@RestController
public class QuizController {

    /**
     * "/quiz" 엔드포인트에 대한 HTTP GET 요청을 처리합니다.
     *
     * <p>
     * 이 메서드는 "code"라는 쿼리 매개변수를 포함한 요청을 처리합니다. "code"의 값에 따라
     * 다른 HTTP 응답을 반환합니다:
     * <br>
     *
     * > 요청 파라미터의 키가 "code" 이면 int 자료 형의 code 변수와 매핑이 됩니다.
     * </p>
     * <ul>
     *     <li>{@code code}가 {@code 1}인 경우, HTTP 201 (Created)과 본문 "Created!"를 응답합니다.</li>
     *     <li>{@code code}가 {@code 2}인 경우, HTTP 400 (Bad Request)과 본문 "Bad Request!"를 응답합니다.</li>
     *     <li>그 외의 {@code code} 값에 대해서는 HTTP 200 (OK)과 본문 "OK!"를 응답합니다.</li>
     * </ul>
     *
     * <p><strong>요청 예시:</strong></p>
     * <ul>
     *     <li>{@code GET /quiz?code=1} &rarr; {@code 201 Created}와 본문 "Created!"</li>
     *     <li>{@code GET /quiz?code=2} &rarr; {@code 400 Bad Request}와 본문 "Bad Request!"</li>
     *     <li>{@code GET /quiz?code=3} &rarr; {@code 200 OK}와 본문 "OK!"</li>
     * </ul>
     *
     * @param code 응답 유형을 결정하는 정수 코드
     * @return 적절한 HTTP 상태 및 메시지를 포함하는 {@code ResponseEntity<String>}
     */
    @GetMapping("/quiz")
    public ResponseEntity<String> quiz(@RequestParam("code") int code) {
        return switch (code) {
            case 1 -> ResponseEntity.created(null).body("Created!");
            case 2 -> ResponseEntity.badRequest().body("Bad Request!");
            default -> ResponseEntity.ok().body("OK!");
        };
    }


    /**
     * "/quiz" 엔드포인트에 대한 HTTP POST 요청을 처리합니다.
     *
     * <p>
     * 이 메서드는 {@code Code} 객체를 나타내는 JSON 본문을 포함한 요청을 처리합니다.
     * {@code Code} 객체의 {@code value} 필드에 따라 다른 HTTP 응답을 반환합니다:
     * <br>
     * 이 메서드는 요청 값을 Code 라는 객체로 매핑한 후에 value 값에 따른 다른 응답을 보냅니다.
     * </p>
     * <ul>
     *     <li>{@code code.value()}가 {@code 1}인 경우, HTTP 403 (Forbidden)과 본문 "Forbidden!"을 응답합니다.</li>
     *     <li>{@code code.value()}가 다른 값인 경우, HTTP 200 (OK)과 본문 "OK!"을 응답합니다.</li>
     * </ul>
     *
     * <p><strong>요청 예시:</strong></p>
     * <ul>
     *     <li>{@code POST /quiz}와 본문 {@code {"value":1}} &rarr; {@code 403 Forbidden}과 본문 "Forbidden!"</li>
     *     <li>{@code POST /quiz}와 본문 {@code {"value":2}} &rarr; {@code 200 OK}과 본문 "OK!"</li>
     * </ul>
     *
     * @param code 요청 본문에서 파싱된 {@code Code} 객체
     * @return 적절한 HTTP 상태 및 메시지를 포함하는 {@code ResponseEntity<String>}
     */
    @PostMapping("/quiz")
    public ResponseEntity<String> quiz2(@RequestBody Code code) {
        switch (code.value()) {
            case 1:
                return ResponseEntity.status(403).body("Forbidden!");
            default:
                return ResponseEntity.ok().body("OK!");
        }
    }
}
/**
 * <p>
 * {@code Code}는 정수 값을 캡슐화하는 단순한 레코드입니다. <br>
 * POST 요청의 JSON 본문을 {@code /quiz} 엔드포인트에 매핑하는 데 사용됩니다.
 * </p>
 *
 * <p><strong>JSON 예시:</strong></p>
 * <pre>{@code
 * {
 *     "value": 1
 * }
 * }</pre>
 *
 * @param value 이 레코드가 캡슐화하는 정수 값
 */
record Code(int value) {
}
