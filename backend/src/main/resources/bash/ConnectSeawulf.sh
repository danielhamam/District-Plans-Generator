#!/usr/bin/env bash
NETID=$1
FILE_PATH=$2

if [[ ! -f ${FILE_PATH} ]] #if the file does not exist exit script
then
    echo 'Exiting program...'
    exit
fi
echo ${NETID} is the netid you entered
scp ${FILE_PATH} ${NETID}@login.seawulf.stonybrook.edu:./
ssh ${NETID}@login.seawulf.stonybrook.edu '
echo Connected to remote server;
pwd;
ls $1;
echo \"#TODO: EXPAND ON THIS SCRIPT\";
#module load slurm;
#module load anaconda/3;
'
#cat src/main/resources/seawulf.slurm | ssh <carlopez>@login.seawulf.stonybrook.edu
#'source /etc/profile.d/modules.sh;
#module load slurm;
#module load anaconda/3;
#cd <GO-TO-DIRECTORY-OF-CHOICE>;
#sbatch'q