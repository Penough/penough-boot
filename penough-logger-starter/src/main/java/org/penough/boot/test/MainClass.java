package org.penough.boot.test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

public class MainClass {

    public static void main(String[] args) throws IOException {
        Pipe pipe = Pipe.open();
        Pipe.SinkChannel sinkChannel = pipe.sink();
        String newData = "New String to write to file..." + System.currentTimeMillis();
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(newData.getBytes());
        buf.flip();
        while(buf.hasRemaining()) {
            sinkChannel.write(buf);
        }
//        Pipe.SourceChannel sourceChannel = pipe.source();
        buf.flip();
        buf.clear();
//        int bytesRead = sourceChannel.read(buf);
        while (buf.hasRemaining()){
            System.err.print((char)buf.get());
        }
    }
}
