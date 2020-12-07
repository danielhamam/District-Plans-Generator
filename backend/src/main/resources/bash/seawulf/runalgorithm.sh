#!/usr/bin/env bash
source /etc/profile.d/modules.sh;
module load slurm
JOBNAME=$(cat temp.txt)
SJOBID=$(sbatch test.slurm ${JOBNAME} | cut -d " " -f 4)
echo ${SJOBID} > ./jobs/${JOBNAME}/seawulfjobid.txt
echo Algorithm submitted. seawulfjobid.txt created.
module unload slurm
#python3 AlgorithmDanny_p3.py ./jobs/${JOBNAME}/AlgorithmInput.json
#echo pretending to run algorithm..
#echo as > ./jobs/${JOBNAME}/AlgorithmOutput.json


