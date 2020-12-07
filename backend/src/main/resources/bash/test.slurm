#!/usr/bin/env bash

#SBATCH --job-name=bronco_practice
#SBATCH --output=bronco_practice_log.log
#SBATCH -p short-24core
#SBATCH --ntasks-per-node=24
#SBATCH --nodes=1
#SBATCH --time=00:10:00
#SBATCH --mail-type=BEGIN,END
#SBATCH --mail-user=carlos.lopez@stonybrook.edu


JOB_NAME=$1
source /etc/profile.d/modules.sh;
module load python/3.8.6;
module load intel/mpi/64/2017/0.098
cd /gpfs/home/carlopez
python3 Algorithm.py jobs/${JOB_NAME}/AlgorithmInput.json

source /etc/profile.d/modules.sh;
module unload python/3.8.6;
module unload intel/mpi/64/2017/0.098

#409670
