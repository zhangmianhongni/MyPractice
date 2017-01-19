package Test;

import Algorithm.RoundRobin;

/**
 * Created by mian on 2017/1/18.
 */
public class LoadBalanceTest {
    public static void main(String[] args) {
        for (int i = 0; i < 30; i++) {
            String server = RoundRobin.getServer();
            System.out.println(server);
        }
    }
}
