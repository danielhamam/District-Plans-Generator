import mysql.connector

mydb = mysql.connector.connect(
  host="mysql3.cs.stonybrook.edu",
  user="kadiallo",
  password="110331331", 
  database="kadiallo"
)

if mydb:
    print("connection successful")
else:
    print("connection unsuccessful")
    exit(0)


mycursor = mydb.cursor()

#Create Tables related to Jobs
jobs = '''
  CREATE TABLE IF NOT EXISTS Jobs (
    jobId INT AUTO_INCREMENT PRIMARY KEY,
    stateID VARCHAR(2) NOT NULL,
    stateFipscode INT NOT NULL, 
    numberOfPlans INT NOT NULL, 
    populationDifference INT NOT NULL,
    compactness VARCHAR(255) NOT NULL,
    numberOfDistricts INT NOT NULL,
    jobStatus VARCHAR(10) NOT NULL,
    jobName VARCHAR(255)
  )'''

summaries = '''
  CREATE TABLE IF NOT EXISTS JobSummaries(
    summaryId INT AUTO_INCREMENT PRIMARY KEY,
    summaryFileId INT,
    jobID INT, 
    FOREIGN KEY (jobID) REFERENCES Jobs(jobId),
    FOREIGN KEY (summaryFileId) REFERENCES Files(fileID)
  )'''

minorityGroups = '''
  CREATE TABLE IF NOT EXISTS JobMinorityGroups(
    minorityGroupId VARCHAR(100),
    jobID INT,
    FOREIGN KEY (minorityGroupId) REFERENCES CensusEthnicities(ethnicityName),
    FOREIGN KEY (jobID) REFERENCES Jobs(jobId) 
  )'''

#Create Tables related to Job plans
plans = '''
  CREATE TABLE IF NOT EXISTS Plans(
    planId INT AUTO_INCREMENT PRIMARY KEY,
    stateID VARCHAR(2) NOT NULL,
    averageDistrictPopulation BIGINT DEFAULT 0,
    averageDistrictCompactness INT DEFAULT 0,
    numberOfDistricts INT DEFAULT 0
  )'''

jobGraphs = '''
  CREATE TABLE IF NOT EXISTS JobGraphs(
    jobId INT,
    planGraphId INT,
    FOREIGN KEY (jobId) REFERENCES Jobs(jobId),
    FOREIGN KEY (planGraphId) REFERENCES JobPlanGraphs(planGraphId)
  )'''

planGraphs = '''
  CREATE TABLE IF NOT EXISTS JobPlanGraphs(
    planGraphId INT AUTO_INCREMENT PRIMARY KEY,
    planId INT,
    lowerExtreme INT DEFAULT 0,
    upperExtreme INT DEFAULT 0,
    lowerQuartile INT DEFAULT 0,
    upperQuartile INT DEFAULT 0,
    median INT DEFAULT 0,
    FOREIGN KEY (planId) REFERENCES Plans(planId) 
  )'''

graphDistricts = '''
  CREATE TABLE IF NOT EXISTS GraphDistricts(
    planGraphId INT,
    districtId INT,
    districtValue INT DEFAULT 0,
    FOREIGN KEY (districtId) REFERENCES Districts(districtId),
    FOREIGN KEY (planGraphId) REFERENCES JobPlanGraphs(planGraphId)
  )'''

planDistricts = '''
  CREATE TABLE IF NOT EXISTS JobPlanDistricts(
    districtId INT,
    planId INT,
    FOREIGN KEY (districtId) REFERENCES Districts(districtId),
    FOREIGN KEY (planId) REFERENCES Plans(planId)
  )'''

planPrecincts = '''
  CREATE TABLE IF NOT EXISTS JobPlanPrecincts(
    precinctId INT,
    planId INT,
    FOREIGN KEY (precinctID) REFERENCES Precincts(precinctId),
    FOREIGN KEY (planId) REFERENCES Plans(planId)
)'''

jobPlans = '''
  CREATE TABLE IF NOT EXISTS JobPlans(
    planId INT,
    jobId INT,
    FOREIGN KEY (planId) REFERENCES Plans(planId),
    FOREIGN KEY (jobId) REFERENCES Jobs(jobId) 
  )'''

