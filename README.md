# pricing-kaizen
Pricing engine which<br>
1. Listens to all the relevant signal changes (mrp, seller price, competitor price, etc) and persists them.<br>
2. Uses the signals to compute an optimal price for each listing.<br>
3. Applies subsidies to compute the fsp (flipkart_selling_price).<br>
4. Propagates those values realiably to multiple systems. (Fatak, discovery, etc)<br>

##Setup
Setup requires Kafka server & MySQL server running on your system.

MySQL setup<br>
1. If you have a mac, then get the installation from a peer. Make sure you give a blank password for root while installing mysql.<br>
2. Run ./bootstrap.sh to create the bare DB.<br>

Kafka Setup<br>
1. The test suite requires a kafka server running on your system. You can use kafka 2.11-0.8.2.0. See instructions on website on how to start the server.<br>
2. Once the kafka server is up, create all the topics that you see in `config/server_config/dev/pricing_kaizen.yaml` under the heading kaizen -> kafka.<br>

To run the build, enter
mvn clean install -DskipTests

To run migrations,
cd service/ && mvn liquibase:update (You would have to run liquibase:update each time a new migration is written)

To run tests
mvn clean install

To run the server
cd service
mvn exec:java
