#!/bin/bash
for i in {1..5000}; do echo "A AAPL B L 435."`shuf -i 0-50 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> trades; done; for i in {1..5000}; do echo "A AAPL S L 435."`shuf -i 0-50 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> trades; done; for i in {1..5000}; do echo "A GOOG B L 843."`shuf -i 35-95 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> trades; done; for i in {1..5000}; do echo "A GOOG S L 843."`shuf -i 35-95 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> trades; done; for i in {1..5000}; do echo "A MSFT B L 28."`shuf -i 20-45 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> trades; done; for i in {1..5000}; do echo "A MSFT S L 28."`shuf -i 20-45 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> trades; done; shuf trades > trades.i; rm -f trades; shuf trades.i > trades.in; rm -f trades.i

#echo "A AAPL B L 435."`shuf -i 0-50 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> aapl
#echo "A AAPL S L 435."`shuf -i 0-50 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> aapl

#echo "A GOOG B L 843."`shuf -i 35-95 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> goog
#echo "A GOOG S L 843."`shuf -i 35-95 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> goog

#echo "A MSFT B L 28."`shuf -i 20-45 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> msft
#echo "A MSFT S L 28."`shuf -i 20-45 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> msft

