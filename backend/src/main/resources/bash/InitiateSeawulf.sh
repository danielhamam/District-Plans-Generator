#!/usr/bin/env bash
NETID=$1
JOB_DIR=$2
JOB_NAME=$3

JOB_PATH=${JOB_DIR}${JOB_NAME}/
ALGO_FILE_PATH=${JOB_PATH}AlgorithmInput.json
SEAWULF_JOBFILE=temp.txt


if [[ ! -f ${ALGO_FILE_PATH} ]] #if the file does not exist exit script
then
    echo 'Exiting program...'
    exit
fi


echo ${JOB_NAME} > ${SEAWULF_JOBFILE}
scp ${SEAWULF_JOBFILE} ${NETID}@login.seawulf.stonybrook.edu:./
scp -r ${JOB_PATH} ${NETID}@login.seawulf.stonybrook.edu:./
rm -v temp.txt
ssh ${NETID}@login.seawulf.stonybrook.edu '
source /etc/profile.d/modules.sh;
module load python/3.8.6;
pwd;
./movedir.sh;
./runalgorithm.sh
./rmtempfile.sh
module unload python/3.8.6;
'









#cat src/main/resources/seawulf.slurm | ssh <carlopez>@login.seawulf.stonybrook.edu
#'source /etc/profile.d/modules.sh;
#module load slurm;
#module load anaconda/3;
#cd <GO-TO-DIRECTORY-OF-CHOICE>;
#sbatch'q