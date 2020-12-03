#!/usr/bin/env bash
JOBNAME=$(cat temp.txt)
python3 AlgorithmDanny_p3.py ./jobs/${JOBNAME}/AlgorithmInput.json
