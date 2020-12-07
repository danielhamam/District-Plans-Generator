#!/usr/bin/env bash

#todo: change the --job-name --output with script variables

#SBATCH --job-name=bronco_practice_test
#SBATCH --output=bronco_practice_test.log
#SBATCH -p short-24core
#SBATCH --ntasks-per-node=24
#SBATCH --nodes=1
#SBATCH --time=00:30:00
#SBATCH --mail-type=BEGIN,END
#SBATCH --mail-user=carlos.lopez@stonybrook.edu

source /etc/profile.d/modules.sh;
module load python/3.8.6;
module load gnu-parallel/6.0
module load intel/mpi/64/2017/0.098

#input variables
JOB_NAME=$1
NUM_OF_JOBS=$2

#script variables
PARALLEL_INPUT_FILENAME=commands.txt
PARALLEL_INPUT_FILEPATH=./jobs/${JOB_NAME}/${PARALLEL_INPUT_FILENAME}

cd /gpfs/home/carlopez

#create commands.txt file
touch ${PARALLEL_INPUT_FILEPATH}

#Loop for writing to commands.txt file. Setting up for parallel
COUNTER=0
while [${COUNTER} -le $(NUM_OF_JOBS) ]
do
    echo python3 Algorithm.py jobs/${JOB_NAME}/AlgorithmInput.json jobs/${JOB_NAME}/algorithm-output/ ${COUNTER} >> ./jobs/${JOB_NAME}${PARALLEL_INPUT_FILENAME}
    COUNTER=$(expr ${COUNTER} + 1)
done


cat ${PARALLEL_INPUT_FILEPATH}| parallel --verbose --jobs 0
echo slurm script ending

module unload python/3.8.6;
module unload gnu-parallel/6.0
module unload intel/mpi/64/2017/0.098

