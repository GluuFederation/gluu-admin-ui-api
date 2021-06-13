# Gluu Admin UI API

Backend apis of [Gluu Admin UI] (https://github.com/GluuFederation/gluu-admin-ui).

## Installation

### Prerequisites

1. JDK 8+
2. Maven 3+
3. Git

### Development

To run the project locally execute following commands.

```
git clone https://github.com/GluuFederation/gluu-admin-ui-api.git
mvn clean install -Dmaven.test.skip=true
cd gluu-admin-ui-api\target
java -jar gluu-admin-ui-api-5.0.0-SNAPSHOT-standalone.jar
```

The application configuration can be modified at: https://github.com/GluuFederation/gluu-admin-ui-api/blob/main/app/src/main/resources/application-dev.properties

Once the project is compiled and started, UI can be accessed at port 8080.

http://localhost:8080/plugins/list
