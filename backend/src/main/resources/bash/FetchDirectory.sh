#!/usr/bin/env bash

#input variables
NETID=$1
JOB_DIR_PATH=$2
SEAWULF_DIR_PATH=$3


#get directory from seawulf
scp -rf ${NETID}@login.seawulf.stonybrook.edu:${SEAWULF_DIR_PATH} ${JOB_DIR_PATH}
echo ${SEAWULF_DIR_PATH} directory has be received from The Seawulf
