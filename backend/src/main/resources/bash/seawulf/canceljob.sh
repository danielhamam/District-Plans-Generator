#!/usr/bin/env bash
echo
source /etc/profile.d/modules.sh;
module load slurm
echo canceljob.sh:
JOBID=$(cat temp.txt)
scancel ${JOBID}
echo Canceling Job Finished
echo

