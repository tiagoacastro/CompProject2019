#!/bin/bash
# sh run.sh test_class_name

jjtree parser.jjt
javacc parser.jj
javac *.java
java JmmParser $1.jmm
java -jar jasmin.jar $1.j