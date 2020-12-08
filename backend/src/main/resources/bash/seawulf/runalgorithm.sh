#!/usr/bin/env bash
echo Running the Algorithm... Using runalgorithm.sh
source /etc/profile.d/modules.sh;
module load slurm
JOBNAME=$(cat temp1.txt)
NUM_OF_JOBS=$(cat temp2.txt)
SJOBID=$(sbatch test.slurm ${JOBNAME} ${NUM_OF_JOBS}| cut -d " " -f 4)
echo ${SJOBID} > ./jobs/${JOBNAME}/seawulfjobid.txt
echo Algorithm submitted. seawulfjobid.txt created in /jobs/${JOBNAME}/ directory
module unload slurm
#python3 AlgorithmDanny_p3.py ./jobs/${JOBNAME}/AlgorithmInput.json
#echo pretending to run algorithm..
#echo as > ./jobs/${JOBNAME}/AlgorithmOutput.json


