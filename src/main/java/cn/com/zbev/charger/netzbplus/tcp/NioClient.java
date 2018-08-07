package cn.com.zbev.charger.netzbplus.tcp;


import javax.net.SocketFactory;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {
	
	
    Socket socket;
    OutputStream out;
    InputStream in;

    public NioClient() throws IOException {
        socket = SocketFactory.getDefault().createSocket();
        socket.setTcpNoDelay(true);
        socket.setKeepAlive(true);
        InetSocketAddress server = new InetSocketAddress("localhost", 5500);
        socket.connect(server, 10000);
        out = socket.getOutputStream();
        in = socket.getInputStream();
    }


    public void send(String message) throws IOException {
        byte[] data = message.getBytes();
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(data.length);
        dos.write(data);
        out.flush();
    }


    public static void main(String[] args) throws IOException {

    	int n = 3;
    	ByteBuffer buffer  = ByteBuffer.allocate(7);
    	
    	System.out.println("position="+buffer.position()+",remaining="+buffer.remaining()+",limit="+buffer.limit());  
    	buffer.putChar('A');
    	
    	System.out.println("position="+buffer.position()+",remaining="+buffer.remaining()+",limit="+buffer.limit());  
    	buffer.putInt(n);
    	System.out.println("position="+buffer.position()+",remaining="+buffer.remaining()+",limit="+buffer.limit());
    	
    	
        
        /*for(int i = 0; i < n; i++) {
            new Thread() {
            	NioClient client = new NioClient();
            	
                public void run() {
                	
                	BufferedReader reader =new BufferedReader(new InputStreamReader(System.in)); 
                     
                    String str ="";
					try {
						str = reader.readLine();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                      
                    System.out.println(str);  
                      
                    //ByteArrayInputStream bai = new ByteArrayInputStream()
                    
                    //while(str != "exit") {
                    	try {
                            client.send(getName() + "_xiaomiemie");

                            DataInputStream inputStream = new DataInputStream(client.in);
                            int dataLength = inputStream.readInt();
                            byte[] data = new byte[dataLength];
                            inputStream.readFully(data);
                            
                            System.out.println("receive from server: dataLength=" + data.length );
                        } catch (IOException e) {
                        	System.out.println(e.getMessage());
                        } catch (Exception e) {
                        	System.out.println(e.getMessage());
                        }
                    //}
                    
                    try {
						client.socket.close();
						
						System.out.println("socket close and app exit");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}                	
                    
                }
            }.start();
        }*/
    }

	
}
