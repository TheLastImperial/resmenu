wget -O docker-compose.yml https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/main/src/docker-compose/docker-compose.yml;
wget -O docker-compose-rabbitmq.yml https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/main/src/docker-compose/docker-compose-rabbitmq.yml;
wget -O docker-compose-postgres.yml https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/main/src/docker-compose/docker-compose-postgres.yml;

```bash
# export DATAFLOW_VERSION=2.10.2
# export SKIPPER_VERSION=2.9.2
# Spring Boot 3.x
# export DATAFLOW_VERSION=2.11.5
# export SKIPPER_VERSION=2.11.5
export DATAFLOW_VERSION=3.0.0-SNAPSHOT-jdk17
export SKIPPER_VERSION=3.0.0-SNAPSHOT-jdk17

export DATAFLOW_VERSION=2.11.5
export SKIPPER_VERSION=2.11.5
export HOST_MOUNT_PATH=./apps
docker compose -f docker-compose.yml -f docker-compose-rabbitmq.yml -f docker-compose-postgres.yml up
```

Using generated security password: 1cf764ed-b4cf-4cad-8b60-7b2d1459fd53
```bash
export DATAFLOW_VERSION=2.11.5
export SKIPPER_VERSION=2.11.5
export HOST_MOUNT_PATH=./src/dataflow/apps
docker compose -f docker-compose.yml -f src/dataflow/docker-compose.yml -f src/dataflow/docker-compose-rabbitmq.yml -f src/dataflow/docker-compose-postgres.yml up
```

```bash
export HOST_MOUNT_PATH=./src/dataflow/apps
export DATAFLOW_VERSION=2.11.5-jdk17
export SKIPPER_VERSION=2.11.5-jdk17
docker compose -f docker-compose.yml -f src/dataflow/docker-compose.yml -f src/dataflow/docker-compose-rabbitmq.yml -f src/dataflow/docker-compose-postgres.yml up
```

Name: credexpiredbatch
Type: task
Spring Boot Version: 3.x
URI: file:/home/cnb/scdf/credexpiredbatch-0.0.1.jar
Description: Force users to update credentials

stderr:
Error: LinkageError occurred while loading main class org.springframework.boot.loader.launch.JarLauncher
	java.lang.UnsupportedClassVersionError: org/springframework/boot/loader/launch/JarLauncher has been compiled by a more recent version of the Java Runtime (class file version 61.0), this version of the Java Runtime only recognizes class file versions up to 55.0
