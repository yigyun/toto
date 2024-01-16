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

    static boolean[] visited ;
    static List<Integer>[] list;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new java.io.InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        visited = new boolean[N+1];;
        int count = 0;

        list = new ArrayList[N+1];

        for(int i = 1; i <= N; i++){
            list[i] = new ArrayList<>();
        }

        for(int i = 0; i < M; i++){
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            list[u].add(v);
            list[v].add(u);
        }

        for(int i = 1; i < N+1; i++){
            if(!visited[i]){
                count++;
                dfs(i);
            }
        }
        System.out.println(count);
    }

    static void dfs(int u){
            visited[u] = true;
            for(int num : list[u]){
                if(!visited[num]) dfs(num);
            }
    }
}
