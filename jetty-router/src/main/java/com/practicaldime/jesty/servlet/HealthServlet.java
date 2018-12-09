package com.practicaldime.jesty.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.practicaldime.zesty.servlet.HandlerServlet;

public class HealthServlet extends HandlerServlet {
	
	private static final long serialVersionUID = 1L;
	private final Logger LOG = LoggerFactory.getLogger(HealthServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AsyncContext async = req.startAsync();
        String page = req.getParameter("page");

        if (page != null) {
            resp.setContentType("text/html");
            resp.setStatus(HttpServletResponse.SC_OK);

            //prepare response
            ServletOutputStream out = resp.getOutputStream();
            out.setWriteListener(new WriteListener() {

                @Override
                public void onWritePossible() throws IOException {
                    File file = new File(page);
                    try (FileInputStream fis = new FileInputStream(file)) {
                        byte[] bytes = new byte[4096];
                        while ((fis.read(bytes)) != -1) {
                            out.write(bytes);
                        }
                    }
                    async.complete();
                }

                @Override
                public void onError(Throwable t) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
        } else {
            //prepare response
            String content = req.getRequestURI() + " @ " + new SimpleDateFormat("MMM dd, yyy 'at' hh:mm:ss").format(new Date());
            ByteBuffer buffer = ByteBuffer.wrap(content.getBytes());

            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);

            //write response
            ServletOutputStream out = resp.getOutputStream();
            out.setWriteListener(new WriteListener() {

                @Override
                public void onWritePossible() throws IOException {
                    while (out.isReady()) {
                        if (!buffer.hasRemaining()) {
                            async.complete();
                            return;
                        }
                        out.write(buffer.get());
                    }
                }

                @Override
                public void onError(Throwable t) {
                    getServletContext().log("Async Write Error", t);
                    async.complete();
                }
            });
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AsyncContext async = req.startAsync();

        //setup async listener
        async.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                event.getSuppliedResponse().getOutputStream().print("Complete");
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                LOG.error("Health servlet async listener onTimeout() - {}", event.getThrowable().getMessage());
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                LOG.error("Health servlet async listener onError() - {}", event.getThrowable().getMessage());
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                LOG.error("Health servlet async listener onStartAsync() - {}", req.getRequestURI());
            }
        });

        //content queue
        Queue<byte[]> queue = new LinkedBlockingQueue<>();

        //read input
        ServletInputStream input = req.getInputStream();
        ReadListener readListener = new ReadListener() {

            @Override
            public void onDataAvailable() throws IOException {
                try (ReadableByteChannel inChannel = Channels.newChannel(input)) {
                    
                    ByteBuffer buffer = ByteBuffer.allocate(4096);                    
                    int bytesRead = inChannel.read(buffer); //read into buffer.
                    while (bytesRead != -1) {
                        buffer.flip();  //make buffer ready for read

                        if (buffer.hasRemaining()) {
                            byte[] xfer = new byte[buffer.limit()]; //transfer buffer bytes to a different aray
                            buffer.get(xfer);
                            queue.add(xfer); // read entire array backing buffer
                        }

                        buffer.clear(); //make buffer ready for writing
                        bytesRead = inChannel.read(buffer);
                    }
                    LOG.debug("Completed reading content from input channel. Ques size = {} * {}", queue.size(), 4096);
                }
            }

            @Override
            public void onAllDataRead() throws IOException {
                LOG.info("add posted data has been read");
            }

            @Override
            public void onError(Throwable t) {
                async.complete();
                t.printStackTrace(System.err);
            }
        };
        input.setReadListener(readListener);

        //prepare response
        resp.setContentType("application/octet-stream");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
         
        // forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", "health-check.txt");
        resp.setHeader(headerKey, headerValue);

        //write response
        ServletOutputStream out = resp.getOutputStream();
        WriteListener writeListener = new WriteListener() {

            @Override
            public void onWritePossible() throws IOException {
                try (WritableByteChannel wchannel = Channels.newChannel(out)) {
                    while (queue.peek() != null && out.isReady()) {
                        byte[] data = queue.poll();
                        wchannel.write(ByteBuffer.wrap(data));
                    }
                    if (queue.peek() == null) {
                        async.complete();
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                getServletContext().log("Async Write Error", t);
                async.complete();
            }
        };
        out.setWriteListener(writeListener);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp); //To change body of generated methods, choose Tools | Templates.
    }
}
