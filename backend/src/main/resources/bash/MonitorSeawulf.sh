#!/usr/bin/env bash
NETID=$1
JOBNAME=$2

ssh ${NETID}@login.seawulf.stonybrook.edu '
echo Connected to remote server;
pwd;
cd ./jobs/${JOBNAME}
pwd;
find AlgorithmOutput.json
echo \"#TODO: EXPAND ON THIS SCRIPT\";
'
#cat src/main/resources/seawulf.slurm | ssh <carlopez>@login.seawulf.stonybrook.edu
#'source /etc/profile.d/modules.sh;
#module load slurm;
#module load anaconda/3;
#cd <GO-TO-DIRECTORY-OF-CHOICE>;
#sbatch'q