CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude -lpthread -lboost_thread
LDFLAGS:=-lboost_system -lboost_thread -pthread

all: BGRSclient
	g++ -o bin/BGRSclient bin/connectionHandler.o bin/EncoderDecoder.o bin/main.o bin/userInputReaderSender.o $(LDFLAGS)

BGRSclient: bin/connectionHandler.o bin/EncoderDecoder.o bin/main.o bin/userInputReaderSender.o
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/EncoderDecoder.o: src/EncoderDecoder.cpp
	g++ $(CFLAGS) -o bin/EncoderDecoder.o src/EncoderDecoder.cpp

bin/main.o: src/main.cpp
	g++ $(CFLAGS) -o bin/main.o src/main.cpp

bin/userInputReaderSender.o: src/userInputReaderSender.cpp
	g++ $(CFLAGS) -o bin/userInputReaderSender.o src/userInputReaderSender.cpp
	
.PHONY: clean
clean:
	rm -f bin/*
