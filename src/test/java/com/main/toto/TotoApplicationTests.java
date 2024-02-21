package com.main.toto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
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
    static int[] indegree;
    static ArrayList<Integer>[] list;
    static ArrayList<Integer> result;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        list = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
            list[i] = new ArrayList<>();
        }
        result = new ArrayList<>();
        indegree = new int[N + 1];
        for(int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            list[a].add(b);
            indegree[b]++;
        }

        PriorityQueue<Integer> que = new PriorityQueue<>();
        for (int i = 1; i <= N; i++) {
            if (indegree[i] == 0) {
                que.add(i);
            }
        }

        while(!que.isEmpty()){
            int cur = que.poll();
            System.out.print(cur + " ");
            for(int next : list[cur]){
                indegree[next]--;
                if(indegree[next] == 0){
                    que.add(next);
                }
            }
        }
    }
}
