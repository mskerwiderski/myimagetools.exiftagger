package de.msk.myimagetools.exiftagger.exiftool;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.myimagetools.exiftagger.ExifTaggerException;
import de.msk.myimagetools.exiftagger.ExifTaggerProps;
import de.msk.myimagetools.exiftagger.ExifTaggerProps.Keyword;
import de.msk.myimagetools.exiftagger.cli.CmdLineParams;
import de.msk.myimagetools.exiftagger.domain.CameraAndFilmDataRecord;
import de.msk.myimagetools.exiftagger.domain.DomainUtils;
import de.msk.myimagetools.exiftagger.domain.HybridProcessRecord;
import de.msk.myimagetools.exiftagger.domain.ImageDataRecord;
import de.msk.myimagetools.exiftagger.domain.ImageDataRecord.GpsPosition;
import de.msk.myimagetools.exiftagger.exiftool.ExiftoolUtils.ExecExiftoolResult;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoArtist;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoCamera;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoHybridProcess.HybridProcessType;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoLens;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfos;
import de.msk.myimagetools.exiftagger.util.ExifSpecUtils;
import de.msk.myimagetools.exiftagger.util.Utils;

public class ExiftoolWriter {

	private static final Log log = LogFactory.getLog(ExiftoolWriter.class);
	
	private static class ImageFilenameFilter implements FilenameFilter {
		private String imageFileFormat;
		
		private ImageFilenameFilter(String fileFormatExpression) {
			this.imageFileFormat = fileFormatExpression;
		}

		public boolean accept(File dir, String name) {
			return Pattern.matches(this.imageFileFormat, name);
		}
	}
	
	private static int getImageFileNameNumber(String imageFileFormat, String fileName) 
		throws ExifTaggerException {
		Pattern pattern = Pattern.compile(imageFileFormat);
		Matcher matcher = pattern.matcher(fileName);
		int imageFileNameNumber = -1;
		if (matcher.matches()) {
			imageFileNameNumber = Integer.valueOf(matcher.group(1));
		} else {
			throw new ExifTaggerException(
				"invalid imageFileFormat '" + imageFileFormat + "'");
		}
		return imageFileNameNumber;
	}
	
	private static final String KEYWORD_DELIMITER = "#";
	
	private static void addArg(List<String> args, String arg, boolean writeEmptyValue, String value) {
		addArg(args, null, arg, false, writeEmptyValue, value);
	}
	
	private static void addArg(List<String> args, String namespace, String arg, boolean writeEmptyValue, String value) {
		addArg(args, namespace, arg, false, writeEmptyValue, value);
	}
	
	private static void addArg(List<String> args, String arg, boolean argIsEnum, boolean writeEmptyValue, String value) {
		addArg(args, null, arg, argIsEnum, writeEmptyValue, value);
	}
	
	private static void addArg(List<String> args, String namespace, 
		String arg, boolean argIsEnum, boolean writeEmptyValue, String value) {
		if (args == null) {
			throw new IllegalArgumentException("args must not be null.");
		}
		if (StringUtils.isEmpty(arg)) {
			throw new IllegalArgumentException("arg must not be empty.");
		}
		if (writeEmptyValue || !StringUtils.isEmpty(value)) {
			if (value == null) {
				value = "";
			}
			if (!StringUtils.isEmpty(namespace)) {
				arg = namespace + ":" + arg;
			}
			if (argIsEnum) {
				args.add("-" + arg + "#=" + value);
			} else {
				args.add("-" + arg + "=" + value);
			}
		}
	}
	
	private static String addKeyword(String keywords, String keyword) {
		if (keywords == null) {
			throw new IllegalArgumentException("keywords must not be null.");
		}
		if (!StringUtils.isEmpty(keyword) && 
			!StringUtils.equals(keyword, ExifSpecUtils.EXIF_NO_VALUE)) {
			if (!StringUtils.isEmpty(keywords)) {
				keywords += KEYWORD_DELIMITER;
			}
			keywords += keyword;
		}
		return keywords;
	}
	
