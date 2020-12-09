#!/usr/bin/env bash

echo MonitorSeawulf.sh...
#input variables
NETID=$1
JOB_DIR_PATH=$2
JOBNAME=$3
JOB_SEAWULF_ID=$4

#bash script variables
FILE_TO_TRANSFER1=temp1.txt
FILE_TO_TRANSFER2=temp2.txt

#Create file, send file, remove file
echo ${JOB_SEAWULF_ID} > ${FILE_TO_TRANSFER1}
scp ${FILE_TO_TRANSFER1} ${NETID}@login.seawulf.stonybrook.edu:./
rm -v ${FILE_TO_TRANSFER1}

#Create file, send file, remove file
echo ${JOBNAME} > ${FILE_TO_TRANSFER2}
scp ${FILE_TO_TRANSFER2} ${NETID}@login.seawulf.stonybrook.edu:./
rm -v ${FILE_TO_TRANSFER2}


ssh ${NETID}@login.seawulf.stonybrook.edu '
pwd;
./monitorjob.sh
'

#get file from seawulf
scp ${NETID}@login.seawulf.stonybrook.edu:./jobs/${JOBNAME}/monitor.txt ${JOB_DIR_PATH}/${JOBNAME}/
echo monitor.txt has be received from The Seawulf
