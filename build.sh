#!/bin/bash
mvn --projects pdfbox install -Dmaven.test.skip=true && cp ~/.m2/repository/org/apache/pdfbox/pdfbox/2.0.0-SNAPSHOT/pdfbox-2.0.0-SNAPSHOT.jar /nrx/lib/pdfbox.jar
