package cn.com.zbev.charger.netzbplus;

import java.nio.ByteBuffer;

public final class ByteHelper {

	public static String byteArrayToHexStr(byte[] byteArray) {
	    if (byteArray == null){
	        return null;
	    }
	    char[] hexArray = "0123456789ABCDEF".toCharArray();
	    char[] hexChars = new char[byteArray.length * 2];
	    for (int j = 0; j < byteArray.length; j++) {
	        int v = byteArray[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	
	public static byte[] hexStrToByteArray(String str)
	{
	    if (str == null) {
	        return null;
	    }
	    if (str.length() == 0) {
	        return new byte[0];
	    }
	    byte[] byteArray = new byte[str.length() / 2];
	    for (int i = 0; i < byteArray.length; i++){
	        String subStr = str.substring(2 * i, 2 * i + 2);
	        byteArray[i] = ((byte)Integer.parseInt(subStr, 16));
	    }
	    return byteArray;
	}
	
    /**
     * byte[]转int
     * @param b
     * @return
     */
    public static int byteArrayToInt(byte[] b) {  
        return   b[3] & 0xFF |  
                (b[2] & 0xFF) << 8 |  
                (b[1] & 0xFF) << 16 |  
                (b[0] & 0xFF) << 24;  
    }
    
    /**
     * 2个byte转int
     * @param b
     * @return
     */
    public static int byteArrayToInt2(byte[] b) {  
        return  (b[0] & 0xff << 8) |  (b[1] & 0xFF);  
    }
    
    public static int byteArrayToInt(byte[] b, int index){
    	  return   b[index+3] & 0xFF |  
                  (b[index+2] & 0xFF) << 8 |  
                  (b[index+1] & 0xFF) << 16 |  
                  (b[index+0] & 0xFF) << 24;  
    }
    
    /**
     * int转byte[]
     * @param a
     * @return
     */
    public static byte[] intToByteArray(int a) {  
        return new byte[] {  
            (byte) ((a >> 24) & 0xFF),  
            (byte) ((a >> 16) & 0xFF),     
            (byte) ((a >> 8) & 0xFF),     
            (byte) (a & 0xFF)  
        };  
    }
    
    /** 
     * short转byte[]
     *  
     * @param b 
     * @param s 
     * @param index 
     */  
    public static void byteArrToShort(byte b[], short s, int index) {  
        b[index + 1] = (byte) (s >> 8);  
        b[index + 0] = (byte) (s >> 0);  
    }
    
    /** 
     * byte[]转short 
     *  
     * @param b 
     * @param index 
     * @return 
     */  
    public static short byteArrToShort(byte[] b, int index) {  
        return (short) (((b[index + 0] << 8) | b[index + 1] & 0xff));  
    }
    
    /** 
     * 16位short转byte[] 
     *  
     * @param s 
     *            short 
     * @return byte[]
     * */  
    public static byte[] shortToByteArr(short s) {  
        byte[] targets = new byte[2];  
        for (int i = 0; i < 2; i++) {  
            int offset = (targets.length - 1 - i) * 8;  
            targets[i] = (byte) ((s >>> offset) & 0xff);  
        }  
        return targets;  
    }
    /**
     * byte[]转16位short
     * @param b
     * @return
     */
    public static short byteArrToShort(byte[] b){
    	return byteArrToShort(b,0);
    }
    
    /**
     * long转byte[]
     * @param x
     * @return
     */
    public static byte[] longToBytes(long x) {
    	ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, x);  
        return buffer.array();  
    }  
    /**
     * byte[]转Long
     * @param bytes
     * @return
     */
    public static long bytesToLong(byte[] bytes) {  
    	ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes, 0, bytes.length);  
        buffer.flip();//need flip   
        return buffer.getLong();  
    }
    
}
