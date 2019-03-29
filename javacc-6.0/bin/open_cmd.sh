#!/bin/bash
pushd . > /dev/null
cd `dirname ${BASH_SOURCE[0]}`
MY_PATH=`pwd`
popd > /dev/null
PATH=$PATH:$MY_PATH:$1
export PATH
bash
kill -9 $PPID
