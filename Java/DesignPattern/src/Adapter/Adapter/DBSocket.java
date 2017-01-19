package Adapter.Adapter;

/**
 * Created by Administrator on 2016/10/8.
 */
public class DBSocket implements DBSocketInterface {
    @Override
    public void powerWithTwoRound() {
        System.out.println("使用两项圆头的插孔供电");
    }
}
