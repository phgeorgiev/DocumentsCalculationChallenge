# DocumentsCalculationChallenge
Implementing https://github.com/clippings/documents-calculation-challenge in Java as cli app using picocli.

## Requirements
 - Java >= 17  

## Build the executable file
```shell
./mvnw package
```

## Run command as a Java program
```shell
target/appassembler/bin/documents-calculate
```

```shell
Usage: documents-calculate [-hV] [-v=<vatNumber>] <filePath> <exhangeRates>
                           <outputCurrency>
Sum invoice documents in different currencies via a CSV file.
      <filePath>          Path of the CSV file containing the documents
      <exhangeRates>      A list of currencies and exchange rates. The
                            currencies can have different exchange rates, based
                            on a default currency: EUR:GBP, EUR:BGN and so on.
                            The default currency is specified by giving it an
                            exchange rate of 1.
                          (Example: EUR:1,USD:0.987,GBP:0.878)
      <outputCurrency>    Output currency
  -h, --help              Show this help message and exit.
  -v, --vat=<vatNumber>   Summed documents for specified customer by vat number
  -V, --version           Print version information and exit.
```

### Example input
```shell
documents-calculate src/test/resources/data.csv EUR:1,USD:0.987,GBP:0.878 EUR
```

### Example output
```shell
Customer Vendor 1 - 1962.22 EUR
Customer Vendor 2 - 697.37 EUR
Customer Vendor 3 - 1580.64 EUR
```

## Using Docker
```shell
docker build -t documents-calculate .
docker run -it --rm documents-calculate src/test/resources/data.csv EUR:1,USD:0.987,GBP:0.878 EUR
```

