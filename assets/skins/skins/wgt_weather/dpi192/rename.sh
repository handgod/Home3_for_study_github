#!/bin/sh

for res in 32 72 96; 
do 
	unset y; 
	for x in 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31; 
	do 
		if test -n "$y"; then 
			from="${res}_$x.png"; to="${res}_$y.png"; 
			echo "$from -> $to"; 
			mv $from $to;
		fi; 
		y=$x; 
	done; 
done
