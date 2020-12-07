#!/usr/bin/env bash
echo Moving Directory... Using movedir.sh...
JOBDIR=$(cat temp.txt)
mv ${JOBDIR} ./jobs
echo ${JOBDIR} moved into /job directory
mkdir -v ./jobs/${JOBDIR}/algorithm-output

