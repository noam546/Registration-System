package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.*;
import bgu.spl.net.srv.Reactor;



public class ReactorMain {
    public static void main(String [] args){
        Database instance=Database.getInstance();
        Reactor<Message> server = new Reactor(Integer.parseInt(args[1]),Integer.parseInt(args[0]),()->new BGRSProtocol(),()->new BGRSEncoderDecoder());
        server.serve();
    }
}