extremePlans = '''
  CREATE TABLE IF NOT EXISTS ExtremePlans(
    planId INT,
    jobId INT,
    FOREIGN KEY (planId) REFERENCES Plans(planId),
    FOREIGN KEY (jobId) REFERENCES Jobs(jobId) 
  )'''

averagePlans = '''
    CREATE TABLE IF NOT EXISTS AveragePlans(
    planId INT,
    jobId INT,
    FOREIGN KEY (planId) REFERENCES Plans(planId),
    FOREIGN KEY (jobId) REFERENCES Jobs(jobId) 
)'''

planFiles = '''
  CREATE TABLE IF NOT EXISTS PlanFiles(
    planFileId INT AUTO_INCREMENT PRIMARY KEY,
    planId INT,
    stateId VARCHAR(2),
    stateFileId INT,
    precinctFileId INT, 
    districtFileId INT, 
    FOREIGN KEY (planId) REFERENCES Plans(planId),
    FOREIGN KEY (stateFileId) REFERENCES Files(fileId),
    FOREIGN KEY (precinctFileId) REFERENCES Files(fileId),
    FOREIGN KEY (districtFileId) REFERENCES Files(fileId)
  )'''

#Create Tables related to States
states = '''
  CREATE TABLE IF NOT EXISTS States(
    stateId VARCHAR(2) PRIMARY KEY,
    stateName VARCHAR(25) NOT NULL,
    stateFIPSCODE INT(2) NOT NULL,
    totalPopulation BIGINT DEFAULT 0
  )'''

enactedPlanFiles = '''
  CREATE TABLE IF NOT EXISTS EnactedPlanFiles(
    enactedPlanId INT,
    enactedPlanFilesId INT,
    FOREIGN KEY (enactedPlanId) REFERENCES EnactedPlans(enactedPlanId),
    FOREIGN KEY (enactedPlanFilesId) REFERENCES PlanFiles(planFileId)
)'''

enactedPlan = '''
  CREATE TABLE IF NOT EXISTS EnactedPlans(
    enactedPlanId INT AUTO_INCREMENT PRIMARY KEY,
    stateId VARCHAR(2),
    planId INT,
    FOREIGN KEY (stateId) REFERENCES States(stateId),
    FOREIGN KEY (planId) REFERENCES Plans(planId)

)'''

stateDemographics = '''
  CREATE TABLE IF NOT EXISTS StateDemographics(
    stateId VARCHAR(2),
    demographicId INT,
    FOREIGN KEY (stateId) REFERENCES States(stateId),
    FOREIGN KEY (demographicId) REFERENCES Demographics(demographicId)
  )'''

# #Create Tables related to Districts
districts = '''
  CREATE TABLE IF NOT EXISTS Districts(
    districtId INT AUTO_INCREMENT PRIMARY KEY,
    districtFIPSCode INT NOT NULL,
    districtNumber INT(2) NOT NULL,
    numberOfCounties INT DEFAULT 0,
    numberOfPrecincts INT DEFAULT 0,
    totalPopulation BIGINT DEFAULT 0,
    stateId VARCHAR(2),
    FOREIGN KEY (stateId) REFERENCES States(stateId)
  )'''

districtDemographics = '''
  CREATE TABLE IF NOT EXISTS DistrictDemographics(
    districtId INT,
    demographicId INT,
    FOREIGN KEY (districtId) REFERENCES Districts(districtId),
    FOREIGN KEY (demographicId) REFERENCES Demographics(demographicId)
  )'''

#Create Tables related to Precincts
precincts = '''
  CREATE TABLE IF NOT EXISTS Precincts(
    precinctId INT AUTO_INCREMENT PRIMARY KEY,
    precinctFIPSCode INT NOT NULL,
    precinctName VARCHAR(255) NOT NULL,
    totalPopulation BIGINT DEFAULT 0,
    countyId INT,
    stateId VARCHAR(2),
    districtId INT,
    FOREIGN KEY (countyId) REFERENCES Counties(countyId),
    FOREIGN KEY (stateId) REFERENCES States(stateId),
    FOREIGN KEY (districtId) REFERENCES Districts(districtId)
  )'''

precinctNeighbors = '''
  CREATE TABLE IF NOT EXISTS PrecinctNeighbors(
    precinctID INT,
    precinctNeighborID INT,
    FOREIGN KEY (precinctID) REFERENCES Precincts(precinctId),
    FOREIGN KEY (precinctNeighborID) REFERENCES Precincts(precinctId)
  )'''

