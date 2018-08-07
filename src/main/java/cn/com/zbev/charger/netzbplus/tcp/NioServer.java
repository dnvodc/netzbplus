package cn.com.zbev.charger.netzbplus.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NioServer {
	
	private BlockingQueue<Call> queue = new LinkedBlockingQueue<Call>();

    private Queue<Call> responseCalls = new ConcurrentLinkedQueue<Call>();

    volatile boolean running = true;

    private Responder responder = null;

    private static int NIO_BUFFER_LIMIT = 64 * 1024;

    private int handler = 10;
    
    private Queue<SocketChannel> connections = new ConcurrentLinkedQueue<SocketChannel>();
    
	
	class Listener extends Thread {

        Selector selector;
        Reader[] readers;
        int robin;
        int readNum;

        Listener(int port) throws IOException {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port), 150);
            selector = Selector.open();
            
           
            
            SelectionKey key_1= serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            
            
            
            readNum = 10;
            readers = new Reader[readNum]; //初始化10个读线程
            for(int i = 0; i < readNum; i++) {
                readers[i] = new Reader(i);
                readers[i].start();
            }
        }


        //主线程接受Connection Request
        public void run() {
            while(running) {
                try {
                    selector.select();
                    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                    while(it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        if(key.isValid()) {
                            if(key.isAcceptable()) {
                                doAccept(key);
                            }
                            if (key.isWritable()) {
                            	if (key.attachment()  != null) {
                            		System.out.println(key.attachment().toString());
                            	}
    						}
    						else
    						{
    							System.out.println("key not isWirtable");
    						}
    						
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        public void doAccept(SelectionKey selectionKey) throws IOException {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel;
            int i = 0;
            
            while((socketChannel = serverSocketChannel.accept()) != null) {
                try {
                	
                	System.out.println("accept--"+(i++));
                	
                    socketChannel.configureBlocking(false);
                    socketChannel.socket().setTcpNoDelay(true);
                    socketChannel.socket().setKeepAlive(true);
                    
                    connections.offer(socketChannel);
                    
                    
                    SelectionKey key_2 = socketChannel.register(selector, SelectionKey.OP_READ );
                    System.out.println("the key interestOps()="+key_2.interestOps());
                    
                    SelectionKey key_3 = socketChannel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ,"name--"+i);
                    System.out.println("the key interestOps()="+key_2.interestOps());                    
                    
                    if (key_2.equals(key_3)) {
                    	System.out.println("the two key is same");
                    }
                    
                    if (key_3.isWritable()) {
                    	System.out.println(key_3.attachment().toString());
                    }
                    
                    
                } catch (IOException e) {
                    socketChannel.close();
                    throw e;
                }
                
                
                
                //测试时不用进行读
                Reader reader = getReader();
                try {
                    reader.startAdd();
                    SelectionKey readKey = reader.registerChannel(socketChannel);
                    Connection c = new Connection(socketChannel);
                    readKey.attach(c);
                } finally {
                    reader.finishAdd();
                }
            }
        }

        public Reader getReader() {
            if(robin == Integer.MAX_VALUE) {
                robin = 0;
            }
            return readers[(robin ++) % readNum];
        }
    }


    class Reader extends Thread {

        Selector readSelector;
        boolean adding;

        Reader(int i) throws IOException {
            setName("Reader-" + i);
            this.readSelector = Selector.open();
            System.out.println("Starting Reader-" + i + "...");
        }

        @Override
        public void run() {
            while(running) {
                try {
                    readSelector.select();
                    while(adding) {
                        synchronized(this) {
                            this.wait(1000);
                        }
                    }

                    Iterator<SelectionKey> it = readSelector.selectedKeys().iterator();
                    while(it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        if(key.isValid()) {
                        	
                            if(key.isReadable()) {
                                doRead(key);
                            }
                        }
                    }
                } catch (IOException e) {
                	System.out.println(e.getMessage());
                } catch (InterruptedException e) {
                	System.out.println(e.getMessage());
                }
            }
        }

        public void doRead(SelectionKey selectionKey) {
            Connection c = (Connection) selectionKey.attachment();
            if(c == null) {
                return;
            }

            int n;
            try {
                n = c.readAndProcess();
            } catch (IOException e) {
            	System.out.println( e.getMessage());
                n = -1;
            } catch (Exception e) {
            	System.out.println( e.getMessage());
                n = -1;
            }
            if(n == -1) {
                c.close();
            }
        }

        public SelectionKey registerChannel(SocketChannel channel) throws IOException {
            return channel.register(readSelector, SelectionKey.OP_READ);
        }

        public void startAdd() {
            adding = true;
            readSelector.wakeup();
        }

        public synchronized void finishAdd() {
            adding = false;
            this.notify();
        }
    }
    
    class Connection {
        private SocketChannel channel;
        private ByteBuffer dataBufferLength;
        private ByteBuffer dataBuffer;
        private boolean skipHeader;

        public Connection(SocketChannel channel) {
            this.channel = channel;
            this.dataBufferLength = ByteBuffer.allocate(4);
        }

        public int readAndProcess() throws IOException {
            int count;
            if(!skipHeader) {
                count = channelRead(channel, dataBufferLength);
                if (count < 0 || dataBufferLength.remaining() > 0) {
                    return count;
                }
            }

            skipHeader = true;

            if(dataBuffer == null) {
                dataBufferLength.flip();
                int dataLength = dataBufferLength.getInt();
                dataBuffer = ByteBuffer.allocate(dataLength);
            }

            count = channelRead(channel, dataBuffer);

            if(count >= 0 && dataBuffer.remaining() == 0) {
                process();
            }

            return count;
        }


        /**
         * process the dataBuffer
         */
        public void process() {
            dataBuffer.flip();
            byte[] data = dataBuffer.array();
            Call call = new Call(this, data, responder);
            try {
                queue.put(call);
            } catch (InterruptedException e) {
            	System.out.println(e.getMessage());
            }

        }


        public void close() {
            if(channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                }
            }
        }
    }


    class Responder extends Thread {

        Selector writeSelector;

        public Responder() throws IOException {
            writeSelector = Selector.open();
        }

        public void run() {
            while(running) {
                try {
                    registWriters();
                    int n = writeSelector.select(1000);
                    if(n == 0) {
                        continue;
                    }
                    Iterator<SelectionKey> it = writeSelector.selectedKeys().iterator();
                    while(it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        if(key.isValid() && key.isWritable()) {
                            doAsyncWrite(key);
                        }
                    }
                } catch (IOException e) {
                	System.out.println( e.getMessage());
                }
            }
        }


        public void registWriters() throws IOException {
            Iterator<Call> it = responseCalls.iterator();
            while(it.hasNext()) {
                Call call = it.next();
                it.remove();
                SelectionKey key = call.conn.channel.keyFor(writeSelector);
                try {
                    if (key == null) {
                        try {
                            call.conn.channel.register(writeSelector, SelectionKey.OP_WRITE, call);
                        } catch (ClosedChannelException e) {
                            //the client went away
                        	System.out.println( e.getMessage());
                        }
                    } else {
                        key.interestOps(SelectionKey.OP_WRITE);
                    }
                } catch (CancelledKeyException e) {
                	System.out.println( e.getMessage());
                }
            }
        }


        public void registerForWrite(Call call) throws IOException {
            responseCalls.add(call);
            writeSelector.wakeup();
        }

        private void doAsyncWrite(SelectionKey key) throws IOException {
            Call call = (Call) key.attachment();
            if(call.conn.channel != key.channel()) {
                throw new IOException("bad channel");
            }
            int numBytes = channelWrite(call.conn.channel, call.response);
            if(numBytes < 0 || call.response.remaining() == 0) {
                try {
                    key.interestOps(0);
                } catch (CancelledKeyException e) {
                	System.out.println( e.getMessage());
                }
            }
        }
        
        
        

        private void doResponse(Call call) throws IOException {
            //if data not fully send, then register the channel for async writer
            if(!processResponse(call)) {
                registerForWrite(call);
            }
        }

        public boolean processResponse(Call call) throws IOException {
                        
            int numBytes = channelWrite(call.conn.channel, call.response);
            if (numBytes < 0) {
            	System.out.println("write channel error return -1");
            	call.conn.channel.close();
            	
            }
            if (call.response.hasRemaining()) {
            	System.out.println("buffer hasRemaining="+call.response.remaining());
            	//
            }
            if(!call.response.hasRemaining()) {
                call.done = true;
                return true;
            }
            else
            {
            	return false;
            }
            
        }
    }

    class Handler extends Thread {

        public Handler(int i) {
            setName("handler-" + i);
            System.out.println( "Starting Handler-" + i + "...");
        }

        public void run() {
            while(running) {
                try {
                    Call call = queue.take();
                    process(call);
                } catch (InterruptedException e) {
                	System.out.println( e.getMessage());
                } catch (IOException e) {
                	System.out.println( e.getMessage());
                }
            }
        }

        public void process(Call call) throws IOException {
            byte[] request = call.request;
            String message = new String(request);
            System.out.println( "received mseesage: " + message);

            //each channel write 2MB data for test
            int dataLength = 2 * 1024 * 1024;
            ByteBuffer buffer = ByteBuffer.allocate(4 + dataLength);

            buffer.putInt(dataLength);
            writeDataForTest(buffer);
            buffer.flip();

            call.response = buffer;
            responder.doResponse(call);
        }
    }

    public void writeDataForTest(ByteBuffer buffer) {
        int n = buffer.limit() - 4;
        for(int i = 0; i < n; i++) {
            buffer.put((byte)0);
        }
    }


    class Call {
        Connection conn;
        byte[] request;
        Responder responder;
        ByteBuffer response;
        boolean done;
        public Call(Connection conn, byte[] request, Responder responder) {
            this.conn = conn;
            this.request = request;
            this.responder = responder;
        }
    }


    public int channelRead(ReadableByteChannel channel, ByteBuffer buffer) throws IOException {
        return buffer.remaining() <= NIO_BUFFER_LIMIT ? channel.read(buffer) : channleIO(channel, null, buffer);
    }

    public int channelWrite(WritableByteChannel channel, ByteBuffer buffer) throws IOException {
        return buffer.remaining() <= NIO_BUFFER_LIMIT ? channel.write(buffer) : channleIO(null, channel, buffer);
    }


    public int channleIO(ReadableByteChannel readCh, WritableByteChannel writeCh, ByteBuffer buffer) throws IOException {
        int initRemaining = buffer.remaining();
        int originalLimit = buffer.limit();

        int ret = 0;
        try {
            while (buffer.remaining() > 0) {
                int ioSize = Math.min(buffer.remaining(), NIO_BUFFER_LIMIT);
                buffer.limit(buffer.position() + ioSize);
                ret = readCh == null ? writeCh.write(buffer) : readCh.read(buffer);
                if (ret < ioSize) {
                    break;
                }
            }
        } finally {
            buffer.limit(originalLimit);
        }

        int byteRead = initRemaining - buffer.remaining();
        return byteRead > 0 ? byteRead : ret;
    }


    public void startHandler() {
        for(int i = 0; i < handler; i++) {
            new Handler(i).start();
        }
    }

    private class WriteTestThread extends Thread
    {
    	public void run() {
    		while(running) {
    			
    			if (!connections.isEmpty()) {

    				synchronized(connections) {    					
    					
						SocketChannel s = connections.poll();
						
						ByteBuffer buffer = ByteBuffer.allocate(10);
	    				buffer.putInt(Integer.MAX_VALUE);
	    				try {
							int ret = channelWrite(s,buffer);
							System.out.println("write result="+ ret);
							
						} catch (IOException e) {
							System.out.println("write error="+ e.getMessage());
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    					    					
    				}   				
    				
    			}
    			
    		}
    		
    	}
    	
    	
    }

    public void start() throws IOException {
        new Listener(5500).start();
        //responder = new Responder();
        //responder.start();
        //startHandler();
        
        WriteTestThread t = new WriteTestThread();
        t.start();
        
        System.out.println("server startup! ");
    }

    public static void main(String[] args)  {
        NioServer server = new NioServer();
        try {
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
