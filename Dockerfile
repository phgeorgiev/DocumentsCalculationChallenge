FROM openjdk:17-oracle

COPY . /home/app/DocumentsCalculationChalleng
WORKDIR /home/app/DocumentsCalculationChalleng

RUN ./mvnw package

ENTRYPOINT ["target/appassembler/bin/documents-calculate"]
