# ad-adviser
test assignment

## Contents
### Initial conditions based on assignment text and assumptions
### Technical decisions
### Stack
### Components overview
### Some more implementation details
### How to build and run
### How to use the app
### Final word

## Initial conditions based on assignment text and assumptions
-	It’s assumed that all the client needs from the service is a list of ad network SDK provider names for every type of the ad (banner, interstitial, and video – three lists in total), sorted in descending order by the calculated priority. The service must return only names and no other additional information, since the client already has all the required information to work with any ad network or can fetch it from another service
-	priority score ranges from 0 to 99, higher score value means higher priority of the ad network
-	every ad network provides all three possible types of ad (banner, interstitial, and video)
-	multiple score values are calculated for every ad network – for every ad type in every geo location, i.e.: 
Total score for one network = ad type * number of locations, where ad type = 3
So total number of scores calculated by the pipeline is:
total number of scores = 3 * number of locations * number of ad network SDK provider
-	In current implementation geo locations are restricted to countries with notion of region (Africa, Asia, Europe etc.)
-	every time batch job is executed, it calculates all the scores for all the networks.
-	The service must return data of the most recent calculation result
-	Request to the service has following list of contextual and environmental parameters:
o	Country name
o	Operating system name
o	Operating system version
o	Application name
o	Age restriction for using the application
- There should be a possibility to configure restrictions based on the above parameters and filter out ad networks for which these restrictions configured.
-	There should be a possibility to configure “conflicting” (mutually exclusive) ad networks. Only the one of them with highest score should be returned to the client
-	There should be a possibility to configure a fallback, “last resort” set of ad networks for every geo location and ad type. It should be returned if no data found based on request data (i.e. not calculated at all for the country or filtered out based on other request data). No additional filtering is performed on a fallback data. The client should be notified that he receives non optimal set of data.
-	Service is under high load and must be horizontally scalable
-	Secure access to the service is not a concern of this assignment
- since there could be many instances of the app running up, there should be a convinient mechanism for updating data and configuration at runtime for all instances at the same time in order to avoid inconsistency in data and configuration
- data retrieved from the database (calculated and stored as the result of a batch job) is not needed to be check for consistency.