	public static void writeFilmData(
		CmdLineParams cmdLineParams,  
		ExifTaggerProps props,
		GearInfos gearInfos,
		CameraAndFilmDataRecord cameraAndFilmDataRecord) 
			throws ExifTaggerException {
		HybridProcessRecord hybridProcessRecord = 
			cameraAndFilmDataRecord.getHybridProcessRecord();
		Map<Integer, ImageDataRecord> 
			imageDataRecordsMap = cameraAndFilmDataRecord.getImageDataRecordsMap();
		
		boolean exifInfoWritten = false;
		try {
			File dir = new File(cmdLineParams.imageDir);
		    File[] files = dir.listFiles(new ImageFilenameFilter(cmdLineParams.imageFileFormat));
		    if (files == null) {
		    	Utils.logcLn("No matching files found in '" + cmdLineParams.imageDir + "'.");
		    } else {
			    Utils.logcLn(files.length + " matching files found in '" + cmdLineParams.imageDir + "'.");
				for (int i=0; i < files.length; i++) {
					ImageDataRecord imageDataRecord = null;
					int imageNo = getImageFileNameNumber(
						cmdLineParams.imageFileFormat, files[i].getName());
					if (imageDataRecordsMap != null) {
						imageDataRecord = imageDataRecordsMap.get(imageNo);
					}
					List<String> exiftoolArgs = new ArrayList<String>();
					exiftoolArgs.add("-config");
					exiftoolArgs.add(cmdLineParams.configDir + "exiftool-msk.cfg");
					Utils.logc("Writing exif information to '" + files[i].getName() + "' ... ");
					
					String keywords = "";
					
					addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK, 
						"ExifTaggerVersion", cmdLineParams.writeEmptyValues, Utils.EXIFTAGGER_VERSION);
					addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK, 
						"ExifTaggerTimestamp", cmdLineParams.writeEmptyValues, Utils.getExifTaggerTimestamp());
					addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK, 
						"ExifTaggerCopyright", cmdLineParams.writeEmptyValues, Utils.EXIFTAGGER_COPYRIGHT);
	
					addArgsArtistRecord(cmdLineParams, cameraAndFilmDataRecord, exiftoolArgs);
					keywords = addArgsCameraRecord(cmdLineParams, props, cameraAndFilmDataRecord, 
						imageDataRecord, exiftoolArgs, keywords);
					keywords = arrArgsLensRecord(cmdLineParams, gearInfos, props, 
						cameraAndFilmDataRecord, imageDataRecord, exiftoolArgs, keywords);
					
					String[] focalLengths = DomainUtils.getFocalLengths(gearInfos,
						cameraAndFilmDataRecord, imageDataRecord);
					addArg(exiftoolArgs, "FocalLength", cmdLineParams.writeEmptyValues, focalLengths != null ? focalLengths[0] : null);
					addArg(exiftoolArgs, "FocalLengthIn35mmFormat", cmdLineParams.writeEmptyValues, focalLengths != null ? focalLengths[1] : null);
					
					addArg(exiftoolArgs, "ISO", cmdLineParams.writeEmptyValues, cameraAndFilmDataRecord.getFilmSpeed());
					addArgsImageDataRecord(cmdLineParams, exiftoolArgs, imageDataRecord);
					keywords = addArgsHybridProcessRecord(cmdLineParams, props, exiftoolArgs, keywords, hybridProcessRecord, imageNo);
					if ((imageDataRecord != null) && props.isKeyword(Keyword.tagsFromDataFile)) {
						for (String tag : imageDataRecord.getTags()) {
							keywords = addKeyword(keywords, tag);
						}
					}
					exiftoolArgs.add("-sep");
					exiftoolArgs.add("#");
					addArg(exiftoolArgs, "keywords", cmdLineParams.writeEmptyValues, 
						cmdLineParams.writeKeywords && !StringUtils.isEmpty(keywords) ? 
							keywords : null);
					addArg(exiftoolArgs, "subject", cmdLineParams.writeEmptyValues, 
						cmdLineParams.writeKeywords && !StringUtils.isEmpty(keywords) ? 
							keywords : null);
					exiftoolArgs.add("-iptc:all");
					exiftoolArgs.add("-codedcharacterset=utf8");
					log.debug("added keywords to exiftoolArgs: " + keywords);
					exiftoolArgs.add(files[i].getAbsolutePath());
					ExiftoolUtils.deleteAllXmpMskInfos(cmdLineParams.exiftool,
						files[i].getAbsolutePath(), cmdLineParams.writeMode);
					log.debug("all XmpMskInfos deleted.");
					log.debug("exiftoolArgs: " + exiftoolArgs);
					ExecExiftoolResult result = 
						ExiftoolUtils.execExiftool(cmdLineParams.exiftool,
							exiftoolArgs, cmdLineParams.writeMode);
					Utils.logcLn(result.isSuccess() ?  
						"done: " + result.getResult() : 
						"failed: " + result.getResult());
				}
				exifInfoWritten = true;
		    }
		} catch (Exception e) {
			throw new ExifTaggerException(e);
		} finally {
			if (!exifInfoWritten) {
				Utils.logcSep("Nothing to do.");
			} else {
				Utils.logcSep("Writing exif information to matching files successfully done.");
			}
		}
	}
	
	private static void addArgsArtistRecord(CmdLineParams cmdLineParams,
		CameraAndFilmDataRecord cameraAndFilmDataRecord, 
		List<String> exiftoolArgs) {
		GearInfoArtist artist = cameraAndFilmDataRecord.getArtist();
		addArg(exiftoolArgs, "Artist", cmdLineParams.writeEmptyValues, artist != null ? artist.getFullName() : null);
		addArg(exiftoolArgs, "Creator", cmdLineParams.writeEmptyValues, artist != null ? artist.getFullName() : null);
		addArg(exiftoolArgs, "By-line", cmdLineParams.writeEmptyValues, artist != null ? artist.getFullName() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_IPTCCORE, "CreatorWorkEmail", cmdLineParams.writeEmptyValues, artist != null ? artist.getEmailAddress() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_IPTCCORE, "CreatorCountry", cmdLineParams.writeEmptyValues, artist != null ? artist.getCountry() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_IPTCCORE, "CreatorRegion", cmdLineParams.writeEmptyValues, artist != null ? artist.getRegion() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_IPTCCORE, "CreatorPostalCode", cmdLineParams.writeEmptyValues, artist != null ? artist.getPostalCode() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_IPTCCORE, "CreatorCity", cmdLineParams.writeEmptyValues, artist != null ? artist.getCity() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_IPTCCORE, "CreatorAddress", cmdLineParams.writeEmptyValues, artist != null ? artist.getStreet() : null);
		addArg(exiftoolArgs, "CopyrightNotice", cmdLineParams.writeEmptyValues, artist != null ? artist.getCopyrightNotice() : null);
		addArg(exiftoolArgs, "CopyrightStatus", true, cmdLineParams.writeEmptyValues, artist != null ? 
			ExifSpecUtils.ExifCopyrightStatus.getValueByName(artist.getCopyrightStatus()) : null);
		addArg(exiftoolArgs, "Rights", cmdLineParams.writeEmptyValues, artist != null ? artist.getCopyrightNotice() : null);
		addArg(exiftoolArgs, "UsageTerms", cmdLineParams.writeEmptyValues, artist != null ? artist.getUsageTerms() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_XMP, "xmpRights", cmdLineParams.writeEmptyValues, artist != null ? "True" : null);
		addArg(exiftoolArgs, "CopyrightFlag", cmdLineParams.writeEmptyValues, artist != null ? "True" : null);
	}
	
	private static String arrArgsLensRecord(CmdLineParams cmdLineParams, GearInfos gearInfos, ExifTaggerProps props,
		CameraAndFilmDataRecord cameraAndFilmDataRecord, 
		ImageDataRecord imageDataRecord, List<String> exiftoolArgs, String keywords) {
		GearInfoLens lens = DomainUtils.getLens(
			gearInfos, cameraAndFilmDataRecord, imageDataRecord);
		addArg(exiftoolArgs, "LensMake", cmdLineParams.writeEmptyValues, lens != null ? lens.getLensMake() : null);
		addArg(exiftoolArgs, "LensModel", cmdLineParams.writeEmptyValues, lens != null ? lens.getLensModel() : null);
		addArg(exiftoolArgs, "MaxApertureValue", cmdLineParams.writeEmptyValues, lens != null ? lens.getLensMaxAperture() : null);
		addArg(exiftoolArgs, "LensSerialNumber", cmdLineParams.writeEmptyValues, lens != null ? lens.getLensSerialNumber() : null);
		addArg(exiftoolArgs, "Lens", cmdLineParams.writeEmptyValues, lens != null ? lens.getMakeAndModel() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_GI, "LensReleaseDate", cmdLineParams.writeEmptyValues, 
			cmdLineParams.writeGearInfo && (lens != null) ? lens.getLensReleaseDate() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_GI, "LensManufacturingDate", cmdLineParams.writeEmptyValues, 
			cmdLineParams.writeGearInfo && (lens != null) ? lens.getLensManufacturingDate() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_GI, "LensKadlubeksCatalogNo", cmdLineParams.writeEmptyValues, 
			cmdLineParams.writeGearInfo && (lens != null) ? lens.getLensKadlubeksCatalogNo() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_GI, "LensAdditionalInfo", cmdLineParams.writeEmptyValues, 
			cmdLineParams.writeGearInfo && (lens != null) ? lens.getLensAdditionalInfo() : null);
		if (props.isKeyword(Keyword.lens)) {
			keywords = addKeyword(keywords, lens != null ? lens.getMakeAndModel() : null);
		}
		return keywords;
	}
	
	private static String addArgsCameraRecord(CmdLineParams cmdLineParams, ExifTaggerProps props,
		CameraAndFilmDataRecord cameraAndFilmDataRecord, 
		ImageDataRecord imageDataRecord, List<String> exiftoolArgs, String keywords) {
		GearInfoCamera camera = DomainUtils.getCamera(cameraAndFilmDataRecord, imageDataRecord);
		addArg(exiftoolArgs, "Make", cmdLineParams.writeEmptyValues, camera != null ? camera.getCameraMake() : null);
		addArg(exiftoolArgs, "Model", cmdLineParams.writeEmptyValues, camera != null ? camera.getCameraModel() : null);
		addArg(exiftoolArgs, "SerialNumber", cmdLineParams.writeEmptyValues, camera != null ? camera.getCameraSerialNumber() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_GI, "cameraReleaseDate", cmdLineParams.writeEmptyValues, 
			cmdLineParams.writeGearInfo && (camera != null) ? 
				camera.getCameraReleaseDate() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_GI, "cameraManufacturingDate", cmdLineParams.writeEmptyValues, 
			cmdLineParams.writeGearInfo && (camera != null) ? 
				camera.getCameraManufacturingDate() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_GI, "cameraKadlubeksCatalogNo", cmdLineParams.writeEmptyValues, 
			cmdLineParams.writeGearInfo && (camera != null) ? 
				camera.getCameraKadlubeksCatalogNo() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_GI, "cameraAdditionalInfo", cmdLineParams.writeEmptyValues, 
			cmdLineParams.writeGearInfo && (camera != null) ? 
				camera.getCameraAdditionalInfo() : null);
		if (props.isKeyword(Keyword.camera)) {
			keywords = addKeyword(keywords, camera != null ? camera.getMakeAndModel() : null);
		}
		return keywords;
	}
	
	private static String addArgsHybridProcessRecord(CmdLineParams cmdLineParams, ExifTaggerProps props,
		List<String> exiftoolArgs, String keywords, HybridProcessRecord hybridProcessRecord, int imageNo) {
		boolean writeXmp = cmdLineParams.writeHybridInfoXmp && 
			(hybridProcessRecord != null);
		boolean writeUserComment = cmdLineParams.writeHybridInfoUserComment && 
			(hybridProcessRecord != null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "RollId", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getRollId() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "ImageNo", cmdLineParams.writeEmptyValues, 
			writeXmp ? String.valueOf(imageNo) : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "DateOfDevelopment", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getDateOfDevelopment() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "FilmFormat", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getFilmFormat() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "FilmName", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getFilmName() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "FilmExpirationDate", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getFilmExpirationDate() : null);
		if (props.isKeyword(Keyword.film)) {
			keywords = addKeyword(keywords, hybridProcessRecord != null ? 
				hybridProcessRecord.getFilmName() : null);
		}
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "DeveloperName", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getDeveloperName() : null);
		if (props.isKeyword(Keyword.developer)) {
			keywords = addKeyword(keywords, hybridProcessRecord != null ? 
				hybridProcessRecord.getDeveloperName() : null);
		}
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "DeveloperDilution", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getDeveloperDilution() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "DevelopmentTemperature", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getDevelopmentTemperature() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "DevelopmentTime", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getDevelopmentTime() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "DevelopmentTime2", cmdLineParams.writeEmptyValues, 
			writeXmp && hybridProcessRecord.getProcessType().equals(HybridProcessType.FilmSelfSlide) ? 
				hybridProcessRecord.getDevelopmentTime2() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "DevelopmentProcess", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getDevelopmentProcess() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "DevelopmentAgitations", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getDevelopmentAgitations() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "DevelopmentPullPushFstops", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getDevelopmentPullPushFstops() : null);
		if (props.isKeyword(Keyword.developmentPullPushFstops)) {
			keywords = addKeyword(keywords, hybridProcessRecord != null ? 
				hybridProcessRecord.getDevelopmentPullPushFstops() : null);
		}
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "DevelopmentHardware", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getDevelopmentHardware() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "DevelopmentLaboratory", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getDevelopmentLaboratory() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "DigitizingHardware", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getDigitizingHardware() : null);
		if (props.isKeyword(Keyword.digitizingHardware)) {
			keywords = addKeyword(keywords, hybridProcessRecord != null ? 
				hybridProcessRecord.getDigitizingHardware() : null);
		}
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "DigitizingSoftware", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getDigitizingSoftware() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP, "AdditionalInfo", cmdLineParams.writeEmptyValues, 
			writeXmp ? hybridProcessRecord.getAdditionalInfo() : null);
		addArg(exiftoolArgs, "UserComment", cmdLineParams.writeEmptyValues, 
			writeUserComment ? hybridProcessRecord.getUserComment() : null);
		return keywords;
	}
	
	private static void addArgsImageDataRecord(CmdLineParams cmdLineParams, List<String> exiftoolArgs, ImageDataRecord imageDataRecord) {
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_MSK_HP,  
			"ImageNo", cmdLineParams.writeEmptyValues, 
			imageDataRecord != null ? imageDataRecord.getImageNumber().toString() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_IPTC,  
			"ObjectName", cmdLineParams.writeEmptyValues, 
			imageDataRecord != null ? imageDataRecord.getTitle() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_IPTC, 
			"Caption-Abstract", cmdLineParams.writeEmptyValues, 
			imageDataRecord != null ? imageDataRecord.getCaptionAbstract() : null);
		addArg(exiftoolArgs, "DateTimeOriginal", cmdLineParams.writeEmptyValues,  
			imageDataRecord != null ? imageDataRecord.getDateTimeOriginal() : null);
		addArg(exiftoolArgs, "ExposureTime", cmdLineParams.writeEmptyValues, 
			imageDataRecord != null ? imageDataRecord.getExposureTime() : null);
		addArg(exiftoolArgs, "FNumber", cmdLineParams.writeEmptyValues, 
			imageDataRecord != null ? imageDataRecord.getAperture() : null);
		addArg(exiftoolArgs, 
			"Flash", true, cmdLineParams.writeEmptyValues, 
			imageDataRecord != null ? imageDataRecord.getFlashMode() : null);
		addArg(exiftoolArgs, 
			"ExposureMode", true, cmdLineParams.writeEmptyValues, 
			imageDataRecord != null ? imageDataRecord.getExposureMode() : null);
		addArg(exiftoolArgs, 
			"MeteringMode", true, cmdLineParams.writeEmptyValues, 
			imageDataRecord != null ? imageDataRecord.getMeteringMode() : null);
		addArg(exiftoolArgs, 
			"ExposureProgram", true, cmdLineParams.writeEmptyValues, 
			imageDataRecord != null ? imageDataRecord.getExposureProgram() : null);
		addArg(exiftoolArgs, 
			"ExposureCompensation", cmdLineParams.writeEmptyValues, 
			imageDataRecord != null ? imageDataRecord.getExposureCompensation() : null);
		// gps position
		GpsPosition gpsPosition = (imageDataRecord != null ? imageDataRecord.getGpsPosition() : null);
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_EXIF, "GpsLatitude", 
			cmdLineParams.writeEmptyValues, 
			(gpsPosition != null ? gpsPosition.getLatitude() : null));
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_EXIF, "GpsLatitudeRef",
			cmdLineParams.writeEmptyValues, 	
			(gpsPosition != null ? gpsPosition.getLatitudeRef() : null));
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_EXIF, "GpsLongitude",
			cmdLineParams.writeEmptyValues, 
			(gpsPosition != null ? gpsPosition.getLongitude() : null));
		addArg(exiftoolArgs, ExifSpecUtils.NAMESPACE_EXIF, "GpsLongitudeRef",
			cmdLineParams.writeEmptyValues, 
			(gpsPosition != null ? gpsPosition.getLongitudeRef() : null));
	}
}
