package de.msk.myimagetools.exiftagger.cli;

import org.apache.commons.lang3.StringUtils;

import de.msk.myimagetools.exiftagger.ExifTaggerException;
import de.msk.myimagetools.exiftagger.ExifTaggerProps;
import de.msk.myimagetools.exiftagger.domain.CameraAndFilmDataRecord;
import de.msk.myimagetools.exiftagger.domain.HybridProcessRecord;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoHybridProcess;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfoHybridProcess.HybridProcessType;
import de.msk.myimagetools.exiftagger.gearinfo.GearInfos;
import de.msk.myimagetools.exiftagger.util.Utils;

public class CmdLineReaderHybridProcessRecord {
	public static HybridProcessRecord readHybridProcessRecord(
		CmdLineParams cmdLineParams, ExifTaggerProps props, GearInfos gearInfos, 
		CameraAndFilmDataRecord cameraAndFilmDataRecord) 
		throws ExifTaggerException {
		GearInfoHybridProcess hybridProcess = CmdLineReaderUtils.getHybridProcess(cmdLineParams, gearInfos);
		HybridProcessRecord hybridProcessRecord = new HybridProcessRecord();
		hybridProcessRecord.setProcessType(HybridProcessType.valueOf(hybridProcess.getId()));
		
		if (hybridProcessRecord.getProcessType().isGetArtist() &&
			(cameraAndFilmDataRecord.getArtist() == null)) {
			cameraAndFilmDataRecord.setArtist(
				CmdLineReaderUtils.getArtist(
					cmdLineParams, gearInfos));
		}
		if (hybridProcessRecord.getProcessType().isGetCamera() &&
			(cameraAndFilmDataRecord.getCamera() == null)) {
			cameraAndFilmDataRecord.setCamera(
				CmdLineReaderUtils.getCamera(
					cmdLineParams, gearInfos, true));
		}
		if (hybridProcessRecord.getProcessType().isGetFilmSpeed() &&
			StringUtils.isEmpty(cameraAndFilmDataRecord.getFilmSpeed())) {
			cameraAndFilmDataRecord.setFilmSpeed(
				CmdLineReaderUtils.getFilmSpeed(cmdLineParams));
		}
		if (hybridProcessRecord.getProcessType().isGetRoleId()) {
			hybridProcessRecord.setRollId(
				CmdLineReaderUtils.getRollId(cmdLineParams, props));
		}
		if (hybridProcessRecord.getProcessType().isGetDateOfDevelopment()) {
			hybridProcessRecord.setDateOfDevelopment(
				CmdLineReaderUtils.getDateOfDevelopment(cmdLineParams, props));
		}	
		if (hybridProcessRecord.getProcessType().isGetFilmFormat()) {
			hybridProcessRecord.setFilmFormat(
				CmdLineReaderUtils.getFilmFormat(cmdLineParams, gearInfos));
		}
		if (hybridProcessRecord.getProcessType().isGetFilmName()) {
			if (StringUtils.isEmpty(cameraAndFilmDataRecord.getFilmName())) {
				hybridProcessRecord.setFilmName(CmdLineReaderUtils.getFilm(cmdLineParams, gearInfos));
			} else {
				hybridProcessRecord.setFilmName(cameraAndFilmDataRecord.getFilmName());
			}
		}
		if (hybridProcessRecord.getProcessType().isGetInstantFilmName()) {
			if (StringUtils.isEmpty(cameraAndFilmDataRecord.getFilmName())) {
				hybridProcessRecord.setFilmName(CmdLineReaderUtils.getInstantFilm(cmdLineParams, gearInfos));
			} else {
				hybridProcessRecord.setFilmName(cameraAndFilmDataRecord.getFilmName());
			}
		}
		if (hybridProcessRecord.getProcessType().isGetFilmExpirationDate()) {
			hybridProcessRecord.setFilmExpirationDate(CmdLineReaderUtils.getFilmExpirationDate(cmdLineParams, props));
		}
		if (hybridProcessRecord.getProcessType().isGetDevelopmentPullPushFstops()) {
			hybridProcessRecord.setDevelopmentPullPushFstops(CmdLineReaderUtils.getDevelopmentPullPushFstops(cmdLineParams));
		}
		if (hybridProcessRecord.getProcessType().isGetDevelopmentLaboratory()) {
			hybridProcessRecord.setDevelopmentLaboratory(CmdLineReaderUtils.getDevelopmentLaboratory(cmdLineParams, gearInfos));
		}
		if (hybridProcessRecord.getProcessType().isGetDigitizingHardware()) {
			hybridProcessRecord.setDigitizingHardware(CmdLineReaderUtils.getDigitizingHardware(cmdLineParams, gearInfos));
		}
		if (hybridProcessRecord.getProcessType().isGetDigitizingSoftware()) {
			hybridProcessRecord.setDigitizingSoftware(CmdLineReaderUtils.getDigitizingSoftware(cmdLineParams, gearInfos));
		}
		if (hybridProcessRecord.getProcessType().isGetAdditionalInfo()) {
			hybridProcessRecord.setAdditionalInfo(CmdLineReaderUtils.getAdditionalInfo(cmdLineParams));
		}
		if (hybridProcessRecord.getProcessType().isGetDeveloperName()) {		
			hybridProcessRecord.setDeveloperName(CmdLineReaderUtils.getDeveloper(cmdLineParams, gearInfos));
		}
		if (hybridProcessRecord.getProcessType().isGetDeveloperDilution()) {
			hybridProcessRecord.setDeveloperDilution(CmdLineReaderUtils.getDeveloperDilution(cmdLineParams));
		}
		if (hybridProcessRecord.getProcessType().isGetDevelopmentProcess()) {
			hybridProcessRecord.setDevelopmentProcess(CmdLineReaderUtils.getDevelopmentProcess(cmdLineParams, gearInfos));
		}
		if (hybridProcessRecord.getProcessType().isGetDevelopmentTemperature()) {
			hybridProcessRecord.setDevelopmentTemperature(CmdLineReaderUtils.getDevelopmentTemperature(cmdLineParams, gearInfos));
		}
		if (hybridProcessRecord.getProcessType().isGetDevelopmentTime()) {
			hybridProcessRecord.setDevelopmentTime(CmdLineReaderUtils.getDevelopmentTime(cmdLineParams, 1));
		}
		if (hybridProcessRecord.getProcessType().isGetDevelopmentTime2()) {
			hybridProcessRecord.setDevelopmentTime2(CmdLineReaderUtils.getDevelopmentTime(cmdLineParams, 2));
		}
		if (hybridProcessRecord.getProcessType().isGetDevelopmentAgitations()) {
			hybridProcessRecord.setDevelopmentAgitations(CmdLineReaderUtils.getDevelopmentAgitation(cmdLineParams, gearInfos));
		}
		if (hybridProcessRecord.getProcessType().isGetDevelopmentHardware()) {
			hybridProcessRecord.setDevelopmentHardware(CmdLineReaderUtils.getDevelopmentHardware(cmdLineParams, gearInfos));
		}
		
		if (hybridProcessRecord.getProcessType().equals(HybridProcessType.FilmLabNegative) ||
			hybridProcessRecord.getProcessType().equals(HybridProcessType.FilmLabSlide)) {
			StringBuffer buf = new StringBuffer()
			.append("Hybrid Process (Analog Film Development and Digitizing)" + Utils.LF)
			.append("-------------------------------------------------------" + Utils.LF)
			.append("Process Type: " + gearInfos.getHybridProcess(hybridProcessRecord.getProcessType().toString()).getDesc() + Utils.LF)
			.append("Roll Id: " + hybridProcessRecord.getRollId() + Utils.LF)
			.append("Date Of Development: " + hybridProcessRecord.getDateOfDevelopment() + Utils.LF)
			.append("Film Format: " + hybridProcessRecord.getFilmFormat() + Utils.LF)
			.append("Film Name: " + hybridProcessRecord.getFilmName() + Utils.LF)
			.append("Film Expiration Date: " + hybridProcessRecord.getFilmExpirationDate() + Utils.LF)
			.append("Development Pull Push Fstops: " + hybridProcessRecord.getDevelopmentPullPushFstops() + Utils.LF)
			.append("Development Laboratory: " + hybridProcessRecord.getDevelopmentLaboratory() + Utils.LF)
			.append("Digitizing Hardware: " + hybridProcessRecord.getDigitizingHardware() + Utils.LF)
			.append("Digitizing Software: " + hybridProcessRecord.getDigitizingSoftware() + Utils.LF)
			.append("Additional Info: " + hybridProcessRecord.getAdditionalInfo() + Utils.LF);
			hybridProcessRecord.setUserComment(buf.toString());
		} else if (hybridProcessRecord.getProcessType().equals(HybridProcessType.InstantFilm) ||
			hybridProcessRecord.getProcessType().equals(HybridProcessType.InstantFilmBleachedNegative)) {
			StringBuffer buf = new StringBuffer()
			.append("Hybrid Process (Analog Film Development and Digitizing)" + Utils.LF)
			.append("-------------------------------------------------------" + Utils.LF)
			.append("Process Type: " + gearInfos.getHybridProcess(hybridProcessRecord.getProcessType().toString()).getDesc() + Utils.LF)
			.append("Film Format: " + hybridProcessRecord.getFilmFormat() + Utils.LF)
			.append("Film Name: " + hybridProcessRecord.getFilmName() + Utils.LF)
			.append("Film Expiration Date: " + hybridProcessRecord.getFilmExpirationDate() + Utils.LF)
			.append("Digitizing Hardware: " + hybridProcessRecord.getDigitizingHardware() + Utils.LF)
			.append("Digitizing Software: " + hybridProcessRecord.getDigitizingSoftware() + Utils.LF)
			.append("Additional Info: " + hybridProcessRecord.getAdditionalInfo() + Utils.LF);
			hybridProcessRecord.setUserComment(buf.toString());
		} else if (hybridProcessRecord.getProcessType().equals(HybridProcessType.FilmSelfNegative) ||
				hybridProcessRecord.getProcessType().equals(HybridProcessType.FilmSelfSlide)) {
			StringBuffer buf = new StringBuffer()
			.append("Hybrid Process (Analog Film Development and Digitizing)" + Utils.LF)
			.append("-------------------------------------------------------" + Utils.LF)
			.append("Process Type: " + gearInfos.getHybridProcess(hybridProcessRecord.getProcessType().toString()).getDesc() + Utils.LF)
			.append("Roll Id: " + hybridProcessRecord.getRollId() + Utils.LF)
			.append("Date Of Development: " + hybridProcessRecord.getDateOfDevelopment() + Utils.LF)
			.append("Film Format: " + hybridProcessRecord.getFilmFormat() + Utils.LF)
			.append("Film Name: " + hybridProcessRecord.getFilmName() + Utils.LF)
			.append("Film Expiration Date: " + hybridProcessRecord.getFilmExpirationDate() + Utils.LF)
			.append("Developer Name: " + hybridProcessRecord.getDeveloperName() + Utils.LF)
			.append("Developer Dilution: " + hybridProcessRecord.getDeveloperDilution() + Utils.LF)
			.append("Development Temperature: " + hybridProcessRecord.getDevelopmentTemperature() + Utils.LF)
			.append("Development Process: " + hybridProcessRecord.getDevelopmentProcess() + Utils.LF)
			.append("Development Time: " + hybridProcessRecord.getDevelopmentTime() + Utils.LF);
			if (hybridProcessRecord.getProcessType().equals(HybridProcessType.FilmSelfSlide)) {
				buf.append("Development Time 2: " + hybridProcessRecord.getDevelopmentTime2() + Utils.LF);
			}
			buf
			.append("Development Agitations: " + hybridProcessRecord.getDevelopmentAgitations() + Utils.LF)
			.append("Development Pull Push Fstops: " + hybridProcessRecord.getDevelopmentPullPushFstops() + Utils.LF)
			.append("Development Hardware: " + hybridProcessRecord.getDevelopmentHardware() + Utils.LF)
			.append("Digitizing Hardware: " + hybridProcessRecord.getDigitizingHardware() + Utils.LF)
			.append("Digitizing Software: " + hybridProcessRecord.getDigitizingSoftware() + Utils.LF)
			.append("Additional Info: " + hybridProcessRecord.getAdditionalInfo() + Utils.LF);
			hybridProcessRecord.setUserComment(buf.toString());
		} else if (hybridProcessRecord.getProcessType().equals(HybridProcessType.DigitalOnly)) {
			// nothing to do here.
		} else {
			throw new ExifTaggerException("unknown hybrid process type.");
		}
		return hybridProcessRecord;
	}
}
