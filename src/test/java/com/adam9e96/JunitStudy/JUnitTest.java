package com.adam9e96.JunitStudy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JUnitTest {

    @DisplayName("1 + 2 는 3이다.") // 테스트 이름
    @Test
    public void jUnitTest() {
        int a = 1;
        int b = 2;
        int sum = 3;

        Assertions.assertEquals(sum, a + b);
    }

    @Test
    public void jUnitTest2() {
        String name1 = "홍길동";
        String name2 = "홍길동";
        String name3 = "홍길은"; // 다른 이름
//        String name4 = null;

        /*
         * 1. 모든 변수가 null 아닌지 확인
         * 2. name1 과 name2 가 같은지 확인
         * 3. name1 과 name3 가 다른지 확인
         */
        // 1.
        Assertions.assertNotNull(name1);
        Assertions.assertNotNull(name2);
        Assertions.assertNotNull(name3);
//        Assertions.assertNotNull(name4);
        assertThat(name1).isNotNull();
        assertThat(name2).isNotNull();
        assertThat(name3).isNotNull();

        // 2.
        Assertions.assertEquals(name1, name2);
        assertThat(name1).isEqualTo(name2);

        // 3
        Assertions.assertNotEquals(name1, name3);
        assertThat(name1).isNotEqualTo(name3);
    }

    @Test
    public void exampleTest() {
        String actual = "Hello, World!";
        String expected = "Hello, World!";

        assertThat(actual)
                .as("확인 : 실제 문자열이 예상 문자열과 동일한지")
                .isEqualTo(expected);

        int actualNumber = 10;
        int expectedNumber = 10;
        assertThat(actualNumber)
                .as("확인 : 실제 숫자가 예상 숫자와 동일한지")
                .isEqualTo(expectedNumber);
    }
}


