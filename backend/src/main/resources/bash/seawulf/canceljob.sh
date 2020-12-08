echo Canceling Job... Using canceljob.sh...
source /etc/profile.d/modules.sh;
module load slurm
JOBID=$(cat temp.txt)
scancel ${JOBID}
echo Canceling Job Finished

