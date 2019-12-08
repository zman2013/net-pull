package com.zman.net.pull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Consumer;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AbstractClientTest {

    private final Object duplex = new Object();
    private final RuntimeException exception = new RuntimeException();

    class ClientTest extends AbstractClient{
        public void connect(String ip, int port) {
            onConnectedCallback.accept(duplex);
        }
        public void disconnect() {
            onDisconnectedCallback.run();
        }
        public void throwException(){
            onThrowableCallback.accept(exception);
        }
    }

    @Mock
    private Consumer<Object> connectCallback;
    @Mock
    private Runnable disconnectCallback;
    @Mock
    private Consumer<Throwable> throwableCallback;

    @Test
    public void test(){

        ClientTest clientTest = new ClientTest();
        clientTest
                .onConnected(connectCallback)
                .onDisconnected(disconnectCallback)
                .onThrowable(throwableCallback)
                .connect("ip", 0);
        clientTest.disconnect();
        clientTest.throwException();

        verify(connectCallback, times(1)).accept(duplex);
        verify(disconnectCallback, times(1)).run();
        verify(throwableCallback, times(1)).accept(exception);

    }

}
