#!/usr/bin/env bash
source /etc/profile.d/modules.sh;
module load slurm
NETID=$1
JOBID=$(cat temp.txt)
SERVER_JOBFILE=temp.txt
CONTENTS=$(squeue | grep 403556)
echo ${CONTENTS} > ${SERVER_JOBFILE}
scp ${NETID}@login.seawulf.stonybrook.edu:./${SERVER_JOBFILE} ./
