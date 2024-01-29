package com.main.toto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@SpringBootTest
class TotoApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * 수도 코드 쓰는 곳
     * 입력받고, 개수로 자르면서 검증
     * 줄이는 방법: 연산 값을 들고 있으면 됨.
     */


//    static int[] array;
//
//    public static void main(String[] args) throws IOException{
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        StringTokenizer st = new StringTokenizer(br.readLine());
//
//        int N = Integer.parseInt(st.nextToken());
//        array = new int[N+1];
//        array[1] = 0;
//
//        for(int i = 2; i <= N; i++){
//            array[i] = array[i-1] + 1;
//            if(i % 2 == 0) array[i] = Math.min(array[i], array[i/2] + 1);
//            if(i % 3 == 0) array[i] = Math.min(array[i], array[i/3] + 1);
//        }
//        System.out.println(array[N]);
//    }
}
