package Adapter;

import Adapter.Adapter.*;
import Adapter.Target.Hotel;

/**
 * Created by Administrator on 2016/10/8.
 */
public class Test {
    public static void main(String[] args) {
        DBSocketInterface dbSocket = new DBSocket();
        Hotel hotel = new Hotel();
        hotel.setDbSocket(dbSocket);
        hotel.charge();

        GBSocketInterface gbSocket = new GBSocket();
        DBSocketInterface socketAdapter = new SocketAdapter(gbSocket);
        hotel.setDbSocket(socketAdapter);
        hotel.charge();
    }
}
