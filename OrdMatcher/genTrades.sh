#!/bin/bash
for i in {1..5000}; do echo "A AAPL B 435."`shuf -i 0-50 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> trades; done; for i in {1..5000}; do echo "A AAPL S 435."`shuf -i 0-50 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> trades; done; for i in {1..5000}; do echo "A GOOG B 843."`shuf -i 35-95 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> trades; done; for i in {1..5000}; do echo "A GOOG S 843."`shuf -i 35-95 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> trades; done; for i in {1..5000}; do echo "A MSFT B 28."`shuf -i 20-45 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> trades; done; for i in {1..5000}; do echo "A MSFT S 28."`shuf -i 20-45 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> trades; done

#echo "A AAPL B 435."`shuf -i 0-50 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> aapl
#echo "A AAPL S 435."`shuf -i 0-50 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> aapl

#echo "A GOOG B 843."`shuf -i 35-95 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> goog
#echo "A GOOG S 843."`shuf -i 35-95 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> goog

#echo "A MSFT B 28."`shuf -i 20-45 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> msft
#echo "A MSFT S 28."`shuf -i 20-45 -n 1` $((`shuf -i 10-1000 -n 1`*10)) "usr"`shuf -i 1-50 -n 1` >> msft

