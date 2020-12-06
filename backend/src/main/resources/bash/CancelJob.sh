#!/usr/bin/env bash
NETID=$1
JOBID=$2
SEAWULF_JOBFILE=temp.txt

#Create file, send file, remove file
echo ${JOBID} > ${SEAWULF_JOBFILE}
scp ${SEAWULF_JOBFILE} ${NETID}@login.seawulf.stonybrook.edu:./
rm -v ${SEAWULF_JOBFILE}

ssh ${NETID}@login.seawulf.stonybrook.edu '
./canceljob.sh
./rmtemp.sh
'
