#!/usr/bin/env bash
source /etc/profile.d/modules.sh;
module load slurm
JOBID=$(cat temp1.txt)
JOBNAME=$(cat temp2.txt)
SERVER_JOBFILE=monitor.txt
CONTENTS=$(scontrol show job 403291 | grep JobState | cut  -d " " -f 4 | cut -d = -f 2)
echo ${CONTENTS} > ./jobs/${JOBNAME}/${SERVER_JOBFILE}
ls ./jobs/${JOBNAME}/${SERVER_JOBFILE}

#scp ${NETID}@login.seawulf.stonybrook.edu:./${SERVER_JOBFILE} ./
