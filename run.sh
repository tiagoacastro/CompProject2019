#!/bin/bash
# sh run.sh parser_file_name parser_class_name test_class_name

jjtree $1.jjt
javacc $1.jj
javac *.java
java $2 $3