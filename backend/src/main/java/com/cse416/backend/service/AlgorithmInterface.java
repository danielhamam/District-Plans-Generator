package com.cse416.backend.service;

import com.cse416.backend.dao.services.*;
import com.cse416.backend.model.enums.JobStatus;
import com.cse416.backend.model.job.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;

public class AlgorithmInterface implements Runnable {
    private Thread proxyThread = null;
    private String jobDirectoryRelativePath;
    private String netid;
    private String jobDirectory;
    private boolean die = false;
    private boolean isAlgorithmLocal;
    private boolean isComputeLocationDetermined;
    private boolean isJobCancelled;
    private Process localAlgorithmProcess;
    private Job job;


    //DAO Servicers
    @Autowired(required = false)
    private JobDAOService jobDAO;

    @Autowired(required = false)
    private CountyDAOService countyDAO;

    @Autowired(required = false)
    private DemographicDAOService demographicDAO;



    public AlgorithmInterface(String netid, Job job, boolean runAlgoLocally) {
        this.netid = netid;
        this.job = job;
        this.isAlgorithmLocal = runAlgoLocally;
        this.isComputeLocationDetermined = false;
        this.isJobCancelled = false;
        this.jobDirectory = "src/main/resources/system/jobs/";
        this.jobDirectoryRelativePath = "../system/jobs/";
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
            System.out.println(jobDAO == null);
            System.out.println(countyDAO == null);
            System.out.println(demographicDAO == null);

//
//            System.out.println("Starting Thread...");
//            proxyThread = new Thread(this);
//            proxyThread.start();
        }
    }

    public void kill(){
        die = true;
    }

    public void run() {
        while (!die) {
            try {
                System.out.println(this + " thread is still running." +
                        "Job's status: " + job.getStatus() + "\t" +
                        "Job's seawulfJobID: " + job.getSeawulfJobID());

                if(isJobCancelled){
                    cancelJob();
                    break;
                }

                if (!isComputeLocationDetermined) {
                    determineAlgorithmComputeLocation();
                    continue;
                }

                monitorAlgorithm();

                JobStatus status = job.getStatus();
                if(status.equals(JobStatus.COMPLETED)){
                    extractDataFromJob();
                    initiateServerProcessing();
                    kill();
                }

                longSleepThread();
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
        System.out.println("Killed thread: " + this);
    }

    public void cancelJobDriver() {
        this.isJobCancelled = true;
    }

    private void cancelJob()throws IOException{
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

    private void longSleepThread() throws InterruptedException{
        System.out.println("Sleeping Thread " + this + " : Long Sleep");
        Thread.sleep(20000);
    }

    private void shortSleepThread() throws InterruptedException{
        System.out.println("Sleeping Thread " + this + " : Short Sleep");
        Thread.sleep(10000);
    }

    private void initiateServerProcessing(){
        String algorithmOutputPath = jobDirectory + job.getJobName().toLowerCase() +
                "/algorithm-output/AlgorithmOutput.json";
        String algorithmOutputAbsolutePath = new File(algorithmOutputPath).getAbsolutePath();
        File algorithmOutput = new File(algorithmOutputAbsolutePath);
        if(algorithmOutput.exists()){
            System.out.println("AlgorithmOutput.json exists...\nStarting Processing...");

        }else{
            System.out.println("Error -> AlgorithmOutput.json does not exists...");
        }

    }

    private void extractDataFromJob()throws IOException, InterruptedException{
        if(isAlgorithmLocal) {
            //TODO: For local algorithm run
        }
        else{
            System.out.println("Extract Data from Seawulf");
            String seawulfDirectory = "./jobs/" + job.getJobName().toLowerCase() + "/algorithm-output/";
            ProcessBuilder pbOne = new ProcessBuilder("bash", "src/main/resources/bash/FetchDirectory.sh",
                    netid, jobDirectory + job.getJobName().toLowerCase() + "/", seawulfDirectory);
            pbOne.redirectErrorStream(true);
            Process tempProcess = pbOne.start();
            printProcessOutput(tempProcess);
            shortSleepThread();
            //TODO: CALL PYTHON TO MERGE JSON FILES
            String pythonArg = jobDirectory + job.getJobName().toLowerCase() + "/algorithm-output/";
            ProcessBuilder pbTwo = new ProcessBuilder("python3",
                    "src/main/resources/python/preprocessing/MergeOutputFiles.py", pythonArg);
            pbTwo.redirectErrorStream(true);
            tempProcess = pbTwo.start();
            printProcessOutput(tempProcess);
            shortSleepThread();
        }

    }

    private String getContentsFile(String filename)throws IOException{
        String filepath = jobDirectory + job.getJobName().toLowerCase() + "/" + filename;
        String absoluteFilePath = new File(filepath).getAbsolutePath();
        String response = new BufferedReader(new FileReader(absoluteFilePath)).readLine();
        response = response.trim();
        return response;
    }

    private void monitorAlgorithm()throws IOException, InterruptedException{
        boolean doesFileExist = false;
        if(isAlgorithmLocal) {
            doesFileExist = !localAlgorithmProcess.isAlive();
            if(doesFileExist){
                job.setStatus(JobStatus.COMPLETED);
                //jobDAO.updateJob(job);
            }
        }
        else{
            System.out.println("Monitoring The Seawulf...");
            ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/bash/MonitorSeawulf.sh",
                    netid, jobDirectory, job.getJobName().toLowerCase(), job.getSeawulfJobID());
            pb.redirectErrorStream(true);
            Process tempProcess = pb.start();
            //printProcessOutput(tempProcess);
            shortSleepThread();
            String jobStatus = getContentsFile("monitor.txt");
            System.out.println("Status recieved from the seawulf: " + jobStatus);
            JobStatus status =  JobStatus.getEnumFromString(jobStatus);
            if(!job.getStatus().equals(status)){
                job.setStatus(status);
                //jobDAO.updateJob(job);
            }
        }
    }

    private void determineAlgorithmComputeLocation()throws IOException, InterruptedException{
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
            System.out.println("Running algorithm remotely...");
            ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/bash/InitiateAlgorithm.sh",
                    netid, jobDirectory, job.getJobName().toLowerCase(), ""+job.getNumDistrictingPlan());
            pb.redirectErrorStream(true);
            Process tempProcess = pb.start();
            //printProcessOutput(tempProcess);
            shortSleepThread();
            String seawulfJobID = getContentsFile("seawulfjobid.txt");
            job.setSeawulfJobID(seawulfJobID);
            //jobDAO.updateJob(job);
        }
        isComputeLocationDetermined = true;
    }

}