## Technical decisions
o	In order to leverage cloud platforms and make horizontal scaling easy, it was chosen to use microservice architecture. So component responsible for updating ad network data and component responsible to serve this data are splitted on two microservices.
o	Country selected as a geo location unit. According to various sources, at this moment there are up to 233 countries in the world.
[From top-10 ad networks just 4 has more than 6% integration, 10th has only two](https://www.statista.com/statistics/1035623/leading-mobile-app-ad-network-sdks-android/).
This means it’s unlikely that potential clients of the service will use more than 10-15 ad network SDKs.
Assuming, that scores are calculated for every ad network for every ad type and every location, the total number of calculated scores per batch job will be:
	`numberOfCountries * numberOfAdTypes * numberOfAdNetworksInUse== 233 * 3 * 15 = 10485 entries`
It means we can store all these lightweight entries in memory and avoid DB querying.

Even if it was decided to use some other, smaller unit and the world is now divided on 500 zones, we have to support 100 instead of 15 ad networks in addition:
		`500*3*100 = 150 000`
Despite amount of the entries has grown significantly, again, it is still reasonable number of entries to keep in cache.

o	Cache always have all the required data at once. On cache update all the old data discarded and new data uploaded. Cache populated on application startup, and after that once per day on receiving notification from the batch job processor. Hence we don’t need features provided by caching frameworks such as eviction or automatic update. Since the score is defined by two keys – country and ad type – cache can be implemented based on nested maps:
Map<Country, Map<AdType, List<AdNetwork>>
Since countries and ad types are predefined constants, we can make enums from them. This allows us to use highly performant and compact implementation of Map interface – [EnumMap](https://docs.oracle.com/javase/7/docs/api/java/util/EnumMap.html)

o	Scores are sorted in descending order via ORDER BY condition SQL select query. No more sorting ever required at runtime.

## Stack
Technical implementation is build upon Spring Boot (Web, Cloud)
o language - Java 8
o DB - in-memory H2 database
o messaging - activeMQ
o web-service documentation - Swagger
o build tool - Maven

## Components overview
Final solution consist of four components:
o ad-sdk-adviser
o ad-sdk-info-updater
o config-server
o database-mock-service

### ad-sdk-adviser
This service does the actual job serving ad networks data to the client applications. For better performance it stores all the data received as a result of the most recent batch job in memory. 
When request comes, it finds appropriate data, filters out entries, which dont't pass configurated restrictions and send it to the caller.
Restrictions can be configured and updated at runtime without a need to restart an app. These restrictions are kept in `.properties` file as lists and key-value pairs.
I would implement it other way for the real system (I would at least store them in DB and programm some more reliable configutration update mechanism)
Configurable restrictions are:
`ad.network.restriction.banned-in-country` -if the network is not allowed to be used in a particular country.<br/>
`ad.network.restriction.os` - if network is not working with some OS<br/>
`ad.network.restriction.os-version` - if network not works for particular version of OS (no space between OS and version)<br/>
`ad.network.restriction.conflict`- conflicting networks. Of one is present in the list, the others should go away. The highest rated one wins.<br/>
`ad.network.restriction.age` - to check if the network cannot be used with apps which has a particular age restrictions<br/>
`ad.network.restriction.app` - restriction based on the name of the application<br/>
these properties are stored in `adviser.properties`
If no data found for a particular country, then app falls to fallback list of the networks which is configured also configured in `adviser.properties` for every geo region (not country). For example: `ad.network.fallback.africa`, `ad.network.fallback.asia` and so on
<br/>These values (restrictions and fallback lists) can be updated at runtime.

### ad-sdk-info-updater
This service emulates batch job processor which collects all the data from providers, analyzes it and saves to the DB.<br/>
It's also 'hosts' embedded jms-brocker, which I decided not to make standalone service in order to not overcomplicate the solution, for easier build and deploy of the app <br/>
"Batch job" process can be triggered via call to REST service, it generates random test data, stores it in the table and notifies all the running instances of ad-sdk-adviser via "topic->subscriber" mechanism

### config-server
manages configurations of all applications. As a storage uses git repository.

### database-mock-service
has no business logic, just runs H2 in-memory database, which is being populated with a small set of predefined data.

## Some more implementation details
there are just two tables in the database - one for storing the data, and the other is for different instances of the application to register themself in the cluster. The latter seems and implemented pretty awkward, due to known restrictions, but it still allows instances of the app to communicate with eachother.<br/>
For simplicity, such data as, for example, date-time of completed batch job and reference for ad networks names, country codes etc are not stored in the DB.

## How to build and run
Modules are compiled and packed to executable jar.
To build module from the source code the one should execute maven command (`mvn package` or `mvn package -DskipTests`)
You can also download it as a binary [here](https://drive.google.com/file/d/14eB57-J3DwifYcyKx0mA2sZA1FMXQWSH/view?usp=sharing) <br/>
**Important!** Order of start is the following:<br/>
1. config-server (running on port 8888
2. database-mock-service (port 8889)
3. ad-sdk-info-updater (port 8887)
4. ad-sdk-adviser (port 8180)
To run multiple instances of ad-sdk-adviser the one should use other ports (8280, 8380 and so on)
The following ports are also in use - `9090` (database) and '61616' (messaging broker)

to start the application use the following command `java -jar <artifact-name>.jar` <br/>
properties, such as ports, can be overriden with using of `--`, for example:<br/>
`java -jar ad-sdk-adviser-demo.jar --server.port=8280`
<br/>
config server serves property files form [this repo](https://github.com/butenkos/test). You can change the the repo URI in the config server `.properies` file or just override it the same way as with server port using `--spring.cloud.config.server.git.uri=<your_location>`

## How to use the app
Assuming you're using default ports<br/>
h2 console is accessible via url http://localhost:8889/h2-console/ (username=sa, password=password)<br>
ad-sdk-info-updater and ad-sdk-adviser has a built-in autogenerated Swagger UI. Here you can see all the endpoints and easily test them.<br>
ui of ad-sdk-adviser can be reached this link via link http://localhost:8180/swagger-ui.html <br/>
There you can find all endpoints and brief description of them. There you can also execute every request (click endpoint -> "Try it out" -> fill required data -> "Execute") <br/>
Exposed services allow:
- to "advise" which networks to use by request data
- update the cache with the most recent data - updates all running instances
- update the cache with data of specific batch job (by its id) - updates all running instances
- reload constraints and fallback configuration defined in `.properties` file after it was changed and pushed to the repository - updates all running instances
- display brief statistics of the data in cache (number of ad network entries by country and ad type, batch job id, entry count)
- print out the cache contents

ui of ad-sdk-info-updater can be reached this link via link http://localhost:8887/swagger-ui.html <br/>
It exposes a single endpoint, which allows to simulate a batch job and populate the database with the new random test data and notify all running instances of ad-sdk-adviser in order to reload their caches.

**NOTE**<br/>
For simplicity, just small set of countries was used in the app. Please refer to the following list (use 3-character code in your requests):<br/>
  ANGOLA("AGO", AFRICA),
  ALGERIA("DZA", AFRICA),
  MOROCCO("MAR", AFRICA),
  NIGERIA("NGA", AFRICA),
  SAR("ZAF", AFRICA),
  BRAZIL("BRA", SOUTH_AMERICA),
  ARGENTINA("ARG", SOUTH_AMERICA),
  URUGUAY("URY", SOUTH_AMERICA),
  CHILE("CHL", SOUTH_AMERICA),
  ECUADOR("ECU", SOUTH_AMERICA),
  USA("USA", NORTH_AMERICA),
  CANADA("CAN", NORTH_AMERICA),
  JAPAN("JPN", ASIA),
  CHINA("CHN", ASIA),
  ITALY("ITA", EUROPE),
  SPAIN("ESP", EUROPE),
  ENGLAND("ENG", EUROPE),
  RUSSIA("RUS", EUROPE),
  SLOVENIA("SVN", EUROPE),
  AUSTRIA("AUT", EUROPE),
  GERMANY("DEU", EUROPE),
  FRANCE("FRA", EUROPE),
  SCOTLAND("SCO", EUROPE),
  IRELAND("IRL", EUROPE),
  AUSTRALIA("AUS", OCEANIA),
  NEW_ZEALAND("NZL", OCEANIA);

## Final word
Thanks a lot, it was fun :)
If you have any questions, please contact me.
