package com.zman.net.pull;

import com.zman.pull.stream.IDuplex;
import com.zman.pull.stream.impl.DefaultDuplex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Consumer;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class AbstractServerTest {

    private final IDuplex duplex = new DefaultDuplex();
    private final Throwable throwable = new RuntimeException();

    class ServerTest extends AbstractServer{

        public void listen(int port) {}

        public void close() { onClosedCallback.run(); }

        public void acceptConnect(){ onAcceptCallback.accept(duplex); }

        public void throwException(){ onThrowableCallback.accept(throwable);}
    }

    @Mock
    private Consumer<IDuplex> connectCallback;
    @Mock
    private Runnable disconnectCallback;
    @Mock
    private Consumer<Throwable> throwableCallback;

    @Test
    public void test(){
        ServerTest server = new ServerTest();
        server.onAccept(connectCallback)
                .onClosed(disconnectCallback)
                .onThrowable(throwableCallback)
                .listen(8080);
        server.acceptConnect();
        server.throwException();
        server.close();

        verify(connectCallback, times(1)).accept(duplex);
        verify(throwableCallback, times(1)).accept(throwable);
        verify(disconnectCallback, times(1)).run();
    }

}
