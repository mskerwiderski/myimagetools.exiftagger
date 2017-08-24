package de.msk.myimagetools.exiftagger.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class Utils {

	private Utils() {
	}

	public static final String EXIFTAGGER = "ExifTagger";
	public static final String EXIFTAGGER_VERSION = "v1.6.1";
	public static final String EXIFTAGGER_COPYRIGHT = "(c)2015 michael skerwiderski";
	
	public static final String UNKNOWN = "unknown";
	public static final String LF = "\n";
	public static final String LINE = StringUtils.repeat("-", 40);
	
	public static void logc(Object obj) {
		System.out.print(obj);
	}
	public static void logcLn(Object obj) {
		logc(obj + LF);
	}
	public static void logcLnLn(Object obj) {
		logc(obj + LF + LF);
	}
	public static void logcSep(Object obj) {
		logc(obj + LF + LINE + LF);
	}
	public static void logcSep() {
		logc(LF + LINE + LF);
	}
	
	public static String getExifTaggerTimestamp() {
		DateFormat dateFmt = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		return dateFmt.format(new Date());
	}
}
