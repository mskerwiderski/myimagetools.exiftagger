ExifTagger
v1.5.1 (c)2015 m.skerwiderski

This is a short manual why and how to use ExifTagger.

Please notice:
==============
o You use ExifTagger at your own risk.
o It can't be guaranteed that ExifTagger works correctly.
o It can't be guaranteed that ExifTagger does not damage your image files.
o If you have suggestions for improvements, don't hesitate to contact me under michael@skerwiderski.de.

0. Sense of ExifTagger:
=======================

With ExifTagger you can automatically tag your digital scans from analog films.
For this, ExifTagger 
o accesses configuration files where you can store your gear information
o imports existing data files (e.g. from Nikon F6 or iPhone App PhotoExif)
o asks missing information from the command line. 

In detail you can tag:
o camera and lens information
o recording data (exposure time, aperture, flash used, filters used, ...)
o geo location
o film information
o development information

ExifTagger supports following data formats:
o Nikon F6 data format
o iPhone App PhotoExif data format
o Own data format (see "exiftagger-datafile-template.txt")
Datafiles are optional and not required.

1. Requirements:
================

o Java Runtime v1.8.0 or above (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
o ExifTool by Phil Harvey v9.68 or above (http://www.sno.phy.queensu.ca/~phil/exiftool/)
Maybe ExifTagger will run also with other versions of the Java Runtime or Exiftool. But it is not tested.

2. Installation and Configuration:
==================================

o Just decompress the package in any folder.
o Enter your gear information in the config files (folder "cfg").
  A lot of things are already pre-configured and can be adopted or adapted.
  But: Some configuration stuff should not be changed. Please pay attention to the notes in the files.
o Create (or just copy and adapt the template batch file "exiftagger-batch-template.sh"):	
	o Replace "<dataFile>" with the folder and filename of the data file you would like to import.
	  If you don't want to import any datafile, just delete the whole line.
	o Replace "<imageDir>" with the folder where the images are you would like to tag.
	o Replace "<imageFileFormat>" with the file format of the images you would like to tag.
	  You have to set the file format as a regular expression (see https://en.wikipedia.org/wiki/Regular_expression).
	  E.g. "img([0-9]{2}).tif" will match files like "img00.tif", "img01.tif", ..., "img99.tif".
	o Rename the batch file to "exiftagger.sh" (or "exiftagger.bat" if you are on a windows machine).  
o Now open up a command line and run the batch file.
