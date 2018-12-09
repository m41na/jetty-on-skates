package com.practicaldime.jesty.nio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.junit.Test;

public class TestNIOChannels {

    @Test
    public void testReadFromChannel() throws IOException {
        
        ByteArrayInputStream input = new ByteArrayInputStream("A very, very extra long string in this case".getBytes());
        System.out.println("input length = " + input.available());

        ByteArrayOutputStream queue = new ByteArrayOutputStream();
        try (ReadableByteChannel inChannel = Channels.newChannel(input)) {

            ByteBuffer buffer = ByteBuffer.allocate(8);
            int bytesRead = inChannel.read(buffer); //read into buffer.
            while (bytesRead != -1) {
                System.out.println("input remaining = " + input.available());
                System.out.println("bytes read = " + bytesRead);
                buffer.flip();  //make buffer ready for read

                if (buffer.hasRemaining()) {
                    byte[] xfer = new byte[buffer.limit()]; //transfer buffer bytes to a different aray
                    buffer.get(xfer);
                    queue.write(xfer); // read entire array backing buffer
                }

                buffer.clear(); //make buffer ready for writing
                bytesRead = inChannel.read(buffer);
            }
        }
        System.out.println("output length = " + queue.size());
        
        //now write to output
        OutputStream out = System.out;
        queue.writeTo(out);
    }
}
