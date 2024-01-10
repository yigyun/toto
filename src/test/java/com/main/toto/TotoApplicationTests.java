package com.main.toto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Scanner;

//@SpringBootTest
class TotoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void back(){
                Scanner sc = new Scanner(System.in);
//                int N = sc.nextInt();
                int N = 15;
                int M = N/2 + 1;

                int count = 1;

                    for (int i = 1; i <= M; i++) {
                        int sum = 0;
                        for (int j = i; j <= M; j++) {
                            sum += j;
                            if(i != N) {
                                if (sum == N) {
                                    count++;
                                    break;
                                } else if (sum > N) {
                                    break;
                                }
                            }
                        }
                    }

                System.out.println(count);
    }
}
