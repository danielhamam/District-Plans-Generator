import mysql.connector
import json

mydb = mysql.connector.connect(
  host="mysql3.cs.stonybrook.edu",
  user="kadiallo",
  password="110331331", 
  database="kadiallo"
)

def main():
    if mydb:
        print("connection successful")
    else:
        print("connection unsuccessful")
        exit(0)

    mycursor = mydb.cursor()

    f = open('backend/database/sqlFormat.json')
    data = json.load(f)

    # deleteTableRows(mycursor)
    writeToCensusEthnicityTable(mycursor)
    # writeToStateTable(mycursor)
    # writeToDistrictTable(data, mycursor)
    # writeToCountyTable(data, mycursor)
    # writeToPrecinctTable(data, mycursor)
    # writePrecinctDemographic(data, mycursor)
    # writeCountyDemographic(data, mycursor)
    # writeDistrictDemographic(data, mycursor)
    # writeStateDemographic(mycursor)
    # writePrecinctNeighbors(data, mycursor)

def writeToStateTable(mycursor):

    states = {
        'state0':{
            'stateId': 'MD',
            'stateName': 'Maryland',
            'stateFIPSCode': 24,
        },
        'state1':{
            'stateId': 'PA',
            'stateName': 'Pennsylvania',
            'stateFIPSCode': 42,
           
        },
        'state2':{
            'stateId': 'GA',
            'stateName': 'Georgia',
            'stateFIPSCode': 13,
            
        },
    }

    for state in states:
      sql = "INSERT INTO States (stateId, stateName, stateFIPSCode) VALUES (%s, %s, %s)"
      val = (states[state]['stateId'], states[state]['stateName'], states[state]['stateFIPSCode'])
      mycursor.execute(sql, val)
   
      mydb.commit()

def writeToDistrictTable(data, mycursor):
    
    for district in data:
        p = data[district]
        sql = "INSERT INTO Districts (districtNumber, stateId) VALUES (%s, %s)"
        val = (p['districtNumber'], p['stateId'])

        mycursor.execute(sql, val)
        # mydb.commit()

def writeToCountyTable(data, mycursor):

    for countyDistrict in data:
        
        d = data[countyDistrict]

        #Query District Id
        sql = "SELECT districtId FROM Districts WHERE districtNumber = (%s) and stateId = (%s)"
        val = (d['districtFIPS'], d['stateId'])
        mycursor.execute(sql, val)
        districtId = mycursor.fetchone()

        if districtId is None: continue

        districtId = districtId[0]
         
        #Insert into Table
        sql = "INSERT INTO Counties (countyFIPSCode, countyName, districtId, stateId) VALUES (%s,%s,%s,%s)"
        val = (d['countyFIPS'], d['countyName'], districtId, d['stateId'])
        mycursor.execute(sql, val)
        mydb.commit()

def writeToPrecinctTable(data, mycursor):
   
    for precinct in data:

        p = data[precinct]

        #Query District Id
        sql = "SELECT districtId FROM Districts WHERE districtNumber = (%s) and stateId = (%s)"
        val = (p['districtNumber'], p['stateId'])
        mycursor.execute(sql, val)
        districtId = mycursor.fetchone()

        if districtId is None: continue

        districtId = districtId[0]

        #Query County Id
        sql = "SELECT countyId FROM Counties WHERE countyFIPSCode = (%s) and stateId = (%s)"
        val = (p['countyFIPSCode'], p['stateId'])
        mycursor.execute(sql, val)
        countyId = mycursor.fetchone()

        if countyId is None: continue

        countyId = countyId[0]

        #Insert into Precinct table
        sql = "INSERT INTO Precincts (precinctFIPSCode, precinctName, countyId, districtId, stateId) VALUES (%s, %s,%s, %s,%s)"
        val = (p['precinctFIPSCode'],p['precinctName'], countyId, districtId, p['stateId'])
        mycursor.execute(sql, val)

        # mydb.commit()

