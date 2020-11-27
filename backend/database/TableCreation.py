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

# summaries = '''
#   CREATE TABLE IF NOT EXISTS JobSummaries(
#     summaryId INT AUTO_INCREMENT PRIMARY KEY,
#     summaryFileId INT,
#     jobID INT, 
#     FOREIGN KEY (jobID) REFERENCES Jobs(jobId),
#     FOREIGN KEY (summaryFileId) REFERENCES Files(fileID)
#   )'''

minorityGroups = '''
  CREATE TABLE IF NOT EXISTS JobMinorityGroups(
    censusEthnicityId VARCHAR(100),
    jobID INT,
    FOREIGN KEY (censusEthnicityId) REFERENCES CensusEthnicities(shortenName),
    FOREIGN KEY (jobID) REFERENCES Jobs(jobId) 
  )'''

#Create Tables related to Job plans
plans = '''
  CREATE TABLE IF NOT EXISTS Plans(
    planId INT AUTO_INCREMENT PRIMARY KEY,
    stateId VARCHAR(2) NOT NULL,
    averageDistrictPopulation BIGINT DEFAULT 0,
    averageDistrictCompactness INT DEFAULT 0,
    numberOfDistricts INT DEFAULT 0,
    jobId INT,
    FOREIGN KEY (jobId) REFERENCES Jobs(jobId) 
  )'''


#Create Tables related to States
states = '''
  CREATE TABLE IF NOT EXISTS States(
    stateId VARCHAR(2) PRIMARY KEY,
    stateName VARCHAR(25) NOT NULL,
    stateFIPSCode INT NOT NULL
  )'''


#Create Tables related to Districts
districts = '''
 CREATE TABLE IF NOT EXISTS Districts(
   districtId INT AUTO_INCREMENT PRIMARY KEY,
   districtNumber INT NOT NULL,
   numberOfCounties INT DEFAULT 0,
   numberOfPrecincts INT DEFAULT 0,
   stateId VARCHAR(2),
   FOREIGN KEY (stateId) REFERENCES States(stateId)
 )'''

#Create Tables related to Precincts
precincts = '''
 CREATE TABLE IF NOT EXISTS Precincts(
   precinctId INT AUTO_INCREMENT PRIMARY KEY,
   precinctFIPSCode VARCHAR(25) NOT NULL,
   precinctName VARCHAR(255) NOT NULL,
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

#Create Tables related to Counties
counties = '''
 CREATE TABLE IF NOT EXISTS Counties(
   countyId INT AUTO_INCREMENT PRIMARY KEY,
   countyFIPSCode INT NOT NULL,
   countyName VARCHAR(255) NOT NULL,
   numberOfPrecincts INT DEFAULT 0,
   districtId INT,
   stateId VARCHAR(2),
   FOREIGN KEY (stateId) REFERENCES States(stateId),
   FOREIGN KEY (districtId) REFERENCES Districts(districtId)
 )'''



# Create Tables related to Demographics
demographics = '''
  CREATE TABLE IF NOT EXISTS Demographics(
    demographicId INT AUTO_INCREMENT PRIMARY KEY,
    totalPopulation BIGINT DEFAULT 0,
    whitePopulation BIGINT DEFAULT 0,
    hispanicPopulation BIGINT DEFAULT 0,
    americanIndianPopulation BIGINT DEFAULT 0,
    nativeHawaiianPopulation BIGINT DEFAULT 0,
    africanAmericanPopulation BIGINT DEFAULT 0,
    asianPopulation BIGINT DEFAULT 0,
    otherRacePopulation BIGINT DEFAULT 0,
    multipleRacePopulation BIGINT DEFAULT 0,
    totalVAPPopulation BIGINT DEFAULT 0,
    whiteVAPPopulation BIGINT DEFAULT 0,
    hispanicVAPPopulation BIGINT DEFAULT 0,
    americanIndianVAPPopulation BIGINT DEFAULT 0,
    nativeHawaiianVAPPopulation BIGINT DEFAULT 0,
    africanAmericanVAPPopulation BIGINT DEFAULT 0,
    asianVAPPopulation BIGINT DEFAULT 0,
    otherRaceVAPPopulation BIGINT DEFAULT 0,
    multipleRaceVAPPopulation BIGINT DEFAULT 0,
    districtId INT,
    stateId VARCHAR(2),
    countyId INT,
    precinctId INT,
    FOREIGN KEY (precinctId) REFERENCES Precincts(precinctId),
    FOREIGN KEY (countyId) REFERENCES Counties(countyId),
    FOREIGN KEY (stateId) REFERENCES States(stateId),
    FOREIGN KEY (districtId) REFERENCES Districts(districtId)
 )'''

censusEthnicities = '''
  CREATE TABLE IF NOT EXISTS CensusEthnicities(
    shortenName VARCHAR(100) PRIMARY KEY,
    censusName VARCHAR(255) NOT NULL
  )'''

#Create Tables related to Box Whisker
boxWhiskers = '''
 CREATE TABLE IF NOT EXISTS BoxWhiskers(
   boxWhiskerId INT AUTO_INCREMENT PRIMARY KEY,
   jobId INT,
   FOREIGN KEY (jobId) REFERENCES Jobs(jobId)
 )'''

boxWhiskerPlots = '''
 CREATE TABLE IF NOT EXISTS BoxWhiskerPlots(
   boxWhiskerPlotId INT AUTO_INCREMENT PRIMARY KEY,
   minimum INT DEFAULT 0,
   lowerQuartile INT DEFAULT 0,
   median INT DEFAULT 0,
   upperQuartile INT DEFAULT 0,
   maximum INT DEFAULT 0,
   boxWhiskerId INT,
   FOREIGN KEY (boxWhiskerId) REFERENCES BoxWhiskers(boxWhiskerId)
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
mycursor.execute(censusEthnicities)
mycursor.execute(minorityGroups)
mycursor.execute(precinctNeighbors)
mycursor.execute(demographics)
mycursor.execute(boxWhiskers)
mycursor.execute(boxWhiskerPlots)



print("Successfully Created Tables")

mydb.close()