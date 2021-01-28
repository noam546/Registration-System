//
// Created by spl211 on 29/12/2020.
//

#ifndef BOOST_ECHO_CLIENT_ENCODERDECODER_H
#define BOOST_ECHO_CLIENT_ENCODERDECODER_H

#include "string"


class EncoderDecoder {
public:
    static char* Encode(std::string msg);
    static short bytesToShort(char* bytesArr);
    static void shortToBytes(short num, char* bytesArr);
    static short Decode(char* bytesToDecode,int size);
    static void decodeNextByte(char* bytesToDecode,char* currByte, int currInd);

private:
    static std::string processLine(std::string str);
    static void setOpcode(std::string msg,char* bytes);

};


#endif //BOOST_ECHO_CLIENT_ENCODERDECODER_H
