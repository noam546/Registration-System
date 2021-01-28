//
// Created by spl211 on 31/12/2020.
//

#ifndef BOOST_ECHO_CLIENT_USERINPUTREADERSENDER_H
#define BOOST_ECHO_CLIENT_USERINPUTREADERSENDER_H

#include <mutex>
#include "connectionHandler.h"


class userInputReaderSender {
private:
    int id;
    ConnectionHandler& connectionHandler;

public:
    userInputReaderSender(int id, ConnectionHandler& connectionHandler);
    void run();
    void setShouldTerminate(bool value);
    bool shouldTerminate;
    bool isLogoutOccured;


};


#endif //BOOST_ECHO_CLIENT_USERINPUTREADERSENDER_H
