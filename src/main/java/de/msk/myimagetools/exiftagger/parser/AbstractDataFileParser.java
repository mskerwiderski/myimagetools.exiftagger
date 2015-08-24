package de.msk.myimagetools.exiftagger.parser;

import java.util.HashMap;
import java.util.Map;

import de.msk.myimagetools.exiftagger.ExifTaggerException;
import de.msk.myimagetools.exiftagger.cli.CmdLineParams;
import de.msk.myimagetools.exiftagger.cli.CmdLineReaderUtils;
import de.msk.myimagetools.exiftagger.domain.CameraAndFilmDataRecord;
import de.msk.myimagetools.exiftagger.domain.ImageDataRecord;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoArtist;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfos;
import de.msk.myimagetools.exiftagger.util.csv.AbstractCsvProcessor;

public abstract class AbstractDataFileParser extends AbstractCsvProcessor {
	
	private CmdLineParams cmdLineParams;
	private GearInfos gearInfos;
	private CameraAndFilmDataRecord cameraAndFilmDataRecord;
	
	public AbstractDataFileParser(
		CmdLineParams cmdLineParams, GearInfos gearInfos) {
		this.cmdLineParams = cmdLineParams;
		this.gearInfos = gearInfos;
		this.cameraAndFilmDataRecord = new CameraAndFilmDataRecord();
		Map<Integer, ImageDataRecord> imageDataRecordsMap = 
			new HashMap<Integer, ImageDataRecord>();
		cameraAndFilmDataRecord.setImageDataRecordsMap(imageDataRecordsMap);
	}

	public void init() 
		throws ExifTaggerException {
	}
	
	public void postProcess() 
		throws ExifTaggerException {
	}
	
	public CameraAndFilmDataRecord process()  
		throws ExifTaggerException {
		this.init();
		GearInfoArtist artist = CmdLineReaderUtils.getArtist(cmdLineParams, gearInfos);
		this.cameraAndFilmDataRecord.setArtist(artist);
		this.processCsvDataFile(cmdLineParams.dataFile);
		this.postProcess();
		return this.cameraAndFilmDataRecord;
	}
	
	public CmdLineParams getCmdLineParams() {
		return cmdLineParams;
	}

	public GearInfos getGearInfos() {
		return gearInfos;
	}

	public CameraAndFilmDataRecord getCameraAndFilmDataRecord() {
		return cameraAndFilmDataRecord;
	}
}
