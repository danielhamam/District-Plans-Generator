#!/usr/bin/env bash

#todo: change the --job-name --output with script variables

#SBATCH --job-name=GA-I
#SBATCH --output=GA-I.log
#SBATCH -p long-28core
#SBATCH --ntasks-per-node=1
#SBATCH --nodes=1
#SBATCH --time=12:00:00
#SBATCH --mail-type=BEGIN,END
#SBATCH --mail-user=carlos.lopez@stonybrook.edu

source /etc/profile.d/modules.sh;
module load python/3.8.6;
module load gnu-parallel/6.0
module load intel/mpi/64/2017/0.098

#input variables
#JOB_NAME=$1
#NUM_OF_JOBS=$2
echo ${JOB_NAME}
echo ${NUM_OF_JOBS}

#script variables
JOB_DIRECTORY=jobs/${JOB_NAME}/
PARALLEL_INPUT_FILENAME=commands.txt
PARALLEL_INPUT_FILEPATH=${JOB_DIRECTORY}${PARALLEL_INPUT_FILENAME}


cd /gpfs/home/carlopez

#create commands.txt file
touch ${PARALLEL_INPUT_FILEPATH}

#Loop for writing to commands.txt file. Setting up for parallel
COUNTER=0
while [ ${COUNTER} -lt ${NUM_OF_JOBS} ]
do
    echo python3 SeawulfAlgorithm.py ${JOB_DIRECTORY}AlgorithmInput.json ${JOB_DIRECTORY}algorithm-output/ ${COUNTER} >> ${PARALLEL_INPUT_FILEPATH}
    COUNTER=$(expr ${COUNTER} + 1)
done


cat ${PARALLEL_INPUT_FILEPATH} | parallel --verbose

#Printing Debug Statements
echo
echo command.txt inputs:
cat ${PARALLEL_INPUT_FILEPATH}
echo slurm script ending...

rm -v ${PARALLEL_INPUT_FILEPATH}

module unload python/3.8.6;
module unload gnu-parallel/6.0
module unload intel/mpi/64/2017/0.098


###
# 2 nodes, each with 10 cores -> 20 cores
#mpirun -np 20 python3 Algorithm_2.py