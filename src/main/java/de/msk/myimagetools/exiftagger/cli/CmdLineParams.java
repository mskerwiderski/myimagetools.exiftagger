package de.msk.myimagetools.exiftagger.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import de.msk.myimagetools.exiftagger.ExifTaggerException;
import de.msk.myimagetools.exiftagger.util.Utils;
import de.msk.myimagetools.exiftagger.util.csv.CsvUtils;

public class CmdLineParams {
	/*
	 * exiftagger
	 * [-exiftool <fileName>]
	 * [-configDir <directory>]
	 * [-datafile <fileName>] 
	 * -imageDir <directory>
	 * -imageFileFormat <fileNameExpression>
	 * [-writeKeywords]
	 * [-writeHybridInfoXmp]
	 * [-writeHybridInfoUserComment]
	 * [-writeGearInfo]
	 * [-writeEmptyValues]
	 * [-deleteBackupFiles | -overwriteOriginalFiles | -overwriteOriginalFilesInPlace]
	 * [-autoFile <fileName>]
	 */
	private static final String OPT_EXIFTOOL = "exiftool";
	private static final String OPT_CONFIGDIR = "configDir";
	private static final String OPT_DATA_FILE = "dataFile";
	private static final String OPT_IMAGE_DIR = "imageDir";
	private static final String OPT_IMAGE_FILE_FORMAT = "imageFileFormat";
	private static final String OPT_WRITE_KEYWORDS = "writeKeywords";
	private static final String OPT_WRITE_HYBRID_INFO_XMP = "writeHybridInfoXmp";
	private static final String OPT_WRITE_HYBRID_INFO_USER_COMMENT = "writeHybridInfoUserComment";
	private static final String OPT_WRITE_GEAR_INFO = "writeGearInfo";
	private static final String OPT_WRITE_EMPTY_VALUES = "writeEmptyValues";
	private static final String OPT_OVERWRITE_ORIGINAL_FILES = "overwriteOriginalFiles";
	private static final String OPT_OVERWRITE_ORIGINAL_FILES_IN_PLACE = "overwriteOriginalFilesInPlace";
	private static final String OPT_AUTO_FILE = "autoFile";
	
	private static final String USAGE = Utils.EXIFTAGGER
		+ " [-" + OPT_EXIFTOOL + " <fileName>] "	
		+ " [-" + OPT_CONFIGDIR + " <fileName>] "
		+ "-" + OPT_DATA_FILE + " <fileName> "
		+ "-" + OPT_IMAGE_DIR + " <directory> "
		+ "-" + OPT_IMAGE_FILE_FORMAT + " <fileFormatExpression> "
		+ " [-" + OPT_WRITE_KEYWORDS + "]"
		+ " [-" + OPT_WRITE_HYBRID_INFO_XMP + "]"
		+ " [-" + OPT_WRITE_HYBRID_INFO_USER_COMMENT + "]"
		+ " [-" + OPT_WRITE_GEAR_INFO + "]"
		+ " [-" + OPT_WRITE_EMPTY_VALUES + "]"
		+ " [-" + OPT_OVERWRITE_ORIGINAL_FILES 
		+ " | -" + OPT_OVERWRITE_ORIGINAL_FILES_IN_PLACE + "]"
		+ " [-" + OPT_AUTO_FILE + "]";
	
	public String exiftool;
	private static final String DEF_EXIFTOOL = "exiftool";
	public String configDir;
	private static final String DEF_CONFIG_DIR = "./cfg";
	public String dataFile;
	public String imageDir;
	public String imageFileFormat;
	public boolean writeKeywords;
	public boolean writeHybridInfoXmp;
	public boolean writeHybridInfoUserComment;
	public boolean writeGearInfo;
	public boolean writeEmptyValues;
	public enum WriteMode {
		BackupOriginalFiles,
		OverwriteOriginalFiles,
		OverwriteOriginalFilesInPlace
	}
	public WriteMode writeMode;
	public String autoFile;
	
	public Map<String, String> autoMap;
	public boolean hasAutoMap() {
		return (autoMap != null);
	}
	
