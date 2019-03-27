#!/bin/bash
# sh run.sh filename classname

jjtree $1.jjt
javacc $1.jj
javac *.java
java $2