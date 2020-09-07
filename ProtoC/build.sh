#!/bin/bash

clear
gcc -Wall -O2 `pkg-config --cflags --libs libprotobuf-c` main.c msg.pb-c.c -o test


