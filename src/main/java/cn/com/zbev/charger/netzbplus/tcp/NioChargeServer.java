package cn.com.zbev.charger.netzbplus.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.EventListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import cn.com.zbev.charger.netzbplus.ConnectionStateEvent;
import cn.com.zbev.charger.netzbplus.EventListeners;
import cn.com.zbev.charger.netzbplus.ProtocolParser.IENProtocolParser;
import cn.com.zbev.charger.netzbplus.cmi.AbstractENData;
import cn.com.zbev.charger.netzbplus.cmi.CMI;
import cn.com.zbev.charger.netzbplus.env1.ChargeBookCancelV1Request;
import cn.com.zbev.charger.netzbplus.env1.ChargeBookV1Request;
import cn.com.zbev.charger.netzbplus.env1.DataUpdateV1Request;
import cn.com.zbev.charger.netzbplus.env1.HeartBeatV1Request;
import cn.com.zbev.charger.netzbplus.env1.ResetV1Request;
import cn.com.zbev.charger.netzbplus.env1.StartChargeV1Request;
import cn.com.zbev.charger.netzbplus.env1.StopChargeV1Request;


public class NioChargeServer  implements ServerInterface {
	
	protected static final Logger LOG = LogManager.getLogger(NioChargeServer.class);
	private static final int NIO_BUFFER_LIMIT = 16 * 1024;
	private int maxThreadNum = 15;
	
	protected int port;
	
	//保存每个Channel对于一个Selector 只有一个KEY
	//定义两个Selector,一个读，一个写
	protected Selector selector;
	protected Selector writeSelector;
	private ThreadPool pool;
	private WriterThread writer;
	
	private volatile  boolean isRunning;
	
	private Map<String,ClientConnection> clients;
	
	//private BlockingQueue<Call> sendQueue ;
	private ConcurrentLinkedQueue<WriteSession> sendQueue  = new ConcurrentLinkedQueue<WriteSession>();
	//private Queue<Call> responseCalls = new ConcurrentLinkedQueue<Call>();
	private ConcurrentLinkedQueue<AbstractENData> receiveQueue = new ConcurrentLinkedQueue<AbstractENData>();
	//private Queue<AbstractENData> sendQueue;
	