precinctDemographics = '''
  CREATE TABLE IF NOT EXISTS PrecinctDemographics(
    precinctID INT,
    demographicID INT,
    FOREIGN KEY (precinctID) REFERENCES Precincts(precinctId),
    FOREIGN KEY (demographicID) REFERENCES Demographics(demographicID)
  )'''

#Create Tables related to Counties
counties = '''
  CREATE TABLE IF NOT EXISTS Counties(
    countyId INT AUTO_INCREMENT PRIMARY KEY,
    countyFIPSCode INT NOT NULL,
    countyName VARCHAR(255) NOT NULL,
    numberOfPrecincts INT DEFAULT 0, 
    totalPopulation BIGINT DEFAULT 0,
    districtId INT,
    FOREIGN KEY (districtId) REFERENCES Districts(districtId)
  )'''

# Create Tables related to Demographics
demographics = '''
  CREATE TABLE IF NOT EXISTS Demographics(
    demographicId INT AUTO_INCREMENT PRIMARY KEY,
    censusGeneralDemographicId INT, 
    censusVotingAgeDemographicId INT,
    FOREIGN KEY (censusGeneralDemographicId) REFERENCES CensusGeneralDemographics(censusGeneralDemographicId),
    FOREIGN KEY (censusVotingAgeDemographicId) REFERENCES CensusVotingAgeDemographics(censusVotingAgeDemographicId)
  )'''

censusGeneralDemographics = '''
  CREATE TABLE IF NOT EXISTS CensusGeneralDemographics(
    censusGeneralDemographicId INT AUTO_INCREMENT PRIMARY KEY,
    totalPopulation INT DEFAULT 0,
    populationID INT,
    FOREIGN KEY (populationID) REFERENCES Populations(populationId)
  )'''

censusVotingAgeDemographics = '''
  CREATE TABLE IF NOT EXISTS CensusVotingAgeDemographics(
    censusVotingAgeDemographicId INT AUTO_INCREMENT PRIMARY KEY,
    totalPopulation INT DEFAULT 0,
    populationID INT,
    FOREIGN KEY (populationID) REFERENCES Populations(populationId)
  )'''

populations = '''
  CREATE TABLE IF NOT EXISTS Populations(
    populationId INT AUTO_INCREMENT PRIMARY KEY,
    ethnicityName VARCHAR(100),
    population BIGINT DEFAULT 0,
    FOREIGN KEY (ethnicityName) REFERENCES CensusEthnicities(ethnicityName)
  )'''

censusEthnicities = '''
  CREATE TABLE IF NOT EXISTS CensusEthnicities(
    ethnicityName VARCHAR(100) PRIMARY KEY,
    censusName VARCHAR(255) NOT NULL
  )'''


# #Create Tables related to Files
files = '''
  CREATE TABLE IF NOT EXISTS Files(
    fileID INT AUTO_INCREMENT PRIMARY KEY,
    filePath VARCHAR(1000) NOT NULL
  )'''




#Create each table in the database
#If and only the table does not 
#already exist
mycursor.execute(states)
mycursor.execute(districts)
mycursor.execute(counties)
mycursor.execute(precincts)
mycursor.execute(jobs)
mycursor.execute(plans)
mycursor.execute(files)
mycursor.execute(summaries)
mycursor.execute(censusEthnicities)
mycursor.execute(minorityGroups)
mycursor.execute(planGraphs)
mycursor.execute(graphDistricts)
mycursor.execute(jobGraphs)
mycursor.execute(jobPlans)
mycursor.execute(planDistricts)
mycursor.execute(planPrecincts)
mycursor.execute(extremePlans)
mycursor.execute(averagePlans)
mycursor.execute(planFiles)
mycursor.execute(enactedPlan)
mycursor.execute(enactedPlanFiles)
mycursor.execute(precinctNeighbors)
mycursor.execute(precinctNeighbors)
mycursor.execute(populations)
mycursor.execute(censusGeneralDemographics)
mycursor.execute(censusVotingAgeDemographics)
mycursor.execute(demographics)
mycursor.execute(stateDemographics)
mycursor.execute(districtDemographics)
mycursor.execute(precinctDemographics)

print("Successfully Created Tables")

mydb.close()