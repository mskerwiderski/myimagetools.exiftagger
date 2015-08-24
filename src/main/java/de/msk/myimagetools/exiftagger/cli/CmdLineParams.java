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
	 * [-configDir <directory>]
	 * [-importfile <fileName>] 
	 * -imageDir <directory>
	 * -fileNameFormat <fileNameExpression>
	 * [-writeKeywords]
	 * [-writeHybridInfoXmp]
	 * [-writeHybridInfoUserComment]
	 * [-writeGearInfo]
	 * [-deleteBackupFiles | -overwriteOriginalFiles | -overwriteOriginalFilesInPlace]
	 * [-auto]
	 */
	private static final String OPT_CONFIGDIR = "configDir";
	private static final String OPT_DATA_FILE = "dataFile";
	private static final String OPT_IMAGE_DIR = "imageDir";
	private static final String OPT_IMAGE_FILE_FORMAT = "imageFileFormat";
	private static final String OPT_WRITE_KEYWORDS = "writeKeywords";
	private static final String OPT_WRITE_HYBRID_INFO_XMP = "writeHybridInfoXmp";
	private static final String OPT_WRITE_HYBRID_INFO_USER_COMMENT = "writeHybridInfoUserComment";
	private static final String OPT_WRITE_GEAR_INFO = "writeGearInfo";
	private static final String OPT_OVERWRITE_ORIGINAL_FILES = "overwriteOriginalFiles";
	private static final String OPT_OVERWRITE_ORIGINAL_FILES_IN_PLACE = "overwriteOriginalFilesInPlace";
	private static final String OPT_AUTO = "auto";
	
	private static final String USAGE = Utils.EXIFTAGGER 
		+ " [-" + OPT_CONFIGDIR + " <fileName>] "
		+ "-" + OPT_DATA_FILE + " <fileName> "
		+ "-" + OPT_IMAGE_DIR + " <directory> "
		+ "-" + OPT_IMAGE_FILE_FORMAT + " <fileFormatExpression> "
		+ " [-" + OPT_WRITE_KEYWORDS + "]"
		+ " [-" + OPT_WRITE_HYBRID_INFO_XMP + "]"
		+ " [-" + OPT_WRITE_HYBRID_INFO_USER_COMMENT + "]"
		+ " [-" + OPT_WRITE_GEAR_INFO + "]"
		+ " [-" + OPT_OVERWRITE_ORIGINAL_FILES 
		+ " | -" + OPT_OVERWRITE_ORIGINAL_FILES_IN_PLACE + "]"
		+ " [-" + OPT_AUTO + "]";
	
	public String configDir = "./cfg";
	public String dataFile;
	public String imageDir;
	public String imageFileFormat;
	public boolean writeKeywords;
	public boolean writeHybridInfoXmp;
	public boolean writeHybridInfoUserComment;
	public boolean writeGearInfo;
	public enum WriteMode {
		BackupOriginalFiles,
		OverwriteOriginalFiles,
		OverwriteOriginalFilesInPlace
	}
	public WriteMode writeMode;
	public boolean auto;
	public Map<String, String> autoMap;
	
	@Override
	public String toString() {
		return "CmdLineParams [configDir=" + configDir + ", dataFile="
			+ dataFile + ", imageDir=" + imageDir + ", imageFileFormat="
			+ imageFileFormat + ", writeKeywords=" + writeKeywords
			+ ", writeHybridInfoXmp=" + writeHybridInfoXmp
			+ ", writeHybridInfoUserComment=" + writeHybridInfoUserComment
			+ ", writeGearInfo=" + writeGearInfo + ", writeMode="
			+ writeMode + ", auto=" + auto + ", autoMap=" + autoMap + "]";
	}

	private static final String PARAMS_CFG_FILE_NAME = "./params.cfg";
	private static String[] addParamsFromFile(String[] args) 
		throws ExifTaggerException {
		List<String> argList = new ArrayList<String>(Arrays.asList(args));
		if (new File(PARAMS_CFG_FILE_NAME).isFile()) {
			List<CSVRecord> records = CsvUtils.readCsvComplete(PARAMS_CFG_FILE_NAME);
			for (CSVRecord record : records) {
				for (int idx=0; idx < record.size(); idx++) {
					String arg = record.get(idx);
					arg = StringUtils.trimToEmpty(arg);
					if (!StringUtils.isEmpty(arg)) {
						argList.add(arg);
					}
				}
			}
			Utils.logcSep("Found '" + PARAMS_CFG_FILE_NAME + "'.");
			args = argList.toArray(new String[0]);
		}
		return args;
	}
	
	private static final String AUTO_FILE_NAME = "./auto.cfg";
	private static Map<String, String> getAutoMap() throws ExifTaggerException {
		Map<String, String> autoMap = new HashMap<String, String>();
		if (new File(AUTO_FILE_NAME).isFile()) {
			List<CSVRecord> records = CsvUtils.readCsvComplete(AUTO_FILE_NAME);
			for (CSVRecord record : records) {
				if (record.size() == 2) {
					autoMap.put(record.get(0), record.get(1));
				} 
			}
			Utils.logcSep("Found '" + AUTO_FILE_NAME + "'.");
		} else {
			Utils.logcSep("'" + AUTO_FILE_NAME + "' not found.");
		}
		return autoMap;
	}
	
	@SuppressWarnings("static-access")
	public static CmdLineParams parseCmdLineParams(String[] args) 
		throws ExifTaggerException {
		args = CmdLineParams.addParamsFromFile(args);
		CmdLineParams cmdLineParams = new CmdLineParams();
		Options options = new Options();
		options.addOption(OptionBuilder
			.isRequired(false)
			.hasArg()
			.withType(String.class)
			.withDescription("Directory of configuration files")
			.create(OPT_CONFIGDIR));
		options.addOption(OptionBuilder
			.isRequired(false)
			.hasArg()
			.withType(String.class)
			.withDescription("Filename of datafile")
			.create(OPT_DATA_FILE));
		options.addOption(OptionBuilder
			.isRequired()
			.hasArg()
			.withType(String.class)
			.withDescription("Directory of imagefiles")
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
			.hasArg(false)
			.withDescription("Run in automatic mode")
			.create(OPT_AUTO));
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
			cmdLineParams.configDir = cmd.getOptionValue(OPT_CONFIGDIR);
			if (!StringUtils.endsWith(cmdLineParams.configDir, "/")) {
				cmdLineParams.configDir += "/";
			}
			cmdLineParams.dataFile = cmd.getOptionValue(OPT_DATA_FILE);
			cmdLineParams.imageDir = cmd.getOptionValue(OPT_IMAGE_DIR);
			cmdLineParams.imageFileFormat = cmd.getOptionValue(OPT_IMAGE_FILE_FORMAT);
			cmdLineParams.writeKeywords = 
				cmd.hasOption(OPT_WRITE_KEYWORDS);
			cmdLineParams.writeHybridInfoXmp = 
				cmd.hasOption(OPT_WRITE_HYBRID_INFO_XMP);
			cmdLineParams.writeHybridInfoUserComment = 
				cmd.hasOption(OPT_WRITE_HYBRID_INFO_USER_COMMENT);
			cmdLineParams.writeGearInfo = 
				cmd.hasOption(OPT_WRITE_GEAR_INFO);
			if (cmd.hasOption(OPT_OVERWRITE_ORIGINAL_FILES)) {
				cmdLineParams.writeMode = WriteMode.OverwriteOriginalFiles;
			} else if (cmd.hasOption(OPT_OVERWRITE_ORIGINAL_FILES_IN_PLACE)) {
				cmdLineParams.writeMode = WriteMode.OverwriteOriginalFilesInPlace;
			} else {
				cmdLineParams.writeMode = WriteMode.BackupOriginalFiles;				
			}
			cmdLineParams.auto = 
				cmd.hasOption(OPT_AUTO);
			if (cmdLineParams.auto) {
				cmdLineParams.autoMap = getAutoMap();
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