def writeToCensusEthnicityTable(mycursor):

    censusEthnicities = {
        "White": {"shortenName": "White", "fullName" : "White"},
        "Black": {"shortenName": "Black", "fullName" : "Black or African American"},
        "Asian": {"shortenName": "Asian", "fullName": "Asian"},
        "AmericanIndian": {"shortenName" : "American Indian", "fullName": "American Indian or Alaska Native"},
        "Latino": {"shortenName": "Latino", "fullName": "Hispanic or Latino"},
        "Native Hawaiian": {"shortenName": "Native Hawaiian", "fullName": "Native Hawaiian or Other Pacific Islander"},
        "Other": {"shortenName": "Other", "fullName": "Other race, Non-Hispanic"},
        "Multiple": {"shortenName": "Multiple", "fullName": "Two or more race, Non-Hispanic"}
    }

    for ethnicity in censusEthnicities:
        sql = "INSERT INTO CensusEthnicities (shortenName, censusName) VALUES (%s,%s)"
        val = (censusEthnicities[ethnicity]['shortenName'], censusEthnicities[ethnicity]['fullName'])
        mycursor.execute(sql, val)
        # mydb.commit()

def writePrecinctDemographic(data, mycursor):

    stateAbbrev = data[list(data)[0]]['stateId']
    statePrecincts = getStatePrecincts()
 
    for precinct in statePrecincts:
        
        getDemographic = findCorrespondingKey(data, precinct)

        if getDemographic != None:
    
            # Query Precinct Id
            sql = "SELECT precinctId FROM Precincts WHERE precinctFIPSCode = (%s) and stateId = (%s)"
            val = (getDemographic['precinct'], getDemographic['stateId'])
            mycursor.execute(sql, val)
            precinctId = mycursor.fetchone()
       
            if precinctId is None: continue

            precinctId = precinctId[0]
            
            #Insert into Table
            sql = "INSERT INTO Demographics (total, white, hispanic, americanIndian, nativeHawaiian, black, asian, otherRace, multipleRace, totalVAP, whiteVAP, hispanicVAP, americanIndianVAP, nativeHawaiianVAP, blackVAP, asianVAP, otherRaceVAP, multipleRaceVAP, precinctId) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"
            val = (getDemographic['total'], getDemographic['white'], getDemographic['hispanic'], getDemographic['american_indian'], getDemographic['native_hawaiian'], getDemographic['black'], getDemographic['asian'], getDemographic['other'], getDemographic['multiple'], getDemographic['vap'], getDemographic['white_vap'], getDemographic['hispanic_vap'], getDemographic['american_indian_vap'], getDemographic['native_hawaiian_vap'], getDemographic['black_vap'], getDemographic['asian_vap'], getDemographic['other_vap'], getDemographic['multiple_vap'], precinctId)
            
            mycursor.execute(sql, val)

            # mydb.commit()

        else:
           
            # Query Precinct Id
            sql = "SELECT precinctId FROM Precincts WHERE precinctFIPSCode = (%s) and stateId = (%s)"
            val = (precinct, stateAbbrev)
            mycursor.execute(sql, val)
            precinctId = mycursor.fetchone()
       
            if precinctId is None: continue

            precinctId = precinctId[0]

            #Insert into Table
            sql = "INSERT  Demographics (total, white, hispanic, americanIndian, nativeHawaiian, black, asian, otherRace, multipleRace, totalVAP, whiteVAP, hispanicVAP, americanIndianVAP, nativeHawaiianVAP, blackVAP, asianVAP, otherRaceVAP, multipleRaceVAP, precinctId) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"
            val = (0,0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, precinctId)

            mycursor.execute(sql, val)
           
            # mydb.commit()

