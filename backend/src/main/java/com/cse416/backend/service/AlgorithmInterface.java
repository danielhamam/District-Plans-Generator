package com.cse416.backend.service;

import com.cse416.backend.model.job.Job;
import com.cse416.backend.model.regions.state.State;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class AlgorithmInterface implements Runnable {
    private Thread proxyThread = null;
    private boolean die = false;
    private Job job;
    private State state;
    private boolean runAlgoLocally;
    private boolean determinedAlgoComputeLocation;
    private String jobDirectoryRelativePath;

    public AlgorithmInterface(Job job, State state, boolean runAlgoLocally) {
        this.job = job;
        this.state = state;
        this.runAlgoLocally = runAlgoLocally;
        this.determinedAlgoComputeLocation = false;
        this.jobDirectoryRelativePath = "src/main/resources/system/jobs/" + job.getJobName();
    }

    public void kill() {
        die = true;
    }


    public void run() {
        while (!die)
        {
            System.out.println(this + " thread is still running");
            if(!determinedAlgoComputeLocation){
                try{
                    determineAlgorithmComputeLocation();
                    Thread.sleep(3000);
                }
                catch(InterruptedException error) {
                    System.out.println("Interrupted thread..");
                }
                catch (IOException error) {
                    System.out.println("Killing thread..");
                    kill();
                }
            }
            boolean doesFileExist = doesAlgorithmOutputFileExist();
            if(doesFileExist){
                initiateServerProcessing();
            }
            try{
                Thread.sleep(3000);
            }
            catch(InterruptedException ie){

            }
        }
    }

    public void start() {
        System.out.println("Starting Maybe Thread...");
        if (proxyThread == null) {
            System.out.println("Starting Thread...");
            proxyThread = new Thread(this);
            proxyThread.start();
        }
    }

    private boolean doesAlgorithmOutputFileExist(){
        boolean doesFileExist = false;
        if(runAlgoLocally) {
            String jobDirectoryAbsolutePath =  new File(jobDirectoryRelativePath).getAbsolutePath();
            File file = new File(jobDirectoryAbsolutePath + "/AlgorithmOutput.json");
            doesFileExist = file.exists();
        }
        else{
            //TODO: Script to check
        }
        return doesFileExist;
    }

    private void initiateServerProcessing(){
        String jobDirectoryAbsolutePath =  new File(jobDirectoryRelativePath).getAbsolutePath();
        File algorithmOutputFile = new File(jobDirectoryAbsolutePath + "/AlgorithmOutput.json");
        job.processAlgorithmOutput(algorithmOutputFile);

    }

    private void determineAlgorithmComputeLocation()throws IOException {
        if(runAlgoLocally){
            System.out.println("Running algorithm locally... Python output...");
            String localPath = "src/main/resources/python/algorithm/AlgorithmDanny_p3.py";
            //TODO:CHANGE THE LINE BELOW TO REFLECT InputAlgorithm.json
            String arg = "src/main/resources/python/algorithm/algorithm_test.json";
            ProcessBuilder pb = new ProcessBuilder("python3", localPath, arg);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            printProcessOutput(process);
        }
        else{
            System.out.println("Running algorithm remotely...  Bash output...");
            ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/bash/ConnectSeawulf.sh");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            printProcessOutput(process);
        }
        determinedAlgoComputeLocation = true;
    }


    private void printProcessOutput(Process process) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            String result = builder.toString();
            System.out.println("\t\t" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
