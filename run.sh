#!/bin/bash
# sh run.sh parser_file_name parser_class_name test_file_name

jjtree parser.jjt
javacc parser.jj
javac *.java
java JmmParser $1.jmm
java -jar jasmin.jar $1.j