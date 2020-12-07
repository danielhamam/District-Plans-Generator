package com.cse416.backend.service;

import com.cse416.backend.dao.services.JobDAOService;
import com.cse416.backend.model.enums.JobStatus;
import com.cse416.backend.model.job.Job;

import java.io.*;

public class AlgorithmInterface implements Runnable {
    private Thread proxyThread = null;
    private String netid;
    private Job job;
    private boolean die = false;
    private boolean isAlgorithmLocal;
    private boolean isComputeLocationDetermined;
    private boolean isJobCancelled;
    private String jobDirectoryRelativePath;
    private Process localAlgorithmProcess;
    private String jobDirectory;
    private JobDAOService jobDAO;

    public AlgorithmInterface(String netid, Job job, boolean runAlgoLocally) {
        this.netid = netid;
        this.job = job;
        this.isAlgorithmLocal = runAlgoLocally;
        this.isComputeLocationDetermined = false;
        this.isJobCancelled = false;
        this.jobDirectory = "src/main/resources/system/jobs/";
        this.jobDirectoryRelativePath = "../system/jobs/";
        this.jobDAO = new JobDAOService();
    }

    public Job getJob(){
        return job;
    }

    private void printProcessOutput(Process process) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append("\t\t");
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            String result = builder.toString();
            System.out.format("Process:\n%s\n", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (proxyThread == null) {
            System.out.println("Starting Thread...");
            proxyThread = new Thread(this);
            proxyThread.start();
        }
    }

    public void kill(){
        die = true;
    }

    public void run() {
        while (!die) {
            try {
                System.out.println(this + " thread is still running. Job's status: " + job.getStatus());
                //TODO: If the algo is ran locally check to see if the script crashed to kill the thread.
                // The thread becomes useless if the python crashed
                if (!isComputeLocationDetermined) {
                    determineAlgorithmComputeLocation();
                    sleepThread();
                }
                monitorAlgorithm();
                JobStatus status = job.getStatus();
                if(status.equals(JobStatus.COMPLETED)){
                    //TODO: THIS IS WHERE YOU EXTRACT INFORMATION. SIGNIGIES JOB IS COMPLETE.
                    kill();
                }

                if(isJobCancelled){
                    cancelJob();

                }
                sleepThread();
            }
            catch(InterruptedException ie){
                System.out.println(this + "Interrupted thread..");
            }
            catch (IOException error) {
                System.out.println("IOException Killing thread..");
                kill();
            }
            catch (Exception error) {
                error.printStackTrace();
                System.out.println(error.getCause().getMessage() + " Exception Killing thread..");
                kill();
            }
        }
    }

    public void cancelJobDriver() {
        this.isJobCancelled = true;
    }

    public void cancelJob()throws IOException{
        if(isAlgorithmLocal){
            if(localAlgorithmProcess.isAlive()){
                localAlgorithmProcess.destroy();
            }
            kill();
        }else{
            System.out.println("Canceling job remotely...  Bash output...");
            ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/bash/CancelAlgorithm.sh",
                    netid, job.getSeawulfJobID());
            pb.redirectErrorStream(true);
            Process tempProcess = pb.start();
            printProcessOutput(tempProcess);
        }
    }

    private void sleepThread() throws InterruptedException{
        Thread.sleep(3000);
    }

    private void extractDataFromCompleteJob(){
        if(isAlgorithmLocal) {
//            String jobDirectoryAbsolutePath =  new File(jobDirectoryRelativePath).getAbsolutePath();
//            File file = new File(jobDirectoryAbsolutePath + "/AlgorithmOutput.json");
//            doesFileExist = file.exists();
        }
        else{
            //TODO: SCRIPT GOES HERE
        }

    }

    private void monitorAlgorithm()throws IOException, InterruptedException{
        boolean doesFileExist = false;
        if(isAlgorithmLocal) {
            doesFileExist = !localAlgorithmProcess.isAlive();
            if(doesFileExist){
                job.setStatus(JobStatus.COMPLETED);
                jobDAO.updateJob(job);
            }
        }
        else{
            System.out.println("Monitoring The Seawulf");
            ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/bash/MonitorSeawulf.sh",
                    netid, jobDirectory, job.getJobName(), job.getSeawulfJobID());
            pb.redirectErrorStream(true);
            Process tempProcess = pb.start();
            printProcessOutput(tempProcess);
            Thread.sleep(5000);
            String monitorFilePath = jobDirectory + job.getJobName().toLowerCase() + "/monitor.txt";
            String monitorAbsoluteFilePath = new File(monitorFilePath).getAbsolutePath();
            System.out.println(monitorAbsoluteFilePath);
            String seawulfResponse = new BufferedReader(new FileReader(monitorAbsoluteFilePath)).readLine();
            System.out.println(seawulfResponse);
            String jobStatus = seawulfResponse.substring(seawulfResponse.indexOf(" ")).trim();
            JobStatus status =  JobStatus.getEnumFromString(jobStatus);
            if(!job.getStatus().equals(status)){
                job.setStatus(status);
//                jobDAO.updateJob(job);
            }
        }
    }

    private void determineAlgorithmComputeLocation()throws IOException {
        String algorithmInputFileName = "AlgorithmInput.json";
        String algorithmInputPath = jobDirectory + algorithmInputFileName;
        if(isAlgorithmLocal){
            System.out.println("Running algorithm locally... Python output...");
            String localPythonScript = "src/main/resources/python/algorithm/Algorithm.py";
            ProcessBuilder pb = new ProcessBuilder("python3", localPythonScript, algorithmInputPath);
            pb.redirectErrorStream(true);
            localAlgorithmProcess = pb.start();
            printProcessOutput(localAlgorithmProcess);
        }
        else{
            System.out.println("Running algorithm remotely...  Bash output...");
            ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/bash/InitiateAlgorithm.sh",
                    netid, jobDirectory, job.getJobName());
            pb.redirectErrorStream(true);
            Process tempProcess = pb.start();
            printProcessOutput(tempProcess);
        }
        isComputeLocationDetermined = true;
    }

    private void initiateServerProcessing(){
        String jobDirectoryAbsolutePath =  new File(jobDirectoryRelativePath).getAbsolutePath();
        File algorithmOutputFile = new File(jobDirectoryAbsolutePath + "/AlgorithmOutput.json");
        job.processAlgorithmOutput(algorithmOutputFile);

    }
}
