package de.msk.myimagetools.exiftagger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.lang3.StringUtils;

import de.msk.myimagetools.exiftagger.cli.CmdLineParams;
import de.msk.myimagetools.exiftagger.cli.CmdLineReaderHybridProcessRecord;
import de.msk.myimagetools.exiftagger.cli.CmdLineReaderUtils;
import de.msk.myimagetools.exiftagger.domain.CameraAndFilmDataRecord;
import de.msk.myimagetools.exiftagger.domain.HybridProcessRecord;
import de.msk.myimagetools.exiftagger.exiftool.ExiftoolUtils;
import de.msk.myimagetools.exiftagger.exiftool.ExiftoolWriter;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoCamera;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoExecMode;
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
					boolean done = false;
					ExifTaggerProps props = ExifTaggerProps.load(cmdLineParams.configDir);
					GearInfos gearInfos = new GearInfos(cmdLineParams.configDir);
					if (new File(cmdLineParams.imageDir + FILENAME_EXIFTAGGER_DUMP).isFile()) {
						CameraAndFilmDataRecord cameraAndFilmDataRecord = loadFilmData(cmdLineParams);
						String msg = "Found existing Exiftagger film data:" + Utils.LF;
						GearInfoCamera camera = cameraAndFilmDataRecord.getCamera();
						msg += "Camera: " +
							(camera != null ?
								cameraAndFilmDataRecord.getCamera().getMakeAndModel() :
								"unknown") + Utils.LF;
						HybridProcessRecord hybridProcessRecord = cameraAndFilmDataRecord.getHybridProcessRecord();
						if (hybridProcessRecord != null) {
							msg += "Roll Id: " + 
								(!StringUtils.isEmpty(hybridProcessRecord.getRollId()) ? 
									hybridProcessRecord.getRollId() : 
									Utils.UNKNOWN) + Utils.LF;
							msg += "Film: " + 
								(!StringUtils.isEmpty(hybridProcessRecord.getFilmName()) ?  
									hybridProcessRecord.getFilmName() : 
									Utils.UNKNOWN) + "@" + 
								(!StringUtils.isEmpty(cameraAndFilmDataRecord.getFilmSpeed()) ?  
									cameraAndFilmDataRecord.getFilmSpeed() : 
									Utils.UNKNOWN) +	
								Utils.LF;
						}
						Utils.logcLn(msg);
						if (CmdLineReaderUtils.getYesNo(cmdLineParams, 
							"Do you want to load and use found film data now?")) {
							ExiftoolWriter.writeFilmData(cmdLineParams, props, 
								gearInfos, cameraAndFilmDataRecord);
							done = true;	
						} 
					} 
					
					if (!done) {
						CameraAndFilmDataRecord cameraAndFilmDataRecord = 
							DataFileParserUtils.process(cmdLineParams, gearInfos);
						cameraAndFilmDataRecord.setHybridProcessRecord(
							CmdLineReaderHybridProcessRecord.readHybridProcessRecord(
								cmdLineParams, props, gearInfos, cameraAndFilmDataRecord));
						GearInfoExecMode execMode =
							CmdLineReaderUtils.getExecMode(cmdLineParams, gearInfos);
						boolean save = false;
						boolean write = false;
						if (StringUtils.equals(execMode.getId(), GearInfoExecMode.execModeType.Save.toString())) {
							save = true;
						} else if (StringUtils.equals(execMode.getId(), GearInfoExecMode.execModeType.Write.toString())) {
							write = true;
						} else if (StringUtils.equals(execMode.getId(), GearInfoExecMode.execModeType.SaveAndWrite.toString())) {
							save = true;
							write = true;
						} else {
							throw new ExifTaggerException("unknown exec mode. please check config file.");
						}
						if (save) {
							saveFilmData(cmdLineParams, cameraAndFilmDataRecord);
						}
						if (write) {
							ExiftoolWriter.writeFilmData(cmdLineParams, props, 
								gearInfos, cameraAndFilmDataRecord);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Utils.logcSep(e.getMessage());
				Utils.logcSep(Utils.EXIFTAGGER + " terminated.");
			}
		}
	}
	
	public static final String FILENAME_EXIFTAGGER_DUMP = "exiftagger_dump.ser";
	
	public static void saveFilmData(CmdLineParams cmdLineParams, 
		CameraAndFilmDataRecord cameraAndFilmDataRecord) 
			throws ExifTaggerException {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		Utils.logc("saving film data ... ");
		try {
			fout = new FileOutputStream(
				cmdLineParams.imageDir + FILENAME_EXIFTAGGER_DUMP);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(cameraAndFilmDataRecord);
		} catch (Exception e){
			throw new ExifTaggerException(e);
		} finally {
			try {
				oos.close();
				fout.close();	
			} catch (IOException e) {
				throw new ExifTaggerException(e);
			}
		}
		Utils.logcLn("done.");
	}
	
	public static CameraAndFilmDataRecord loadFilmData(CmdLineParams cmdLineParams) 
		throws ExifTaggerException {
		CameraAndFilmDataRecord cameraAndFilmDataRecord = null;
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		Utils.logc("loading film data ... ");
		try {
			fin = new FileInputStream(
				cmdLineParams.imageDir + FILENAME_EXIFTAGGER_DUMP);
			ois = new ObjectInputStream(fin);
			cameraAndFilmDataRecord = (CameraAndFilmDataRecord)ois.readObject();
		} catch (Exception e){
			throw new ExifTaggerException(e);
		} finally {
			try {
				ois.close();
				fin.close();	
			} catch (IOException e) {
				throw new ExifTaggerException(e);
			}
		}
		Utils.logcLn("done.");
		return cameraAndFilmDataRecord;
	}
}
