#!/usr/bin/env bash
echo Process runalgorithm.sh: Starting script...
source /etc/profile.d/modules.sh;
module load slurm
JOBNAME=$(cat temp1.txt)
NUM_OF_JOBS=$(cat temp2.txt)
echo Job Name:${JOBNAME} Number of Plans: ${NUM_OF_JOBS}
SJOBID=$(sbatch --export=JOB_NAME=${JOBNAME},NUM_OF_JOBS=${NUM_OF_JOBS} test.slurm | cut -d " " -f 4)
echo ${SJOBID} > ./jobs/${JOBNAME}/seawulfjobid.txt
echo Algorithm submitted. seawulfjobid.txt created in /jobs/${JOBNAME}/ directory
module unload slurm
echo
#python3 AlgorithmDanny_p3.py ./jobs/${JOBNAME}/AlgorithmInput.json
#echo pretending to run algorithm..
#echo as > ./jobs/${JOBNAME}/AlgorithmOutput.json

