package bgu.spl.net;


public class Message {

    private short opcode;
    private short opcodeMessage;
    private String message = "";
    private short courseNum ;


    public Message(){}

    public Message(short opcode, String msg){
        this.opcode = opcode;
        this.message= msg;
    }
    public Message(short opcode,short opcodeMessage){
        this.opcode = opcode;
        this.opcodeMessage = opcodeMessage;
    }
    public Message(short opcode,short opcodeMessage,String message){
        this.opcode = opcode;
        this.opcodeMessage = opcodeMessage;
        this.message = message;
    }
    public short getOpcode() {
        return opcode;
    }

    public void setOpcode(short opcode){
        this.opcode = opcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public short getOpcodeMessage() {
        return opcodeMessage;
    }

    @Override
    public String toString() {
        return "Message{" +
                "opcode=" + opcode +
                ", opcodeMessage=" + opcodeMessage +
                ", message='" + message + '\'' +'}';
    }

    public short getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(short courseNum) {
        this.courseNum = courseNum;
    }
}