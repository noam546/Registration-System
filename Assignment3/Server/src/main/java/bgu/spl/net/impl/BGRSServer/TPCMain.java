package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.BGRSEncoderDecoder;
import bgu.spl.net.BGRSProtocol;
import bgu.spl.net.Database;
import bgu.spl.net.Message;
import bgu.spl.net.srv.ThreadPerClient;

public class TPCMain {
    public static void main(String [] args) {
        Database instance=Database.getInstance();
        ThreadPerClient<Message> server = new ThreadPerClient(Integer.parseInt(args[0]),()->new BGRSProtocol(),()->new BGRSEncoderDecoder());
        server.serve();
    }
}
