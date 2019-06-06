#!/bin/bash
# sh run.sh test_class_name [-r N]

jjtree parser.jjt
javacc parser.jj
javac *.java
java JmmParser $1.jmm $2 $3
java -jar jasmin.jar $1.j