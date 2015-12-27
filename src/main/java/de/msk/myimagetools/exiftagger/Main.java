package de.msk.myimagetools.exiftagger;

import de.msk.myimagetools.exiftagger.cli.CmdLineParams;
import de.msk.myimagetools.exiftagger.cli.CmdLineReaderHybridProcessRecord;
import de.msk.myimagetools.exiftagger.domain.CameraAndFilmDataRecord;
import de.msk.myimagetools.exiftagger.exiftool.ExiftoolUtils;
import de.msk.myimagetools.exiftagger.exiftool.ExiftoolWriter;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfos;
import de.msk.myimagetools.exiftagger.java.JavaUtils;
import de.msk.myimagetools.exiftagger.parser.DataFileParserUtils;
import de.msk.myimagetools.exiftagger.util.Utils;

public class Main {
	public static void main(String[] args) {
		Utils.logcSep();
		Utils.logcLn(Utils.EXIFTAGGER + " " + Utils.EXIFTAGGER_VERSION);
		Utils.logcSep(Utils.EXIFTAGGER_COPYRIGHT);
		if (JavaUtils.javaRuntimeFound()) {
			try {
				CmdLineParams cmdLineParams = CmdLineParams.parseCmdLineParams(args, "./params.cfg");
				if ((cmdLineParams != null) && ExiftoolUtils.exiftoolFound(cmdLineParams.exiftool)) {
					ExifTaggerProps props = ExifTaggerProps.load(cmdLineParams.configDir);
					GearInfos gearInfos = new GearInfos(cmdLineParams.configDir);
					CameraAndFilmDataRecord cameraAndFilmDataRecord = 
						DataFileParserUtils.process(cmdLineParams, gearInfos);
					cameraAndFilmDataRecord.setHybridProcessRecord(
						CmdLineReaderHybridProcessRecord.readHybridProcessRecord(
							cmdLineParams, props, gearInfos, cameraAndFilmDataRecord));
					ExiftoolWriter.writeFilmData(cmdLineParams, props, 
						gearInfos, cameraAndFilmDataRecord);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Utils.logcSep(e.getMessage());
				Utils.logcSep(Utils.EXIFTAGGER + " terminated.");
			}
		}
	}
}
