package com.cse416.backend.service;

import com.cse416.backend.model.job.Job;
import com.cse416.backend.model.regions.state.State;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AlgorithmInterface implements Runnable {
    private Thread proxyThread = null;
    private boolean die = false;
    private Job job;
    private State state;
    private boolean runAlgoLocally;
    private boolean determinedAlgoComputeLocation;

    public AlgorithmInterface(Job job, State state, boolean runAlgoLocally) {
        this.job = job;
        this.state = state;
        this.runAlgoLocally = runAlgoLocally;
        this.determinedAlgoComputeLocation = false;
    }

    public void kill() {
        die = true;
    }


    public void run() {
        while (!die)
        {
            if(!determinedAlgoComputeLocation){
                try{
                    determineAlgorithmComputeLocation();
                    Thread.sleep(300000);
                }
                catch(InterruptedException error) {
                    System.out.println("Interrupted thread..");
                }
                catch (IOException error) {
                    System.out.println("Killing thread..");
                    kill();
                }
            }
            int num = (int) (Math.random() * 10);
            System.out.println("\t\t\t\t" + num);
            try { Thread.sleep(300000); }
            catch(InterruptedException ie) {}
        }
    }

    public void start() {
        if (proxyThread == null) {
            proxyThread = new Thread(this);
            proxyThread.start();
        }
    }

    private void determineAlgorithmComputeLocation()throws IOException {
        if(runAlgoLocally){
            System.out.println("Running algorithm locally... Python output...");
            ProcessBuilder pb = new ProcessBuilder("python3",
                    "src/main/resources/python/algorithm/AlgorithmDanny_p3.py");
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
