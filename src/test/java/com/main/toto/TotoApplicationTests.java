package com.main.toto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

//@SpringBootTest
class TotoApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * 수도 코드 쓰는 곳
     * 입력받고, 개수로 자르면서 검증
     * 줄이는 방법: 연산 값을 들고 있으면 됨.
     */


    static int[] array;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new java.io.InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        array = new int[N+1];
        for(int i = 0; i <= N; i++){
            array[i] = i;
        }

        for(int i = 0; i < M; i++){
            st = new StringTokenizer(br.readLine());
            int c = Integer.parseInt(st.nextToken());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            if(c == 0){
                union(a, b);
            } else{
                int num1 = find(a);
                int num2 = find(b);
                if(num1==num2) System.out.println("YES");
                else System.out.println("NO");
            }
        }
    }

    static int find(int a){
        if(a == array[a])
            return a;
        else
            return array[a] = find(array[a]);
    }

    static void union(int a, int b){
        int k = find(a);
        int l = find(b);
        if(k != l) {
            if(k <= l) array[l] = k;
            else array[k] = l;
        }
    }

}
