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

    static boolean[] visited;
    static ArrayList[] list;

    static int count = 0;

    public static void main(String[] args) throws IOException{

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(br.readLine());
        list = new ArrayList[N+1];
        visited = new boolean[N+1];

        for(int i = 0; i < N+1; i++){
            list[i] = new ArrayList<>();
        }

        for(int i = 0; i < M; i++){
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            list[a].add(b);
            list[b].add(a);
        }
       visited[1] = true;
       dfs(1);

        System.out.println(count);
    }

    static void dfs(int node){
        for(int i =0; i < list[node].size(); i++){
            int temp =  (int)list[node].get(i);
            if(!visited[temp]){
                visited[temp] = true;
                dfs(temp);
                count++;
            }
        }
    }

}
