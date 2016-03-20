package de.msk.myimagetools.exiftagger.cli;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import de.msk.myimagetools.exiftagger.ExifTaggerException;
import de.msk.myimagetools.exiftagger.ExifTaggerProps;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoArtist;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoCamera;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoHybridProcess;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfos;
import de.msk.myimagetools.exiftagger.util.ExifSpecUtils;

public class CmdLineReaderUtils {

	private CmdLineReaderUtils() {
	}

	public static String getRollId(CmdLineParams cmdLineParams,
		ExifTaggerProps props) 
		throws ExifTaggerException {
		return CmdLineReader.getStringValue(
			cmdLineParams,
			"Roll Id", 
			props.getRollIdExample(), 
			props.getRollIdFormat(), 
			props.getRollIdRegEx());
	}

	public static String getFilmSpeed(CmdLineParams cmdLineParams) 
		throws ExifTaggerException {
		return CmdLineReader.getStringValue(
			cmdLineParams,
			"Film Speed (ISO)", 
			"eg. 25 50 64 100 200 400 800 1600 3200 6400 12800", "<n>", 
			"[0-9]+");
	}

	public static String getDateOfDevelopment(CmdLineParams cmdLineParams,
		ExifTaggerProps props) 
		throws ExifTaggerException {
		return CmdLineReader.getStringValue(
			cmdLineParams,
			"Date of Development", 
			props.getDateExample(), 
			props.getDateFormat(), 
			props.getDateRegEx());
	}

	public static String getDeveloperDilution(CmdLineParams cmdLineParams) 
		throws ExifTaggerException {
		return CmdLineReader.getStringValue(
			cmdLineParams,
			"Developer Dilution", 
			"1+10", "1+<n>", "1\\+[0-9]+");
	}
	
	public static String getDevelopmentTime(CmdLineParams cmdLineParams, int number) 
		throws ExifTaggerException {
		return CmdLineReader.getStringValue(
			cmdLineParams,
			"Development Time No. " + number, 
			"13:30", "[hh:]mm:ss", 
			"([0-9][0-9]:)?[0-9][0-9]:[0-5][0-9]");
	}
	
	public static String getDevelopmentPullPushFstops(CmdLineParams cmdLineParams) 
		throws ExifTaggerException {
		String input = CmdLineReader.getStringValue(
			cmdLineParams,
			"Development Pull or Push Fstops", 
			"0, -1.5, +2", "[+|-]<m.n>", 
			"([+,-])?[0-9](\\.[0|5])?");
		double factor = 1.0;
		if (StringUtils.startsWith(input, "-")) {
			factor = -1.0;
		}
		double pullPushValueDbl = 0.0d;
		String pullPushValue = StringUtils.remove(input, "+");
		pullPushValue = StringUtils.remove(pullPushValue, "-");
		NumberFormat format = NumberFormat.getInstance(Locale.US);
		try {
			pullPushValueDbl = format.parse(pullPushValue).doubleValue();
			pullPushValueDbl = pullPushValueDbl * factor;
		} catch (ParseException e) {
			throw new ExifTaggerException(e);
		}
		String result = ExifSpecUtils.EXIF_NO_VALUE;
		if (pullPushValueDbl < 0.0d) {
			result = "Pull -" + String.valueOf(Math.abs(pullPushValueDbl));
		} else if (pullPushValueDbl > 0.0d) {
			result = "Push +" + String.valueOf(Math.abs(pullPushValueDbl));
		}
		return result;
	}
	
	public static GearInfoHybridProcess getHybridProcess(CmdLineParams cmdLineParams,
		GearInfos gearInfos) 
		throws ExifTaggerException {
		return CmdLineReader.getRecordOfSelectedItem(
			cmdLineParams,
			gearInfos.getHybridProcesses(), 
			"Hybrid Process", "Hybrid Processes", true);
	}
	
	public static GearInfoArtist getArtist(CmdLineParams cmdLineParams,
		GearInfos gearInfos) 
		throws ExifTaggerException {
		return CmdLineReader.getRecordOfSelectedItem(
			cmdLineParams,
			gearInfos.getArtists(), 
			"Artist", "Artists", true);
	}
	
	public static GearInfoCamera getCamera(CmdLineParams cmdLineParams,
		GearInfos gearInfos, boolean unknownPossible) 
		throws ExifTaggerException {
		return CmdLineReader.getRecordOfSelectedItem(
			cmdLineParams,
			gearInfos.getCameras(), 
			"Camera", "Cameras", unknownPossible);
	}
	
