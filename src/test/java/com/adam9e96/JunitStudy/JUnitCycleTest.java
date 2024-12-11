package com.adam9e96.JunitStudy;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;

public class JUnitCycleTest {
    @BeforeAll // 전체 테스트를 시작하기 전에 1회 실행 하므로 메서드는 static 으로 선언합니다.
    static void beforeAll() {
        System.out.println("@BeforeAll");
    }

    @BeforeEach // 테스트 케이스를 시작하기 전마다 1회 실행
    public void beforeEach() {
        System.out.println("@BeforeEach");
    }

    @Test
    public void test1() {
        System.out.println("Test1!");
    }

    @Test
    public void test2() {
        System.out.println("Test2!");
    }

    @Test
    public void test3() {
        System.out.println("Test3!");
    }

    @AfterEach
    public void afterEach() {
        System.out.println("@AfterEach");
    }

    @AfterAll // 전체 테스트를 마치고 종료하기 전에 1회 실행하므로 메서드는 static 으로 선언합니다.
    static void afterAll() {
        System.out.println("@AfterAll");
    }
    @Test
    public void junitQuiz(){
        System.out.println("JUnitQuiz");
    }
    @Test
    public void junitQuiz2(){
        System.out.println("JUnitQuiz2");
    }
    @AfterEach
    public void afterEach2() {
        System.out.println("AfterEach2");
    }
}