	/** container for event listeners */
	protected final EventListeners listeners = new EventListeners();
	
	
	public NioChargeServer(int port) {
		this.port = port;
		
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	
	public void stop() {
		
		if (pool != null) {
			Iterator<ReaderThread> it = pool.idle.iterator();
			
			while(it.hasNext()){
				ReaderThread t = it.next();
				t.interrupt();
			}			
		}
		if (selector !=null) {
			try {
				selector.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		isRunning = false;
	}	
	
	
	public void start()  {
		if (port <= 0) {
			isRunning = false;
			throw new java.lang.IllegalArgumentException("port is zero");
		}
		
		try {
			ServerSocketChannel serverChannel;
			serverChannel = ServerSocketChannel.open();
			
			ServerSocket serverSocket  = serverChannel.socket();
			
			selector = Selector.open();			
		
			serverSocket.bind(new InetSocketAddress(port));
			serverChannel.configureBlocking(false);			
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			isRunning = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (selector !=null) {
				//因为有可能是bind错误
				if (selector.isOpen()) {
					try {
						selector.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			e.printStackTrace();
			isRunning = false;
			LOG.error(e.getMessage());
		}
		
		//读线程池
		pool  = new ThreadPool(maxThreadNum);
		
		clients = new ConcurrentHashMap<String,ClientConnection>();
		
		//启动主监听线程(accept 就绪信号)
		Listener l = new Listener();
		l.start();
		
		//写线程 应答器
		
		try {
			
			writeSelector = Selector.open();
			
			writer = new WriterThread();
			writer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//暂时未用
	private class SendListHandler extends Thread{
		
		public void run() {
			
			while(isRunning ) {
				try {
					sendQueue.wait();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				while(!sendQueue.isEmpty()) {
					
	            	//WriteSession session = it.next();
	            	WriteSession session = sendQueue.poll();
	            	if (session == null) {
	            		continue;
	            	}
	                
	                SocketChannel channel =  session.connection.getChannel();
	                //通道是否在选择器上注册
	                SelectionKey key = channel.keyFor(writeSelector);
	                try {
	                	//没有注册
	                    if (key == null) {
	                        try {
	                            channel.register(writeSelector, SelectionKey.OP_WRITE, session);
	                        } catch (ClosedChannelException e) {
	                            //the client went away
	                            if (LOG.isTraceEnabled())
	                                LOG.trace("the client went away", e);
	                        }
	                    } else {
	                    	//此Selector 只关心 OP_WRITE
	                        //key.interestOps(SelectionKey.OP_WRITE);
	                        //是直接写效率更高吗？
	                        if((key.interestOps() & SelectionKey.OP_WRITE) == 0) {
	                        	key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
	                        }
	                    }
	                } catch (CancelledKeyException e) {
	                    if (LOG.isTraceEnabled())
	                        LOG.trace("the client went away", e);
	                }
				}			
				//唤醒write 线程
				writeSelector.wakeup();				
			}
		}
	}
	
	/**
	 * 
	 * @author wanwy
	 * 使用一个独立的线程执行Select,然后再分发给工作线程
	 * 保存每个Channel对于一个Selector 只有一个KEY 
	 */
	private class Listener extends Thread{
		
		public Listener() {
			super("EN Protocol Channel Selector monitor");
			setDaemon(true);
		}
		
		public void run() {
			while(isRunning) {
				
				try {
					int n = selector.select();
					if (n ==0) {
						continue;
					}
					
					java.util.Iterator<SelectionKey> it = selector.selectedKeys().iterator();
					
					while(it.hasNext()) {
						SelectionKey key =(SelectionKey)it.next();
						
						if (!key.isValid()) {
		                    continue;
		                }
						
						if (key.isAcceptable()) {
							ServerSocketChannel server =(ServerSocketChannel)key.channel();
							SocketChannel channel;

							//对于ServerSocketChannel 只有一个，但是同一时间可能会有多处客户端请求连接
							//既然选到了ServerSocketChannel，通道就一次性把所有的连接请求处理完
							while((channel = server.accept()) != null) {
				                try {
				                	
				                	//System.out.println("accept--"+(i++));
				                	
				                	channel.configureBlocking(false);
				                	channel.socket().setTcpNoDelay(true);
				                	channel.socket().setKeepAlive(true);
				                	
				                	ClientConnection conn = new ClientConnection(channel);
				                	//这个时候还是个匿名channel,不添加到 clients集合中，等客户端注册，验证后才知道这个channel 是那个网关的
				                	key.attach(conn);
				                	
				                    //把读监听注册到Selector上,对读事件永远只注册一次
				                	registerChannel(selector,channel,SelectionKey.OP_READ);
				                	
				                    
				                } catch (IOException e) {
				                	channel.close();
				                	channel = null;
				                    //throw e;
				                }
							}
						}
						
						//既然轮询到了此通道，就一起判断这个channel是否有数据要读
						if (key.isReadable()) {
							//取得一个可用的读线程
							ReaderThread reader = pool.getReader();
							//无可用的线程，则等下一次SELECT
							if (reader !=null) {								
								reader.serviceChannel(key);
							}							
						}
						
						//如果没有注册SelectionKey.OP_WRITE Selector不会监视SelectionKey.OP_WRITE，永远返回false
						//但通道有可能是空闲的可以写入数据的
						
						if (key.isWritable()) {
							System.out.println("key isWirtable");
						}
						else
						{
							System.out.println("key not isWirtable");
						}
						
						it.remove(); //已经处理过了，从selected set 中移除这个key.
					}
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					isRunning = false;
					LOG.error(e.getMessage());
					break;
				} catch (ClosedSelectorException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					isRunning = false;
					LOG.error(e.getMessage());
					break;
				}
				
			}
		}
		
	}
	
	protected void registerChannel(Selector selector,SelectableChannel channel,int ops) throws IOException {
		if (channel == null) {
			return;
		}
		channel.configureBlocking(false);
		channel.register(selector, ops);
	}
	
	private void RequestHandler(AbstractENData req) {

		if (req.getCtrl1() == HeartBeatV1Request.CLIENT_REQ && 
				req.getCtrl2() == HeartBeatV1Request.ACTION_CODE  ) {
			
			
			
		}else if (req.getCtrl1() == StartChargeV1Request.CLIENT_REQ && 
				req.getCtrl2() == StartChargeV1Request.ACTION_CODE && 
				req.getParam() == StartChargeV1Request.PARAM_CODE ) {
			
			
			
		}else if (req.getCtrl1() == StopChargeV1Request.CLIENT_REQ && 
				req.getCtrl2() == StopChargeV1Request.ACTION_CODE && 
				req.getParam() == StopChargeV1Request.PARAM_CODE) {
			
			
			
		}else if (req.getCtrl1() == StopChargeV1Request.CLIENT_REQ && 
				req.getCtrl2() == StopChargeV1Request.ACTION_CODE && 
				req.getParam() == StopChargeV1Request.PARAM_CODE) {
			
			
		
		}else if (req.getCtrl1() == ResetV1Request.CLIENT_REQ && 
				req.getCtrl2() == ResetV1Request.ACTION_CODE ) {
			
			
			
			
		}else if (req.getCtrl1() == DataUpdateV1Request.CLIENT_REQ && 
			req.getCtrl2() == DataUpdateV1Request.ACTION_CODE && 
			req.getParam() == DataUpdateV1Request.PARAM_PARKING) {
			
			
		}else if (req.getCtrl1() == DataUpdateV1Request.CLIENT_REQ && 
				req.getCtrl2() == DataUpdateV1Request.ACTION_CODE && 
				req.getParam() == DataUpdateV1Request.PARAM_CHARGING) {
			
			
				
		}else if (req.getCtrl1() == ChargeBookV1Request.CLIENT_REQ && 
				req.getCtrl2() == ChargeBookV1Request.ACTION_CODE && 
				req.getParam() == ChargeBookV1Request.PARAM_CODE) {
			
			
				
		}else if (req.getCtrl1() == ChargeBookCancelV1Request.CLIENT_REQ && 
				req.getCtrl2() == ChargeBookCancelV1Request.ACTION_CODE && 
				req.getParam() == ChargeBookCancelV1Request.PARAM_CODE) {
			
			
		}
	}
	
	
	/**
	 * 
	 * @author wanwy
	 *使用独立的 Selector 选择器
	 */
	private class WriterThread extends Thread {

       
        public void run() {
            while(isRunning) {
                try {
                	//如果并发不大的情况下，使用一个线程也没有什么
                    registWriters();
                    //上面注册后，下面肯定有结果返回
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
                    LOG.error(e.getMessage(), e);
                }
            }
        }


        
        //从数据队列中取得需要发送的数据，并注册读请求
        public void registWriters() throws IOException {
        	
        	
            //Iterator<WriteSession> it = sendQueue.iterator();
            while(!sendQueue.isEmpty()) {
            	//WriteSession session = it.next();
            	WriteSession session = sendQueue.poll();
            	if (session == null) {
            		continue;
            	}
                
                SocketChannel channel =  session.connection.getChannel();
                //通道是否在选择器上注册
                SelectionKey key = channel.keyFor(writeSelector);
                try {
                	//没有注册
                    if (key == null) {
                        try {
                            channel.register(writeSelector, SelectionKey.OP_WRITE, session);
                        } catch (ClosedChannelException e) {
                            //the client went away
                            if (LOG.isTraceEnabled())
                                LOG.trace("the client went away", e);
                        }
                    } else {
                    	//此Selector 只关心 OP_WRITE
                        //key.interestOps(SelectionKey.OP_WRITE);
                        //是直接写效率更高吗？
                        if((key.interestOps() & SelectionKey.OP_WRITE) == 0) {
                        	key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                        }
                    }
                } catch (CancelledKeyException e) {
                    if (LOG.isTraceEnabled())
                        LOG.trace("the client went away", e);
                }
            }
        }


        private void doAsyncWrite(SelectionKey key) throws IOException {
            WriteSession session = (WriteSession) key.attachment();
            if(session.connection.getChannel() != key.channel()) {
                throw new IOException("bad channel");
            }
            int numBytes = channelWrite(session.connection.getChannel(), session.sendBuffer);
            
            if (numBytes <0) {
            	
            	//写返回-1 一般是通道Socket 已经断开连接了
            	key.interestOps(0);
            	key.cancel();
            	
            }else if (session.sendBuffer.remaining() ==0) {
            	//session.isDone = true;  数据已经出列了不置标志也没有关系 。
            	
            	
            	key.interestOps(0);
            }else if (session.sendBuffer.remaining() >0) {
            	
            	//不改变key.interestops,重新把Session追加到 Queue;
            	sendQueue.offer(session) ;      	
            	
            }
           
        }
        
    }

	private class ThreadPool{
		List<ReaderThread> idle = new LinkedList<ReaderThread>();
		
		
		ThreadPool(int poolSize){
			for(int i=0;i<poolSize;i++) {
				ReaderThread thread = new ReaderThread(this);
				thread.setName("Worker" + (i+1));
				thread.start();
				idle.add(thread);
			}
		}
		
		//find an idle reader thread ,if any,could return null;
		ReaderThread getReader() {
			ReaderThread reader = null;
			
			synchronized(idle) {
				if (idle.size() >0) {
					reader = idle.remove(0);
				}
			}
			return reader;			
		}
		
		void returnReader(ReaderThread reader) {
			synchronized(idle) {
				idle.add(reader);
			}
		}
		
	}
	
	private class ReaderThread extends Thread{
		//我们为线程分配读缓冲不适合业务逻辑，因为协议解析的时候，有可能会连帧，所以需要给每个通道一个缓冲区
		private ByteBuffer buffer = ByteBuffer.allocate(4096);
		private ThreadPool pool;
		private SelectionKey key;
		
		ReaderThread(ThreadPool pool){
			this.pool = pool;
		}
		
		public synchronized void run() {
			while(isRunning) {
				try {
					//等待发送读号
					this.wait();
				}
				catch(InterruptedException e) {
					//e.printStackTrace();
					this.interrupted();
				}
				if (key == null) {
					continue;
				}
				
				try {
					drainChannel(key);
					
					//读数据
					
				}
				catch(Exception e) {
					try {
						//移动网络 GPRS 信号不稳定时，未测试
						key.channel().close();
						key.cancel();
					}
					catch(IOException ex) {
						ex.printStackTrace();
					}
					key.selector().wakeup();
				}
				key = null;
				this.pool.returnReader(this);
			}
		}
		
		/**
        * Called to initiate a unit of work by this worker thread on the
        * provided SelectionKey object. This method is synchronized, as is the
        * run( ) method, so only one key can be serviced at a given time.
        * Before waking the worker thread, and before returning to the main
        * selection loop, this key's interest set is updated to remove OP_READ.
        * This will cause the selector to ignore read-readiness for this
        * channel while the worker thread is servicing it.
        * 通过一个被提供SelectionKey对象的工作线程来初始化一个工作集合，这个方法是同步的，所以
        * 里面的run方法只有一个key能被服务在同一个时间，在唤醒工作线程和返回到主循环之前，这个key的
        * 感兴趣的集合被更新来删除OP_READ，这将会引起工作线程在提供服务的时候选择器会忽略读就绪的通道
        */
        synchronized void serviceChannel(SelectionKey key)  //没有修饰词，则默认为public
        {
            this.key = key;
            //先取消读监听，等处理完了以后再注册OP_READ
            key.interestOps(key.interestOps() & (~SelectionKey.OP_READ)); 
           
            //外部线程通知reader 线程 可以开始工作了
            this.notify(); // Awaken the thread
        }
		
        /**
         * The actual code which drains the channel associated with the given
         * key. This method assumes the key has been modified prior to
         * invocation to turn off selection interest in OP_READ. When this
         * method completes it re-enables OP_READ and calls wakeup( ) on the
         * selector so the selector will resume watching this channel.
         */        
		void drainChannel(SelectionKey key) throws Exception 
        {
			
            SocketChannel channel = (SocketChannel) key.channel();
            
            ClientConnection conn =(ClientConnection) key.attachment();
            if (conn == null) {
            	return;
            }

            if (!channel.isOpen() || !channel.isConnected()) {
                //System.out.println("SocketChannel已经close");
            	LOG.debug("SocketChannel已经close");
                return;
            }
            
            int count;
            buffer.clear(); // 清空buffer
            if (conn.getRemainingData() != null) {
            	try {
            		buffer.put(conn.getRemainingData());
            	}
            	catch(BufferOverflowException ex) {
            		conn.setRemainingData(null);
            	}
            	            	
            }
            
            // Loop while data is available; channel is nonblocking
            //此处未考虑缓冲区满的情况
            while ((count = channel.read(buffer)) > 0)
            {
                buffer.flip(); // make buffer readable

                //进行帧解析，
                
                
                
                if (buffer.remaining() ==0) {
                	buffer.clear();                	
                }

                try {
	                //解析过后，缓冲区还是满的，数据肯定是无效的。
	                IENProtocolParser parser = conn.getParser();
	                
	                if (parser != null) {
	                	
	                	byte[] data = new byte[buffer.remaining()];
	                	buffer.get(data, 0, buffer.remaining());
	                	
	                	List<AbstractENData> requests =  parser.Parser(data);
	                	if (!requests.isEmpty()) {
	                		final Iterator<AbstractENData> it = requests.iterator();
	                		while(it.hasNext()) {
	
	                			RequestHandler(it.next());
	                			//处理完了移除
	                			it.remove();
	                		}                		
	                	}                	
	                }
                }catch(Exception ex) {
                	
                }finally {
                	
                
                }
            }
            
            
            if (count < 0) 
            {
                // Close channel on EOF; invalidates the key
                channel.close();
                key.cancel();
                return;
            }
            
            // Resume interest in OP_READ
            key.interestOps(key.interestOps() | SelectionKey.OP_READ);
            // Cycle the selector so this key is active again
            key.selector().wakeup();
        }
		
		
	}
	
	public boolean isStarted() {
		// TODO Auto-generated method stub
		return isRunning;
	}

	public void join() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	public void setMaxThreadSize(int size) {
		// TODO Auto-generated method stub
		maxThreadNum = size;
	}
	
	private int channelRead(ReadableByteChannel channel, ByteBuffer buffer) throws IOException {
        return buffer.remaining() <= NIO_BUFFER_LIMIT ? channel.read(buffer) : channleIO(channel, null, buffer);
    }

    private int channelWrite(WritableByteChannel channel, ByteBuffer buffer) throws IOException {
    	//小于最大缓冲区，则返回通道写结果
        return buffer.remaining() <= NIO_BUFFER_LIMIT ? channel.write(buffer) : channleIO(null, channel, buffer);
    }


    private int channleIO(ReadableByteChannel readCh, WritableByteChannel writeCh, ByteBuffer buffer) throws IOException {
        int initRemaining = buffer.remaining();
        int originalLimit = buffer.limit();

        int ret = 0;
        try {
        	
            while (buffer.remaining() > 0) {
                int ioSize = Math.min(buffer.remaining(), NIO_BUFFER_LIMIT);  //最大的IO数不能大于最大缓冲
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
	
    private class WriteSession {
        ClientConnection connection;       
        ByteBuffer sendBuffer;
        boolean isDone;
        public WriteSession(ClientConnection connection,ByteBuffer buffer) {
            this.connection = connection;
            this.sendBuffer = buffer;
            
        }
    }
    
    
    
    //通道是不需要注册的，直接写
	public void send(CMI cmi ) throws IOException {
		if (cmi == null)
			return;
		
		if (clients.containsKey(cmi.getGatewaySerialNo())) {
			ClientConnection conn = clients.get(cmi.getGatewaySerialNo());
			
			
			ByteBuffer buffer = cmi.getBuffer();
			
			WriteSession session = new WriteSession(conn,buffer);
			sendQueue.offer(session);
			
			/*try {

				SocketChannel channel = conn.getChannel();
				int numBytes = channelWrite(channel, buffer);

				if (numBytes <0) {					
					
					//removeChannel(key,false);
					
					fireChargerOffline(cmi.getGatewaySerialNo(),0);
					
					return ;
				}			
				
			}catch(IOException e) {
				fireChargerOffline(cmi.getGatewaySerialNo(),0);
				//removeChannel(key,true);
			}*/
            
		}
	}
	
	public CMI sendWaitResponse(CMI cmi) throws IOException {
		if (cmi == null)
			throw new java.lang.IllegalArgumentException("cmi paramemter is null");
		
		if (clients.containsKey(cmi.getGatewaySerialNo())) {
			ClientConnection conn = clients.get(cmi.getGatewaySerialNo());
			
			ByteBuffer buffer = cmi.getBuffer();
			
			WriteSession session = new WriteSession(conn,buffer);
			sendQueue.offer(session);
			
		}
		
		AbstractENData request =(AbstractENData) cmi;
		AbstractENData response = null;
		long remaining = 5 * 1000; //5秒
		final long end = System.currentTimeMillis() + remaining;
		boolean received = false;
		while (!received && remaining > 0) {
			try {
				wait(10);				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//查询Receive list 
			if (!receiveQueue.isEmpty()) {
				
				Iterator<AbstractENData> it = receiveQueue.iterator();
				while(it.hasNext()) {
					response = it.next();
					if (request.getMsgId() == response.getMsgId()) {
						received = true;
						it.remove();
						notify();
					}				
				}
			}
			
			
			if (received)
				break;
			
			remaining = end - System.currentTimeMillis();
		}
		
		
		return response;
	}
	
	protected void removeChannel(SelectionKey key, boolean ioException) {
		try {
			/*if (ioException && key.attachment() != null) { // GPRS偶尔不稳定的情况，实际TCP未断开
				KeyAttach keyAttach = (KeyAttach) key.attachment();
				long ioExceptionTime = keyAttach.getIoExceptionTime();
				if (ioExceptionTime == 0 || System.currentTimeMillis() - ioExceptionTime < 2 * 60 * 1000) {
					if (ioExceptionTime == 0) {
						keyAttach.setIoExceptionTime(System.currentTimeMillis());
					}
					log.info("【暂不移除TCP通讯】 EN-Gate序列号为：" + keyAttach.getEnGateSerialNum() + ", ioExceptionTime = "
							+ ioExceptionTime + ", System.currentTimeMillis() = " + System.currentTimeMillis());
					return;
				}
			}
*/
			/*if (key.attachment() != null) {
				KeyAttach keyAttach = (KeyAttach) key.attachment();
				enGateRunTimeManager.removeSingleEnGateConnection(keyAttach.getEnGateSerialNum(), true);
				key.attach(null);
				log.info("【移除TCP通讯】 EN-Gate序列号为：" + keyAttach.getEnGateSerialNum());
			}*/

			SocketChannel socketChannel = (SocketChannel) key.channel();
			socketChannel.finishConnect();
			socketChannel.socket().close();
			socketChannel.close();
			key.cancel();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addChargerListener(ChargerListener l) {
		// TODO Auto-generated method stub
		listeners.add(l);
	}

	public void removeChargerListener(ChargerListener l) {
		// TODO Auto-generated method stub
		listeners.remove(l);
	}

	private void fireChargerOffline(final String deviceSerialNo,final int deviceType)
	{
		final ConnectionStateEvent e = new ConnectionStateEvent(this, deviceSerialNo, deviceType);
		final EventListener[] el = listeners.listeners();
		for (int i = 0; i < el.length; i++) {
			final ChargerListener l = (ChargerListener) el[i];
			try {
				l.chargerOffline(e);
			}
			catch (final RuntimeException ex) {
				removeChargerListener(l);
				LOG.error("removed event listener", ex);
			}
		}
	}
	
	private void fireChargerOnline(final String deviceSerialNo,final int deviceType) {
		final ConnectionStateEvent e = new ConnectionStateEvent(this, deviceSerialNo, deviceType);
		final EventListener[] el = listeners.listeners();
		for (int i = 0; i < el.length; i++) {
			final ChargerListener l = (ChargerListener) el[i];
			try {
				l.chargerOnline(e);
			}
			catch (final RuntimeException ex) {
				removeChargerListener(l);
				LOG.error("removed event listener", ex);
			}
		}
	}
	
	private void fireLogin(final String deviceSerialNo,final int deviceType) {
		final ConnectionStateEvent e = new ConnectionStateEvent(this, deviceSerialNo, deviceType);
		final EventListener[] el = listeners.listeners();
		for (int i = 0; i < el.length; i++) {
			final ChargerListener l = (ChargerListener) el[i];
			try {
				l.loginRequst(data);
			}
			catch (final RuntimeException ex) {
				removeChargerListener(l);
				LOG.error("removed event listener", ex);
			}
		}
	}
	
	
	
}
