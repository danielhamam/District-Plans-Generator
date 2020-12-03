source /etc/profile.d/modules.sh;
module load slurm
JOBID=$(cat temp.txt)
scancel ${JOBID}
./remtempfile.sh

