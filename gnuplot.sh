#!/bin/bash

case "$(uname -sr)" in

   CYGWIN*|MINGW*|MINGW32*|MSYS*)
	   _WINDOWS=true
     ;;
esac

if [[ "$_WINDOWS" == "true" ]]; then
ls /c/Program\ Files/gnuplot
/c/Program\ Files/gnuplot/gnuplot $1
elif [ -x "$(command -v gnuplot)" ]; then
gnuplot $1
else
docker run --rm -v $(pwd):/work remuslazar/gnuplot $1
fi

