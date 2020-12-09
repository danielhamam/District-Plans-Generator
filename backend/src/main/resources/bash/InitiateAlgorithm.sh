#!/usr/bin/env bash

echo "InitiateAlgorithm.sh..."

#input variables
NETID=$1
JOB_DIR_NAME=$2
JOB_NAME=$3
NUM_OF_PLANS=$4

#bash script variables
JOB_DIR_PATH=${JOB_DIR_NAME}${JOB_NAME}/
ALGO_FILE_PATH=${JOB_DIR_PATH}AlgorithmInput.json
FILE_TO_TRANSFER1=temp1.txt
FILE_TO_TRANSFER2=temp2.txt

#conditional: if the file does not exist exit script
if [[ ! -f ${ALGO_FILE_PATH} ]]
then
    echo 'Error Occurred. Exiting program...'
    exit
fi

#Create file, send file, remove file
echo ${JOB_NAME} > ${FILE_TO_TRANSFER1}
scp ${FILE_TO_TRANSFER1} ${NETID}@login.seawulf.stonybrook.edu:./
rm -v ${FILE_TO_TRANSFER1}

#Create file, send file, remove file
echo ${NUM_OF_PLANS} > ${FILE_TO_TRANSFER2}
scp ${FILE_TO_TRANSFER2} ${NETID}@login.seawulf.stonybrook.edu:./
rm -v ${FILE_TO_TRANSFER2}

#send dir, remove file
scp -r ${JOB_DIR_PATH} ${NETID}@login.seawulf.stonybrook.edu:./

ssh ${NETID}@login.seawulf.stonybrook.edu '
./movedir.sh;
./runalgorithm.sh
'

#get file from seawulf
scp -r ${NETID}@login.seawulf.stonybrook.edu:./jobs/${JOB_NAME}/seawulfjobid.txt ${JOB_DIR_PATH}
echo seawulfjobid.txt has be received from The Seawulf
echo
