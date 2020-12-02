#!/usr/bin/env bash
NETID=$1
JOBNAME=$2
JOBID=$3
SEAWULF_JOBFILE=temp.txt

echo ${JOBID} > ${SEAWULF_JOBFILE}
scp ${SEAWULF_JOBFILE} ${NETID}@login.seawulf.stonybrook.edu:./
rm -v ${SEAWULF_JOBFILE}
ssh ${NETID}@login.seawulf.stonybrook.edu '
./canceljob.sh
'
