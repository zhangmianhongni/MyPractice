package Adapter.Adapter;

/**
 * Created by Administrator on 2016/10/8.
 */
public class SocketAdapter implements DBSocketInterface {
    private GBSocketInterface gbSocket;

    public SocketAdapter(GBSocketInterface gbSocket) {
        this.gbSocket = gbSocket;
    }

    @Override
    public void powerWithTwoRound() {
        this.gbSocket.powerWithThreeFlat();
    }
}