def writeCountyDemographic(data, mycursor):

    for county in data:
        d = data[county]

        #Query County Id
        sql = "SELECT countyId FROM Counties WHERE countyFIPSCode = (%s) and stateId = (%s)"
        val = (d['countyFIPS'], d['stateId'])
        mycursor.execute(sql, val)
        countyId = mycursor.fetchone()

        if countyId == None: continue
        countyId = countyId[0]

        #Count the number of precincts that belong to this county
        sql = "SELECT COUNT(*) FROM Precincts WHERE countyId = (%s) and stateId = (%s)"
        val = (countyId,  d['stateId'])
        mycursor.execute(sql, val)
        numPrecincts = mycursor.fetchone()[0]

        #Update the number of precinct field of the county
        sql = "UPDATE Counties SET numberOfPrecincts = (%s) WHERE countyId = (%s)"
        val = (numPrecincts, countyId)
        mycursor.execute(sql, val)
        # mydb.commit()
       
        #get array of precincts belonging to this county
        sql = "SELECT precinctId FROM Precincts WHERE countyId = (%s) and stateId = (%s)"
        val = (countyId,  d['stateId'])
        mycursor.execute(sql, val)
        precincts = mycursor.fetchall()

        #Sum the population values of precincts that belong to this county
        computeSumDemographic(precincts,  mycursor, "county", countyId)

def writeDistrictDemographic(data, mycursor):
    
    for district in data:
        
        d = data[district]

        #Query District Id
        sql = "SELECT districtId FROM Districts WHERE districtNumber = (%s) and stateId = (%s)"
        val = (d['districtNumber'], d['stateId'])
        mycursor.execute(sql, val)
        districtId = mycursor.fetchone()

        if districtId == None: continue
        districtId = districtId[0]

        #Count the number of counties that belong to this district
        sql = "SELECT COUNT(*) FROM Counties WHERE districtId = (%s) and stateId = (%s)"
        val = (districtId,  d['stateId'])
        mycursor.execute(sql, val)
        numCounties = mycursor.fetchone()[0]

        #Count the number of precincts that belong to this district
        sql = "SELECT COUNT(*) FROM Precincts WHERE districtId = (%s) and stateId = (%s)"
        val = (districtId,  d['stateId'])
        mycursor.execute(sql, val)
        numPrecincts = mycursor.fetchone()[0]

        #Update the number of precincts field of the district
        sql = "UPDATE Districts SET numberOfPrecincts = (%s) WHERE districtId = (%s)"
        val = (numPrecincts, districtId)
        mycursor.execute(sql, val)
        # mydb.commit()
       
        #Update the number of counties field of the district
        sql = "UPDATE Districts SET numberOfCounties = (%s) WHERE districtId = (%s)"
        val = (numCounties, districtId)
        mycursor.execute(sql, val)
        # mydb.commit()

        # get array of counties belonging to this district
        sql = "SELECT countyId FROM Counties WHERE districtId = (%s) and stateId = (%s)"
        val = (districtId,  d['stateId'])
        mycursor.execute(sql, val)
        counties = mycursor.fetchall()
       
        #Sum the population values of counties that belong to this district
        computeSumDemographic(counties,  mycursor, "district", districtId)

def writeStateDemographic(mycursor):
    states = {
        'state0':{
            'stateId': 'MD',
            'stateName': 'Maryland',
            'stateFIPSCode': 24,
        },
        'state1':{
            'stateId': 'PA',
            'stateName': 'Pennsylvania',
            'stateFIPSCode': 42,
           
        },
        'state2':{
            'stateId': 'GA',
            'stateName': 'Georgia',
            'stateFIPSCode': 13,
            
        },
    }

    for state in states:
        
        #Count the number of districts that belong to this state
        sql = "SELECT COUNT(*) FROM Districts WHERE stateId = (%s)"
        val = (states[state]['stateId'])
        mycursor.execute(sql, (val,))
        numDistrict = mycursor.fetchone()[0]

        #Count the number of precincts that belong to this district
        sql = "SELECT COUNT(*) FROM Precincts WHERE stateId = (%s)"
        val = (states[state]['stateId'])
        mycursor.execute(sql, (val,))
        numPrecincts = mycursor.fetchone()[0]

        #Count the number of counties that belong to this district
        sql = "SELECT COUNT(*) FROM Counties WHERE stateId = (%s)"
        val = (states[state]['stateId'])
        mycursor.execute(sql, (val,))
        numCounties = mycursor.fetchone()[0]

        #Update the number of precincts field of the state
        sql = "UPDATE States SET numberOfPrecincts = (%s) WHERE stateId = (%s)"
        val = (numPrecincts, states[state]['stateId'])
        mycursor.execute(sql, val)
        # mydb.commit()
       
        #Update the number of counties field of the state
        sql = "UPDATE States SET numberOfDistricts = (%s) WHERE stateId = (%s)"
        val = (numDistrict, states[state]['stateId'])
        mycursor.execute(sql, val)
        # mydb.commit()

        #Update the number of counties field of the state
        sql = "UPDATE States SET numberOfCounties = (%s) WHERE stateId = (%s)"
        val = (numCounties, states[state]['stateId'])
        mycursor.execute(sql, val)
        # mydb.commit()


        # get array of district belonging to this state
        sql = "SELECT districtId FROM Districts WHERE stateId = (%s)"
        val = (states[state]['stateId'])
        mycursor.execute(sql, (val,))
        districts = mycursor.fetchall()

        #Sum the population values of counties that belong to this district
        computeSumDemographic(districts,  mycursor, "state", states[state]['stateId'])

