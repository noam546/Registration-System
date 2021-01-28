package bgu.spl.net;

import bgu.spl.net.api.MessageEncoderDecoder;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BGRSEncoderDecoder implements MessageEncoderDecoder<Message> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private Message msg;
    private boolean newMessage = true;
    private int zeroCounter = 0;

    @Override
    public Message decodeNextByte(byte nextByte) {
        if(msg != null && !newMessage){
            boolean case1 = msg.getOpcode()== (short)4 || msg.getOpcode()== (short)11 || msg.getOpcode()==(short)13;
            boolean case2 = msg.getOpcode()== (short)5||msg.getOpcode()== (short)6||msg.getOpcode()== (short)7||msg.getOpcode()== (short)9||msg.getOpcode()== (short)10;
            boolean case3 = msg.getOpcode()== (short)1||msg.getOpcode()== (short)2||msg.getOpcode()== (short)3;
            if(case2 && len == 1){
                bytes[1] = nextByte;
                msg.setCourseNum(bytesToShort(bytes));
                len = 0;
                newMessage = true;
                return msg;
            }
            else if(case1){
                newMessage = true;
                len = 0;
                return msg;
            }
            else if(msg.getOpcode() == (short)8 && nextByte == '\0'){
                msg.setMessage(popString());
                newMessage = true;
                zeroCounter = 0;
                return msg;
            }
            else if(case3){
                if(nextByte == '\0')
                    zeroCounter++;
                if(zeroCounter == 2){
                    msg.setMessage(popString());
                    newMessage = true;
                    zeroCounter = 0;
                    return msg;
                }
            }

        }
        if(len == 1){
            if(newMessage){
                pushByte(nextByte);
                msg = new Message();
                msg.setOpcode(bytesToShort(bytes));
                len = 0;
                newMessage = false;
                if(msg.getOpcode() == (short)4 || msg.getOpcode() == (short)11){
                    newMessage = true;
                    return msg;
                }
                else{
                    return null;
                }
            }
        }
        pushByte(nextByte);
        return null;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private String popString(){
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    @Override
    public byte[] encode(Message message) {
        byte[] opcode = shortToBytes(message.getOpcode());
        byte[] opcodeMsg = shortToBytes(message.getOpcodeMessage());
        byte[] msg ;
        if(message.getOpcode() == (short)12){
            msg = (message.getMessage()+ "\0").getBytes();
        }
        else{
            msg = new byte[0];
        }
        byte[] whole = new byte[msg.length+opcode.length+opcodeMsg.length];
        for(int i=0; i < opcode.length; i++){
            whole[i] = opcode[i];
        }
        for(int i=0; i < opcodeMsg.length; i++){
            whole[i+opcode.length] = opcodeMsg[i];
        }
        for(int i=0; i < msg.length; i++){
            whole[i+opcode.length+opcodeMsg.length] = msg[i];
        }
        return whole;
    }

    public static short bytesToShort(byte[] byteArr)//maybe not supposed to be here
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    public static byte[] shortToBytes(short num)//maybe not supposed to be here
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
}
