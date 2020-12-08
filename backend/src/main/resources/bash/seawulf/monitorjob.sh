#!/usr/bin/env bash
echo Monitoring Seawulf starting... Using monitorjob.sh...
source /etc/profile.d/modules.sh;
module load slurm
JOBID=$(cat temp1.txt)
JOBNAME=$(cat temp2.txt)
SERVER_JOBFILE=monitor.txt
CONTENTS=$(scontrol show job ${JOBID} | grep JobState | cut  -d " " -f 4 | cut -d = -f 2)
echo ${CONTENTS} > ./jobs/${JOBNAME}/${SERVER_JOBFILE}
ls ./jobs/${JOBNAME}/${SERVER_JOBFILE}
echo Monitoring Seawulf complete
