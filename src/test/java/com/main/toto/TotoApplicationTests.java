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

    public static void main(String[] args) throws IOException{

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();
        int V = Integer.parseInt(st.nextToken());
        int E = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        int S = Integer.parseInt(st.nextToken());

        int[] result = new int[V+1];
        ArrayList<int[]>[] list = new ArrayList[V+1];

        // 초기화
        for(int i = 0; i <= V; i++) list[i] = new ArrayList<>();
        for(int i = 0; i <= V; i++) result[i] = Integer.MAX_VALUE;
        result[S] = 0;

        // 인접 리스트
        for(int i = 0; i < E; i++){
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            list[u].add(new int[]{v, w});
        }

        // 다익스트라

        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> o1[1] - o2[1]);
        pq.offer(new int[]{S, 0});

        while(!pq.isEmpty()){
            int[] current = pq.poll();
            int node = current[0];
            int weight = current[1];

            if(result[node] < weight) continue;

            for(int[] edge : list[node]){
                int next = edge[0];
                int nextWeight = edge[1] + weight;

                if(nextWeight < result[next]){
                    result[next] = nextWeight;
                    pq.offer(new int[]{next, nextWeight});
                }
            }
        }

        for(int i = 1; i <= V; i++){
            if(i  == S) sb.append(0).append("\n");
            else if(result[i] == Integer.MAX_VALUE) sb.append("INF").append("\n");
            else sb.append(result[i]).append("\n");
        }

        System.out.println(sb.toString());
    }

}
