docker-compose -f $PSScriptRoot\docker-images\kafka\docker-compose.yml --env-file $PSScriptRoot\docker.env up -d zookeeper kafka-broker-1 kafka-broker-2 schemaregistry add-topics-notification dbservice kafkaconnect