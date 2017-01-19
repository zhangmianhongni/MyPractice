package Proxy;

/**
 * Created by Administrator on 2016/10/8.
 */
public class Test {
    public static void main(String[] args) {
        ProxyInterface normalHome = new NormalHome();
        ProxyInterface weddingCompany = new WeddingCompany(normalHome);
        weddingCompany.marry();
    }
}
