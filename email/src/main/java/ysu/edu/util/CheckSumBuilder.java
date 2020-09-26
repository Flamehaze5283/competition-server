package ysu.edu.util;

import java.security.MessageDigest;

public class CheckSumBuilder {//计算并获取checkSum
    public static String getCheckSum(String appSecret,String nonce,String curTime){
        return encode("SHA",appSecret+nonce+curTime);
    }

    private static String encode(String algorithm,String value){
        if(value == null){
            return null;
        }
        try {
            MessageDigest messageDigest=MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFormattedText(byte[] bytes){
        int len=bytes.length;
        StringBuilder sb=new StringBuilder(len*2);
        for(int $i=0;$i<len;$i++){
            sb.append(HEX_DIGITS[(bytes[$i]>>4)&0x0f]);
            sb.append(HEX_DIGITS[bytes[$i]&0x0f]);
        }
        return sb.toString();
    }
    private static final char[] HEX_DIGITS={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

}
