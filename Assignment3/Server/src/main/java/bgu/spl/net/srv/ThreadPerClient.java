package bgu.spl.net.srv;
import bgu.spl.net.*;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;

import java.util.function.Supplier;

public class ThreadPerClient<T> extends BaseServer<T> {

    public ThreadPerClient(int port, Supplier<MessagingProtocol<T>> protocolFactory, Supplier<MessageEncoderDecoder<T>> encdecFactory) {
        super(port, protocolFactory, encdecFactory);
    }

    @Override
    protected void execute(BlockingConnectionHandler<T> handler) {
        Thread a = new Thread(handler);
        a.start();
    }
}
