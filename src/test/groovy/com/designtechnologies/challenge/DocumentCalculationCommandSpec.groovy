package com.designtechnologies.challenge

import com.designtechnologies.challenge.cli.DocumentsCalculationCommandFactory
import picocli.CommandLine
import spock.lang.Specification

import java.nio.file.Paths

class DocumentCalculationCommandSpec extends Specification {

    void "Test documents-calculate with command line output"() {
        given:
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        System.setOut(new PrintStream(baos))

        String filePth = Paths.get('src/test/resources/data.csv').toAbsolutePath().toString()
        String[] args = [filePth, 'EUR:1,USD:0.987,GBP:0.878', 'EUR'] as String[]
        new CommandLine(new DocumentsCalculationCommandFactory().create()).execute(args)

        expect:
        baos.toString().contains(
                "Customer Vendor 1 - 1962.22 EUR\n" +
                "Customer Vendor 2 - 697.37 EUR\n" +
                "Customer Vendor 3 - 1580.64 EUR\n"
        )
    }

    void "Test documents-calculate with invalid exchange rates"() {
        given:
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        System.setOut(new PrintStream(baos))

        String filePth = Paths.get('src/test/resources/data.csv').toAbsolutePath().toString()
        String[] args = [filePth, 'EUR:1,USD:0.987,TEST:10', 'EUR'] as String[]
        new CommandLine(new DocumentsCalculationCommandFactory().create()).execute(args)

        expect:
        baos.toString().contains("Invalid exchange rate")
    }
}
