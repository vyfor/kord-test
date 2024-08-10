#! /bin/bash

VERSION=8.9.1

cd ~
sudo apt-get update -y
sudo apt-get install -y nghttp2 libnghttp2-dev libssl-dev build-essential wget
wget https://curl.haxx.se/download/curl-${VERSION}.tar.gz
tar -xzvf curl-${VERSION}.tar.gz 
rm -f curl-${VERSION}.tar.gz
cd curl-${VERSION}
./configure --prefix=/usr/lib --with-ssl --with-nghttp2 --enable-websockets
make -j4
sudo make install
sudo ldconfig
cd ~
rm -rf curl-${VERSION}
