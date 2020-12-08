#!/usr/bin/env bash
source /etc/profile.d/modules.sh;
module load slurm
echo Canceling Job... Using canceljob.sh...
JOBID=$(cat temp.txt)
scancel ${JOBID}
echo Canceling Job Finished

