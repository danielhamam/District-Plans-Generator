#!/usr/bin/env bash

##create commands.txt file
#
#NUM_OF_JOBS=$1\
NETID=$1
#
#PARALLEL_INPUT_FILEPATH=./temp.txt
#touch ${PARALLEL_INPUT_FILEPATH}
#
##Loop for writing to commands.txt file. Setting up for parallel
#COUNTER=0
#while [ ${COUNTER} -lt ${NUM_OF_JOBS} ]
#do
#    echo python3 Algorithm.py AlgorithmInput.json algorithm-output/ ${COUNTER} >> ${PARALLEL_INPUT_FILEPATH}
#    COUNTER=$(expr ${COUNTER} + 1)
#done
echo ${NETID}@login.seawulf.stonybrook.edu
ssh ${NETID}@login.seawulf.stonybrook.edu '
pwd
echo asdfghjkl;
'

#411655
#411657
#411677
#411799
