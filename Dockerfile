FROM maven:3.8.1-jdk-11 as module-build

WORKDIR /module/build

COPY . .

RUN mvn clean package

FROM openjdk:11-jdk-slim as production

RUN useradd -ms /bin/bash cooking-instruction

USER cooking-instruction

WORKDIR /app

COPY --from=module-build --chown=cooking-instruction:cooking-instruction /module/build/target/cooking-instruction-0.0.1-SNAPSHOT.jar ./cooking-instruction-0.0.1-SNAPSHOT.jar

COPY docker/api/bin/entrypoint.sh /entrypoint.sh

CMD [ "/entrypoint.sh" ]