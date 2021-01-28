#include <stdlib.h>
#include <iostream>
#include <string>
#include <mutex>
#include "../include/connectionHandler.h"
#include "thread"
#include "../include/userInputReaderSender.h"
#include "../include/EncoderDecoder.h"


int main (int argc, char *argv[]) {
//    if (argc < 3) {
//        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
//        return -1;
//    }

    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler  a(host, port);
    ConnectionHandler& connectionHandler=a;

    if(!(connectionHandler.connect())) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }


    std::mutex mutex;
//    serverListener serverlistener(1,mutex,connectionHandler);
    userInputReaderSender userInputReaderSender(2,connectionHandler);
//    std::thread th1(&serverListener::run, &serverlistener);
    std::thread th2(&userInputReaderSender::run, &userInputReaderSender);
//    th1.join();
//    th2.join();
    bool shouldTerminate;
    char* bytesToRead = new char [1024];
    char* currByte=new char[1];
    int currInd=0;
    while(!shouldTerminate){
        if(connectionHandler.isReadableBytes()!=0) {
            connectionHandler.getBytes(currByte, 1);
            EncoderDecoder::decodeNextByte(bytesToRead, currByte, currInd);
            currInd=currInd+1;
        }
        else if(currInd>0){
            short messageOpcode = EncoderDecoder::Decode(bytesToRead, currInd);
            if (messageOpcode == 4) {
                shouldTerminate = true;
                userInputReaderSender.shouldTerminate = true;
            } else{
                userInputReaderSender.isLogoutOccured=false;
            }
            currInd=0;
        }
    }
    delete currByte;
    delete bytesToRead;
    connectionHandler.close();

    th2.join();

    return 0;
}
