# Building
The application use JDK16  

To launch tests:
```
./mvn clean test
```

To package application:
```
./mvn clean package
```

To run application:
```
java -jar target/avaloq-task-1.0.jar
```

# Design

The application use a typical 3 layer architecture, controller, service, repository.
To save the different simulation for later lookup I used in memory db H2 to simplify the development,
every new simulation is saved in db. Because there weren't any requirement for scaling and performance every operation is a blocking operation, simplifying the development but in case of high amount of clients and number of simulation a non-blocking-io solution would be better.
It is assumed the number of dice and side per dice is not high otherwise every simulation require a high amount of cpu time. This time came be improved with a parallel calculation of the simulation. Every roll can be parallelized to a different thread or different machine. 

### Endpoint
#### /simulation
```
curl "localhost:8080/simulation?diceSides=6&totalRolls=1&numberDice=3"
```
Run a simulation with 3 dices, 6 side per dice, 1 roll
#### parameter
diceSides >= 4  
totalRolls >= 1  
numberDice >= 1

Return json:
```
[{"sumRolls":{int},"frequency":{int}}]
```
sumRolls is the sum of value of the face of dice launched of 3 times (e.g 5-5-2)   
frequency is how many times sumRolls happened during the simulation

#### /getSimulationsAndRolls
```
curl "localhost:8080/getSimulationsAndRolls"
```
Get the total number of simulations and rolls for every combination number of dice-sides of dice

#### /getDistribution
```
curl "localhost:8080/getDistribution?numberDice=3&diceSides=6"
```
Return: 
```
{"numberDice":{int},"diceSides":{int},"totalRolls":{int},"distributionRecord":[{"sumSimulation":{int},"distribution":{double}]}
```