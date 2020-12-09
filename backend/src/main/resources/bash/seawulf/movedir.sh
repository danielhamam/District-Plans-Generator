#!/usr/bin/env bash
echo
echo movedir.sh: Starting script...
JOBDIR=$(cat temp1.txt)
mv ${JOBDIR} ./jobs
echo ${JOBDIR} moved into /job directory
mkdir -v ./jobs/${JOBDIR}/algorithm-output
echo