	@Override
	public String toString() {
		return "CmdLineParams [exiftool=" + exiftool + ", configDir="
			+ configDir + ", dataFile=" + dataFile + ", imageDir="
			+ imageDir + ", imageFileFormat=" + imageFileFormat
			+ ", writeKeywords=" + writeKeywords + ", writeHybridInfoXmp="
			+ writeHybridInfoXmp + ", writeHybridInfoUserComment="
			+ writeHybridInfoUserComment + ", writeGearInfo="
			+ writeGearInfo + ", writeEmptyValues=" + writeEmptyValues
			+ ", writeMode=" + writeMode + ", autoFile=" + autoFile
			+ ", autoMap=" + autoMap + "]";
	}

	private static String[] addParamsFromFile(String[] args, String additionalParamsCfgFile) 
		throws ExifTaggerException {
		if (StringUtils.isEmpty(additionalParamsCfgFile)) {
			throw new IllegalArgumentException("additionalParamsCfgFile must not be empty.");
		}
		List<String> argList = new ArrayList<String>(Arrays.asList(args));
		if (new File(additionalParamsCfgFile).isFile()) {
			List<CSVRecord> records = CsvUtils.readCsvComplete(additionalParamsCfgFile);
			for (CSVRecord record : records) {
				for (int idx=0; idx < record.size(); idx++) {
					String arg = record.get(idx);
					arg = StringUtils.trimToEmpty(arg);
					if (!StringUtils.isEmpty(arg)) {
						argList.add(arg);
					}
				}
			}
			Utils.logcSep("Found '" + additionalParamsCfgFile + "'.");
			args = argList.toArray(new String[0]);
		}
		return args;
	}
	
	private static Map<String, String> getAutoMap(String autoFile) throws ExifTaggerException {
		Map<String, String> autoMap = new HashMap<String, String>();
		if (new File(autoFile).isFile()) {
			List<CSVRecord> records = CsvUtils.readCsvComplete(autoFile);
			for (CSVRecord record : records) {
				if (record.size() == 2) {
					autoMap.put(record.get(0), record.get(1));
				} 
			}
			Utils.logcSep("Found '" + autoFile + "'.");
		} else {
			Utils.logcSep("'" + autoFile + "' not found.");
		}
		return autoMap;
	}
	
