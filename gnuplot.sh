#!/bin/bash
if [ -x "$(command -v gnuplot)" ]; then
gnuplot $1
else
docker run --rm -v $(pwd):/work remuslazar/gnuplot $1
fi