def getStatePrecincts():
    f = open('backend/database/sqlFormat2.json')
    data = json.load(f)

    d = []

    for precinct in data:
        p = data[precinct]
        d.append(p['precinctFIPSCode'])
    
    return d
            
def deleteTableRows(mycursor):
 
    sql = "DELETE FROM PrecinctNeighbors"
    mycursor.execute(sql)
    #mydb.commit()

def findCorrespondingKey(dictionary, value):
    
    for key in dictionary:
        d = dictionary[key]

        if(d['precinct'] == value): return d

    return None

def computeSumDemographic(array,  mycursor, type, id):
    
    total = 0
    white = 0
    hispanic = 0
    americanIndian = 0
    nativeHawaiian = 0
    black = 0
    asian = 0
    other = 0
    multiple = 0
    totalVAP = 0
    whiteVAP = 0
    hispanicVAP = 0
    americanIndianVAP = 0
    nativeHawaiianVAP = 0
    blackVAP = 0
    asianVAP = 0
    otherVAP = 0
    multipleVAP = 0

    if(type == "county"):

        for x in array:
            x = x[0]

            sql = "SELECT * FROM Demographics WHERE precinctId = (%s)"
        
            mycursor.execute(sql, (x,))
            demographic = mycursor.fetchone()

            total += demographic[1]
            white += demographic[2]
            hispanic += demographic[3]
            americanIndian += demographic[4]
            nativeHawaiian += demographic[5]
            black += demographic[6]
            asian += demographic[7]
            other += demographic[8]
            multiple += demographic[9]
            totalVAP += demographic[10]
            whiteVAP += demographic[11]
            hispanicVAP += demographic[12]
            americanIndianVAP += demographic[13]
            nativeHawaiianVAP += demographic[14]
            blackVAP += demographic[15]
            asianVAP += demographic[16]
            otherVAP += demographic[17]
            multipleVAP += demographic[18]


        #Insert into Table
        sql = "INSERT INTO Demographics (total, white, hispanic, americanIndian, nativeHawaiian, black, asian, otherRace, multipleRace, totalVAP, whiteVAP, hispanicVAP, americanIndianVAP, nativeHawaiianVAP, blackVAP, asianVAP, otherRaceVAP, multipleRaceVAP, countyId) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"
        val = (total, white, hispanic, americanIndian, nativeHawaiian, black, asian, other, multiple, totalVAP, whiteVAP, hispanicVAP, americanIndianVAP, nativeHawaiianVAP, blackVAP, asianVAP, otherVAP, multipleVAP, id)
        mycursor.execute(sql, val)

        # mydb.commit()

    elif(type == "district"):

        for x in array:
            x = x[0]

            sql = "SELECT * FROM Demographics WHERE countyId = (%s)"
            mycursor.execute(sql, (x,))
            demographic = mycursor.fetchone()

            total += demographic[1]
            white += demographic[2]
            hispanic += demographic[3]
            americanIndian += demographic[4]
            nativeHawaiian += demographic[5]
            black += demographic[6]
            asian += demographic[7]
            other += demographic[8]
            multiple += demographic[9]
            totalVAP += demographic[10]
            whiteVAP += demographic[11]
            hispanicVAP += demographic[12]
            americanIndianVAP += demographic[13]
            nativeHawaiianVAP += demographic[14]
            blackVAP += demographic[15]
            asianVAP += demographic[16]
            otherVAP += demographic[17]
            multipleVAP += demographic[18]


        #Insert into Table
        sql = "INSERT INTO Demographics (total, white, hispanic, americanIndian, nativeHawaiian, black, asian, otherRace, multipleRace, totalVAP, whiteVAP, hispanicVAP, americanIndianVAP, nativeHawaiianVAP, blackVAP, asianVAP, otherRaceVAP, multipleRaceVAP, districtId) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"
        val = (total, white, hispanic, americanIndian, nativeHawaiian, black, asian, other, multiple, totalVAP, whiteVAP, hispanicVAP, americanIndianVAP, nativeHawaiianVAP, blackVAP, asianVAP, otherVAP, multipleVAP, id)
        mycursor.execute(sql, val)

        # mydb.commit()

    elif(type == "state"):
    
        for x in array:
            x = x[0]

            sql = "SELECT * FROM Demographics WHERE districtId = (%s)"
            mycursor.execute(sql, (x,))
            demographic = mycursor.fetchone()

            total += demographic[1]
            white += demographic[2]
            hispanic += demographic[3]
            americanIndian += demographic[4]
            nativeHawaiian += demographic[5]
            black += demographic[6]
            asian += demographic[7]
            other += demographic[8]
            multiple += demographic[9]
            totalVAP += demographic[10]
            whiteVAP += demographic[11]
            hispanicVAP += demographic[12]
            americanIndianVAP += demographic[13]
            nativeHawaiianVAP += demographic[14]
            blackVAP += demographic[15]
            asianVAP += demographic[16]
            otherVAP += demographic[17]
            multipleVAP += demographic[18]


        #Insert into Table
        sql = "INSERT INTO Demographics (total, white, hispanic, americanIndian, nativeHawaiian, black, asian, otherRace, multipleRace, totalVAP, whiteVAP, hispanicVAP, americanIndianVAP, nativeHawaiianVAP, blackVAP, asianVAP, otherRaceVAP, multipleRaceVAP, stateId) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"
        val = (total, white, hispanic, americanIndian, nativeHawaiian, black, asian, other, multiple, totalVAP, whiteVAP, hispanicVAP, americanIndianVAP, nativeHawaiianVAP, blackVAP, asianVAP, otherVAP, multipleVAP, id)
        mycursor.execute(sql, val)

        # mydb.commit()

def writePrecinctNeighbors(data, mycursor):

    for precinct in data:
        currentPrecinct = precinct

        #Query Precinct Id
        sql = "SELECT precinctId FROM Precincts WHERE precinctFIPSCode = (%s)"
        mycursor.execute(sql, (currentPrecinct,))
        currentPrecinctId = mycursor.fetchone()

        if currentPrecinctId == None: continue

        currentPrecinctId = currentPrecinctId[0]

        #Query the Precinct Id of each neighbor precinct
        for neighbor in data[precinct]:

            #Query Precinct Id
            sql = "SELECT precinctId FROM Precincts WHERE precinctFIPSCode = (%s)"
            mycursor.execute(sql, (neighbor,))
            neighborPrecinctId = mycursor.fetchone()

            if neighborPrecinctId == None: continue

            neighborPrecinctId = neighborPrecinctId[0]

            #Insert the pairs into Table
            sql = "INSERT INTO PrecinctNeighbors (precinctID, precinctNeighborID) VALUES (%s,%s)"
            val = (currentPrecinctId, neighborPrecinctId)
            mycursor.execute(sql, val)
            # mydb.commit()


if __name__ == '__main__':
    main()