	@SuppressWarnings("static-access")
	public static CmdLineParams parseCmdLineParams(String[] args, 
		String additionalParamsCfgFile) 
		throws ExifTaggerException {
		if (!StringUtils.isEmpty(additionalParamsCfgFile)) {
			args = CmdLineParams.addParamsFromFile(args, additionalParamsCfgFile);
		}
		CmdLineParams cmdLineParams = new CmdLineParams();
		Options options = new Options();
		options.addOption(OptionBuilder
			.isRequired(false)
			.hasArg()
			.withType(String.class)
			.withDescription("Path and filename of exiftool")
			.create(OPT_EXIFTOOL));
		options.addOption(OptionBuilder
			.isRequired(false)
			.hasArg()
			.withType(String.class)
			.withDescription("Path of configuration files")
			.create(OPT_CONFIGDIR));
		options.addOption(OptionBuilder
			.isRequired(false)
			.hasArg()
			.withType(String.class)
			.withDescription("Path and filename of datafile")
			.create(OPT_DATA_FILE));
		options.addOption(OptionBuilder
			.isRequired()
			.hasArg()
			.withType(String.class)
			.withDescription("Path of imagefiles")
			.create(OPT_IMAGE_DIR));
		options.addOption(OptionBuilder
			.isRequired()
			.hasArg()
			.withType(String.class)
			.withDescription("Format of imagefilenames")
			.create(OPT_IMAGE_FILE_FORMAT));
		options.addOption(OptionBuilder
			.isRequired(false)
			.hasArg(false)
			.withDescription("Write keywords to keywords iptc-tag")
			.create(OPT_WRITE_KEYWORDS));
		options.addOption(OptionBuilder
			.isRequired(false)
			.hasArg(false)
			.withDescription("Write hybrid informaton to xmp-tags")
			.create(OPT_WRITE_HYBRID_INFO_XMP));
		options.addOption(OptionBuilder
			.isRequired(false)
			.hasArg(false)
			.withDescription("Write hybrid informaton to xmp-tags")
			.create(OPT_WRITE_HYBRID_INFO_USER_COMMENT));
		options.addOption(OptionBuilder
			.isRequired(false)
			.hasArg(false)
			.withDescription("Write gear information to xmp-tags")
			.create(OPT_WRITE_GEAR_INFO));
		options.addOption(OptionBuilder
			.isRequired(false)
			.hasArg()
			.withType(String.class)
			.withDescription("Filename of autofile")
			.create(OPT_AUTO_FILE));
		OptionGroup optionGroup = new OptionGroup();
		optionGroup.addOption(OptionBuilder
			.isRequired(false)
			.hasArg(false)
			.withDescription("Overwrite original by renaming tmp file")
			.create(OPT_OVERWRITE_ORIGINAL_FILES));
		optionGroup.addOption(OptionBuilder
			.isRequired(false)
			.hasArg(false)
			.withDescription("Overwrite original by copying tmp file")
			.create(OPT_OVERWRITE_ORIGINAL_FILES_IN_PLACE));
		optionGroup.setRequired(false);
		options.addOptionGroup(optionGroup);
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
			cmdLineParams.exiftool = cmd.getOptionValue(OPT_EXIFTOOL);
			if (StringUtils.isEmpty(cmdLineParams.exiftool)) {
				cmdLineParams.exiftool = DEF_EXIFTOOL;
			}
			cmdLineParams.configDir = cmd.getOptionValue(OPT_CONFIGDIR);
			if (StringUtils.isEmpty(cmdLineParams.configDir)) {
				cmdLineParams.configDir = DEF_CONFIG_DIR;
			}
			if (!StringUtils.endsWith(cmdLineParams.configDir, "/")) {
				cmdLineParams.configDir += "/";
			}
			cmdLineParams.dataFile = cmd.getOptionValue(OPT_DATA_FILE);
			cmdLineParams.imageDir = cmd.getOptionValue(OPT_IMAGE_DIR);
			if (!StringUtils.endsWith(cmdLineParams.imageDir, "/")) {
				cmdLineParams.imageDir += "/";
			}
			cmdLineParams.imageFileFormat = cmd.getOptionValue(OPT_IMAGE_FILE_FORMAT);
			cmdLineParams.writeKeywords = 
				cmd.hasOption(OPT_WRITE_KEYWORDS);
			cmdLineParams.writeHybridInfoXmp = 
				cmd.hasOption(OPT_WRITE_HYBRID_INFO_XMP);
			cmdLineParams.writeHybridInfoUserComment = 
				cmd.hasOption(OPT_WRITE_HYBRID_INFO_USER_COMMENT);
			cmdLineParams.writeGearInfo = 
				cmd.hasOption(OPT_WRITE_GEAR_INFO);
			cmdLineParams.writeEmptyValues = 
				cmd.hasOption(OPT_WRITE_EMPTY_VALUES);
			if (cmd.hasOption(OPT_OVERWRITE_ORIGINAL_FILES)) {
				cmdLineParams.writeMode = WriteMode.OverwriteOriginalFiles;
			} else if (cmd.hasOption(OPT_OVERWRITE_ORIGINAL_FILES_IN_PLACE)) {
				cmdLineParams.writeMode = WriteMode.OverwriteOriginalFilesInPlace;
			} else {
				cmdLineParams.writeMode = WriteMode.BackupOriginalFiles;				
			}
			if (cmd.hasOption(OPT_AUTO_FILE)) {
				cmdLineParams.autoFile = 
					cmd.getOptionValue(OPT_AUTO_FILE);
				cmdLineParams.autoMap = getAutoMap(cmdLineParams.autoFile);
			}
		} catch (Exception e) {
			Utils.logcSep(e.getMessage());
			HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.printHelp(USAGE, "", options, "");
			Utils.logcSep();
			cmdLineParams = null;
		}
		return cmdLineParams;
	}
}
