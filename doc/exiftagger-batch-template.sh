#!/bin/bash
java -jar exiftagger.jar \
-configDir ./cfg \
-dataFile "<dataFile>" \
-imageDir "<imageDir>" \
-imageFileFormat "<imageFileFormat>" \
-writeKeywords \
-writeHybridInfoXmp \
-writeHybridInfoUserComment \
-writeGearInfo \
-writeEmptyValues \
-overwriteOriginalFilesInPlace
