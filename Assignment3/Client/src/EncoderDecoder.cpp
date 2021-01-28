//
// Created by spl211 on 29/12/2020.
//

#include <iostream>
#include "../include/EncoderDecoder.h"
#include <boost/lexical_cast.hpp>

char * EncoderDecoder::Encode(std::string msg) {
    std::string submsg=msg.substr(msg.find(' ')+1);
    std::string msgType=msg.substr(0,msg.find(' '));
    std::string firstTrim = processLine(msg);
    char* bytes=new char[submsg.length()+3];
    short opcode;

    if(msg.find(' ') > msg.length()){//LOGOUT and MYCOURSES input dont have spaces
        if(msgType.compare("LOGOUT")==0){
            opcode=4;
            shortToBytes(opcode, bytes);
        }
        else if(msgType.compare("MYCOURSES")==0){
            opcode=11;
            shortToBytes(opcode, bytes);
        }
        else{
            opcode=13;
            shortToBytes(opcode, bytes);
        }
        return bytes;
    }


    if(msgType.compare("ADMINREG")==0){//1
        opcode=1;
        shortToBytes(opcode, bytes);
    }
    else if(msgType.compare("STUDENTREG")==0){//2
        opcode=2;
        shortToBytes(opcode, bytes);
    }
    else if(msgType.compare("LOGIN")==0){//3
        opcode=3;
        shortToBytes(opcode, bytes);
    }
    else if(msgType.compare("COURSEREG")==0){//5
        opcode=5;
        shortToBytes(opcode, bytes);
    }
    else if(msgType.compare("KDAMCHECK")==0){//6
        opcode=6;
        shortToBytes(opcode, bytes);
    }
    else if(msgType.compare("COURSESTAT")==0){//7
        opcode=7;
        shortToBytes(opcode, bytes);
    }
    else if(msgType.compare("STUDENTSTAT")==0){//8
        opcode=8;
        shortToBytes(opcode, bytes);
        firstTrim = firstTrim+'\0';
    }
    else if(msgType.compare("ISREGISTERED")==0){//9
        opcode=9;
        shortToBytes(opcode, bytes);
    }
    else if(msgType.compare("UNREGISTER")==0){//10
        opcode=10;
        shortToBytes(opcode, bytes);
    }
    else{
        opcode=13;
        shortToBytes(opcode, bytes);
        return bytes;
    }

    if(opcode == 5 || opcode == 6 || opcode == 7 || opcode == 9 || opcode == 10){
        try
        {
            short myShort = boost::lexical_cast<short>(firstTrim);
            char* temp = new char [2];
            shortToBytes(myShort,temp);
            bytes[2] = temp[0];
            bytes[3] = temp[1];
            delete[] temp;
            return bytes;
        }
        catch(boost::bad_lexical_cast &)
        {
            opcode=13;
            shortToBytes(opcode, bytes);
            return bytes;
        }

    }

    for(unsigned int i=2;i<firstTrim.length()+2;i++){//copy the context of the str
        bytes[i]=firstTrim[i-2];
    }

    return bytes;
}


std::string EncoderDecoder::processLine(std::string str) {
    std::string firstTrim=str.substr(str.find(' ')+1);
    int firstSpace = firstTrim.find(' ');
    if(firstSpace != -1){
        firstTrim = firstTrim.substr(0,firstSpace)+'\0'+firstTrim.substr(firstSpace+1)+'\0';
    }
    return firstTrim;
}



short EncoderDecoder::bytesToShort(char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void EncoderDecoder::shortToBytes(short num, char* bytesArr)
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

short EncoderDecoder::Decode(char *bytesToDecode, int size) {
    short opcode=bytesToShort(bytesToDecode);
    short messageOpcode = (short)((bytesToDecode[2] & 0xff) << 8);//get the opcode of the message
    messageOpcode += (short)(bytesToDecode[3] & 0xff);
    if((short)opcode == (short)12){//ACK message
        std::string msg="";
        for(int i=4;i<size;i++){//decode str to string
            msg=msg+bytesToDecode[i];
        }
        std::cout << "ACK " << messageOpcode << msg << std::endl;
        if(messageOpcode == 4){//LOGOUT ack
            return messageOpcode;
        }
    }
    else if(opcode==(short)13){//ERROR message
        std::cout << "ERROR " << messageOpcode << std::endl;
    }
    return 0;

}
void EncoderDecoder::decodeNextByte(char *bytesToDecode, char* currByte, int currInd) {
    bytesToDecode[currInd]=currByte[0];
}
