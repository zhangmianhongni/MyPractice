package Adapter.Target;

import Adapter.Adapter.DBSocketInterface;

/**
 * Created by Administrator on 2016/10/8.
 */
public class Hotel {
    private DBSocketInterface dbSocket;

    public Hotel(){

    }

    public Hotel(DBSocketInterface dbSocket){
        this.dbSocket = dbSocket;
    }

    public void setDbSocket(DBSocketInterface dbSocket) {
        this.dbSocket = dbSocket;
    }

    public void charge(){
        this.dbSocket.powerWithTwoRound();
    }
}
