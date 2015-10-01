line_start=`grep -n  "Score    Error  Units"  $2  | sed -e 's/:.*/g/'`
line_end=`wc -l $2`
diff=$($line_end - $line_start)
tail -$diff $2  | sed -e 's/ \+/;/g' | sed -e 's/;Error/;;Error/g' > $2.csv

