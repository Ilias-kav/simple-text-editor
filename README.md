# simple-text-editor
A java program for simple text manipulation.
How to run: java mypackage.MyEditor xxx.txt
where xxx your file name

How it works:
command || operation
^       || Go to the first lie
$       || Go to the last line
"-"       || Go up one line
"+"       || Go down one line
a       || Add new line after current line (the user is asked to type in the text for the new line)
t       || Add new line before current line (the user is asked to type in the text for the new line)
d       || Delete current line
l       || Print all lines
n       || Toggle whether line numbers are displayed when printing all lines (l command)
p       || Print current line
q       || Quit without save
w       || Write file to disk
x       || Exit with save
=       || Print current line number
#       || Print number of lines and characters
c       || Creates an indexing file xxx.txt.ndx. Prints the number of pages of the file.
v       || Prints the content of the indexing file (word, line numbers)
s       || Searches for word and prints every line it appears on (serial search - exhaustive)
b       || Searches for word and prints every line it appears on (binary search)

Some useful information:
The .txt file is loaded in memory and implemented as a doubly linked list.
Contents of .txt file are split into words, coupled with the line numbers they appear on and inserted in an Arraylist
before written to .ndx file.
Line size, maximum word size, minimum word size as well as disk page size are all defined in MyEditor.class and can be changed
to suit your system's needs. (default values: 80, 20, 5, 128)
Words of length > max size are trimmed down to max size.
Words of length < min size are not included in the .ndx file.


