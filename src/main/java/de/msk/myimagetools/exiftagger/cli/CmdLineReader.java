package de.msk.myimagetools.exiftagger.cli;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import de.msk.myimagetools.exiftagger.ExifTaggerException;
import de.msk.myimagetools.exiftagger.gearinfo.AbstractGearInfo;
import de.msk.myimagetools.exiftagger.util.ExifSpecUtils;
import de.msk.myimagetools.exiftagger.util.Utils;

public class CmdLineReader {

	public CmdLineReader() {
	}

	public static String getStringValue(
		CmdLineParams cmdLineParams,
		String description, String example, 
		String format, String expression)
		throws ExifTaggerException {
		return getStringValue(cmdLineParams, description, example, format, expression, true);
	}
		
	@SuppressWarnings("resource")
	public static String getStringValue(
		CmdLineParams cmdLineParams,
		String description, String example, 
		String format, String expression,
		boolean mustNotBeEmpty) 
		throws ExifTaggerException {
		String value = ExifSpecUtils.EXIF_NO_VALUE;
		if (cmdLineParams.hasAutoMap() && cmdLineParams.autoMap.containsKey(description)) {
			value = cmdLineParams.autoMap.get(description);
		} else {
			boolean done = false;
			while (!done) {
				Utils.logc("Enter " + description + " (e.g. '" + example + "'): ");
				String in = (new Scanner(System.in)).nextLine();
				if (!mustNotBeEmpty && StringUtils.isEmpty(in)) {
					done = true;
					value = ExifSpecUtils.EXIF_NO_VALUE;
				} else {
					done = Pattern.matches(expression, in);
					if (!done) {
						Utils.logcLn("Input value must match format '" + 
							format + "'. Please try again.");
					} else {
						value = in;
					}
				}
			}
		}
		Utils.logcSep("Selected " + description + ": " + value);
		return value;
	}
	
	public static String getDescOfSelectedItem(
		CmdLineParams cmdLineParams,
		List<? extends AbstractGearInfo> gearInfoList, 
		String nameSingle, String namePlural, boolean unknownPossible) 
		throws ExifTaggerException {
		String desc = ExifSpecUtils.EXIF_NO_VALUE;
		AbstractGearInfo gearInfo = getRecordOfSelectedItem(
			cmdLineParams, gearInfoList, 
			nameSingle, namePlural, unknownPossible);
		if (gearInfo != null) {
			desc = gearInfo.getDesc();
		}
		return desc;
	}
			
	@SuppressWarnings("resource")
	public static  <T extends AbstractGearInfo> T getRecordOfSelectedItem(
		CmdLineParams cmdLineParams, List<T> gearInfoList, 
		String nameSingle, String namePlural, boolean unknownPossible) 
		throws ExifTaggerException {
		T record = null;
		if (cmdLineParams.hasAutoMap() && cmdLineParams.autoMap.containsKey(nameSingle)) {
			int id = Integer.valueOf(cmdLineParams.autoMap.get(nameSingle));
			record = gearInfoList.get(id);
		} else {
			Utils.logcLn(namePlural + ":");
			Utils.logcLn(StringUtils.leftPad("", namePlural.length() + 1, "="));
			for (int i=0; i < gearInfoList.size(); i++) {
				Utils.logcLn("[" + 
					StringUtils.leftPad(String.valueOf(i), 2, "0") + "] " + 
					gearInfoList.get(i).getDesc());
			}
			if (unknownPossible) {
				Utils.logcLn("[" + 
					StringUtils.leftPad(String.valueOf(gearInfoList.size()), 2, "0") + "] " + 
					ExifSpecUtils.EXIF_NO_VALUE);
			}
			
			boolean selected = false;
			while (!selected) {
				Utils.logc("Select " + nameSingle + ": ");
				String in = (new Scanner(System.in)).next();
				if (!StringUtils.isNumeric(in)) {
					Utils.logcLn("'" + in + "' is not a number. Please try again.");
				} else {
					int inInt = Integer.valueOf(in);
					int maxValue = (unknownPossible ? gearInfoList.size() : gearInfoList.size()-1);
					if ((inInt < 0) || (inInt > maxValue)) {
						Utils.logcLn("Input value must be between 1 and " + 
							maxValue + ". Please try again.");
					} else if (unknownPossible && (inInt == maxValue)) {
						selected = true;
					} else {
						selected = true;
						record = gearInfoList.get(inInt);
					}
				}
			}
		}
		Utils.logcSep("Selected " + nameSingle + ": " + 
			((record == null) ? "none" : record.getDesc()));
		return record;
	}
}
