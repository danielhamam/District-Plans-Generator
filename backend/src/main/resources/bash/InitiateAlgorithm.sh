#!/usr/bin/env bash
NETID=$1
JOB_DIR_NAME=$2
JOB_NAME=$3

JOB_DIR_PATH=${JOB_DIR_NAME}${JOB_NAME}/
ALGO_FILE_PATH=${JOB_DIR_PATH}AlgorithmInput.json
FILE_TO_TRANSFER=temp.txt

pwd
echo ${ALGO_FILE_PATH}
if [[ ! -f ${ALGO_FILE_PATH} ]] #if the file does not exist exit script
then
    echo 'Error Occurred. Exiting program...'
    exit
fi


echo ${JOB_NAME} > ${FILE_TO_TRANSFER}
scp ${FILE_TO_TRANSFER} ${NETID}@login.seawulf.stonybrook.edu:./
scp -r ${JOB_DIR_PATH} ${NETID}@login.seawulf.stonybrook.edu:./
rm -v ${FILE_TO_TRANSFER}
ssh ${NETID}@login.seawulf.stonybrook.edu '
source /etc/profile.d/modules.sh;
module load python/3.8.6;
pwd;
./movedir.sh;
./runalgorithm.sh
./rmtemp.sh
module unload python/3.8.6;
'









#cat src/main/resources/seawulf.slurm | ssh <carlopez>@login.seawulf.stonybrook.edu
#'source /etc/profile.d/modules.sh;
#module load slurm;
#module load anaconda/3;
#cd <GO-TO-DIRECTORY-OF-CHOICE>;
#sbatch'q