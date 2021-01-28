//
// Created by spl211 on 31/12/2020.
//

#include "../include/userInputReaderSender.h"
#include "../include/EncoderDecoder.h"

userInputReaderSender::userInputReaderSender(int id,  ConnectionHandler& connectionHandler) : id(id), connectionHandler(connectionHandler),shouldTerminate(false),isLogoutOccured(false){
}

void userInputReaderSender::run() {
    while (!shouldTerminate) {
        const short bufsize = 1024;
        char buf[bufsize];
        if(!isLogoutOccured) {
            std::cin.getline(buf, bufsize);
            std::string line(buf);
            //encode string to bytes
            char *bytesToSend = EncoderDecoder::Encode(line);
            int bytesToWrite = line.length() - line.find(' ') + 2;
            if (line.find(' ') > line.length())
                bytesToWrite = 3;
            if (bytesToSend[1] == 5 || bytesToSend[1] == 6 || bytesToSend[1] == 7 || bytesToSend[1] == 9 ||
                bytesToSend[1] == 10)
                bytesToWrite = 4;
            if (bytesToSend[1] == 4) {
                bytesToWrite = 2;
                isLogoutOccured = true;
            }
            if (bytesToSend[1] == 11) {
                bytesToWrite = 2;
            }

            if (!connectionHandler.sendBytes(bytesToSend, bytesToWrite)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            delete bytesToSend;
            if (shouldTerminate) {
                connectionHandler.close();
            }
        }
    }
}
