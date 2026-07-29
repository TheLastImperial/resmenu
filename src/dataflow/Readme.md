## Docker compose to Data Flow.

wget -O docker-compose.yml https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/main/src/docker-compose/docker-compose.yml;
wget -O docker-compose-rabbitmq.yml https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/main/src/docker-compose/docker-compose-rabbitmq.yml;
wget -O docker-compose-postgres.yml https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/main/src/docker-compose/docker-compose-postgres.yml;


## Command to start project with Data Flow
```bash
export HOST_MOUNT_PATH=./src/dataflow/apps
export DATAFLOW_VERSION=2.11.5-jdk17
export SKIPPER_VERSION=2.11.5-jdk17
docker compose -f docker-compose.yml -f src/dataflow/docker-compose.yml -f src/dataflow/docker-compose-rabbitmq.yml -f src/dataflow/docker-compose-postgres.yml up
```

## Data to create task in Data Flow
Name: credexpiredbatch
Type: task
Spring Boot Version: 3.x
URI: file:/home/cnb/scdf/credexpiredbatch-0.0.1.jar
Description: Force users to update credentials