	public static String getFilmFormat(CmdLineParams cmdLineParams,
		GearInfos gearInfos) 
		throws ExifTaggerException {
		return CmdLineReader.getDescOfSelectedItem(
			cmdLineParams,
			gearInfos.getFilmFormats(), 
			"Film Format", "Film Formats", true);
	}
	
	public static String getFilm(CmdLineParams cmdLineParams, 
		GearInfos gearInfos) 
		throws ExifTaggerException {
		return CmdLineReader.getDescOfSelectedItem(
			cmdLineParams,
			gearInfos.getFilms(), 
			"Film", "Films", true);
	}
	
	public static String getFilmExpirationDate(CmdLineParams cmdLineParams, 
		ExifTaggerProps props) 
		throws ExifTaggerException {
		return CmdLineReader.getStringValue(
			cmdLineParams,
			"Film Expiration Date", 
			props.getMonthYearExample(), 
			props.getMonthYearFormat(), 
			props.getMonthYearRegEx(), 
			false);
	}
	
	public static String getInstantFilm(CmdLineParams cmdLineParams, 
		GearInfos gearInfos) 
		throws ExifTaggerException {
		return CmdLineReader.getDescOfSelectedItem(
			cmdLineParams,
			gearInfos.getInstantFilms(), 
			"Instant Film", "Instant Films", true);
	}
	
	public static String getDeveloper(CmdLineParams cmdLineParams,
		GearInfos gearInfos) 
		throws ExifTaggerException {
		return CmdLineReader.getDescOfSelectedItem(
			cmdLineParams,
			gearInfos.getDevelopers(), 
			"Developer", "Developers", true);
	}
	
	public static String getDevelopmentProcess(CmdLineParams cmdLineParams,
		GearInfos gearInfos) 
		throws ExifTaggerException {
		return CmdLineReader.getDescOfSelectedItem(
			cmdLineParams,
			gearInfos.getDevelopmentProcesses(), 
			"Development Process", "Development Processes", true);
	}	

	public static String getDevelopmentTemperature(CmdLineParams cmdLineParams,
		GearInfos gearInfos) 
		throws ExifTaggerException {
		return CmdLineReader.getDescOfSelectedItem(
			cmdLineParams,
			gearInfos.getDevelopmentTemperatures(), 
			"Development Temperature", "Development Temperatures", true);
	}
	
	public static String getDevelopmentAgitation(CmdLineParams cmdLineParams,
		GearInfos gearInfos) 
		throws ExifTaggerException {
		return CmdLineReader.getDescOfSelectedItem(
			cmdLineParams,
			gearInfos.getDevelopmentAgitations(), 
			"Development Agitation", "Development Agitations", true);
	}
	
	public static String getDevelopmentLaboratory(CmdLineParams cmdLineParams,
		GearInfos gearInfos) 
		throws ExifTaggerException {
		return CmdLineReader.getDescOfSelectedItem(
			cmdLineParams,
			gearInfos.getDevelopmentLaboratories(), 
			"Development Laboratory", "Development Laboratory", true);
	}
	
	public static String getDevelopmentHardware(CmdLineParams cmdLineParams,
		GearInfos gearInfos) 
		throws ExifTaggerException {
		return CmdLineReader.getDescOfSelectedItem(
			cmdLineParams,
			gearInfos.getDevelopmentHardware(), 
			"Development Hardware", "Development Hardware", true);
	}
	
	public static String getDigitizingHardware(CmdLineParams cmdLineParams,
		GearInfos gearInfos) 
		throws ExifTaggerException {
		return CmdLineReader.getDescOfSelectedItem(
			cmdLineParams,
			gearInfos.getDigitizingHardware(), 
			"Digitizing Hardware", "Digitizing Hardware", true);
	}
	
	public static String getDigitizingSoftware(CmdLineParams cmdLineParams,
		GearInfos gearInfos) 
		throws ExifTaggerException {
		return CmdLineReader.getDescOfSelectedItem(
			cmdLineParams,
			gearInfos.getDigitizingSoftware(), 
			"Digitizing Software", "Digitizing Software", true);
	}
	
	public static String getAdditionalInfo(CmdLineParams cmdLineParams) 
		throws ExifTaggerException {
		return CmdLineReader.getStringValue(
			cmdLineParams,
			"Additional Info", 
			"Developed perfectly.", "<text>", 
			".+", false);
	}
}
