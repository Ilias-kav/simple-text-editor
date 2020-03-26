# simple-text-editor

A java program for simple text manipulation.

###### How to run:

java mypackage.MyEditor xxx.txt
where xxx your file name

###### How it works:

- command || operation
1. ^       || Go to the first lie
2. $       || Go to the last line
3. "-"     || Go up one line
4. "+"     || Go down one line
5. a       || Add new line after current line (the user is asked to type in the text for the new line)
6. t       || Add new line before current line (the user is asked to type in the text for the new line)
7. d       || Delete current line
8. l       || Print all lines
9. n       || Toggle whether line numbers are displayed when printing all lines (l command)
10. p      || Print current line
11. q      || Quit without save
12. w      || Write file to disk
13. x      || Exit with save
14. =      || Print current line number
15. "#"    || Print number of lines and characters
16. c      || Creates an indexing file xxx.txt.ndx. Prints the number of pages of the file.
17. v      || Prints the content of the indexing file (word, line numbers)
18. s      || Searches for word and prints every line it appears on (serial search - exhaustive)
19. b      || Searches for word and prints every line it appears on (binary search)

###### Some useful information:

The .txt file is loaded in memory and implemented as a doubly linked list.
Contents of .txt file are split into words, coupled with the line numbers they appear on and inserted in an Arraylist
before written to .ndx file.
Line size, maximum word size, minimum word size as well as disk page size are all defined in MyEditor.class and can be changed
to suit your system's needs. (default values: 80, 20, 5, 128)
Words of length > max size are trimmed down to max size.
Words of length < min size are not included in the .ndx file.


