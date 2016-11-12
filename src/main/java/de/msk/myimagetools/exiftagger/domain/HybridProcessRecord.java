package de.msk.myimagetools.exiftagger.domain;

import java.io.Serializable;

import de.msk.myimagetools.exiftagger.gearinfo.GearInfoHybridProcess.HybridProcessType;


public class HybridProcessRecord implements Serializable {

	private static final long serialVersionUID = -7812144865706806884L;

	private HybridProcessType processType; // FilmSelfNegative
	private String rollId; // 2015-f020
	private String dateOfDevelopment; // 2015:10:15
	private String filmFormat; // MF-120-6x6
	private String filmName; // ILFORD HP5 PLUS
	private String filmExpirationDate; // 1996:08
	private String developerName; // ILFORD MICROPHEN
	private String developerDilution; // 1+1
	private String developmentTemperature; // 20 degree celsius
	private String developmentTime; // 10:00
	private String developmentTime2; // 10:00
	private String developmentProcess; // Inversion
	private String developmentAgitations; // 30 seconds at the beginning, then 5 times every 30 seconds
	private String developmentPullPushFstops; // Push +2
	private String developmentHardware; // Heiland TAS Filmprocessor
	private String developmentLaboratory;
	private String digitizingHardware; // Epson Perfection V850 Pro
	private String digitizingSoftware; // VueScan v9.5.06, PhotoLine v18.51, ColorPerfect v2.16
	private String additionalInfo; // developed perfectly
	private String userComment;

	public HybridProcessType getProcessType() {
		return processType;
	}

	public void setProcessType(HybridProcessType processType) {
		this.processType = processType;
	}

	public String getRollId() {
		return rollId;
	}
	
	public void setRollId(String rollId) {
		this.rollId = rollId;
	}
	
	public String getDateOfDevelopment() {
		return dateOfDevelopment;
	}
	
	public void setDateOfDevelopment(String dateOfDevelopment) {
		this.dateOfDevelopment = dateOfDevelopment;
	}
	
	public String getFilmFormat() {
		return filmFormat;
	}
	
	public void setFilmFormat(String filmFormat) {
		this.filmFormat = filmFormat;
	}
	
	public String getFilmName() {
		return filmName;
	}
	
	public void setFilmName(String filmName) {
		this.filmName = filmName;
	}
	
	public String getFilmExpirationDate() {
		return filmExpirationDate;
	}

	public void setFilmExpirationDate(String filmExpirationDate) {
		this.filmExpirationDate = filmExpirationDate;
	}

	public String getDeveloperName() {
		return developerName;
	}
	
	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}
	
	public String getDeveloperDilution() {
		return developerDilution;
	}
	
	public void setDeveloperDilution(String developerDilution) {
		this.developerDilution = developerDilution;
	}
	
	public String getDevelopmentTemperature() {
		return developmentTemperature;
	}

	public void setDevelopmentTemperature(String developmentTemperature) {
		this.developmentTemperature = developmentTemperature;
	}

	public String getDevelopmentTime() {
		return developmentTime;
	}
	
	public void setDevelopmentTime(String developmentTime) {
		this.developmentTime = developmentTime;
	}
	
	public String getDevelopmentTime2() {
		return developmentTime2;
	}

	public void setDevelopmentTime2(String developmentTime2) {
		this.developmentTime2 = developmentTime2;
	}

	public String getDevelopmentProcess() {
		return developmentProcess;
	}
	
	public void setDevelopmentProcess(String developmentProcess) {
		this.developmentProcess = developmentProcess;
	}
	
	public String getDevelopmentAgitations() {
		return developmentAgitations;
	}

	public void setDevelopmentAgitations(String developmentAgitations) {
		this.developmentAgitations = developmentAgitations;
	}

	public String getDevelopmentPullPushFstops() {
		return developmentPullPushFstops;
	}

	public void setDevelopmentPullPushFstops(String developmentPullPushFstops) {
		this.developmentPullPushFstops = developmentPullPushFstops;
	}

	public String getDevelopmentHardware() {
		return developmentHardware;
	}

	public void setDevelopmentHardware(String developmentHardware) {
		this.developmentHardware = developmentHardware;
	}

	public String getDevelopmentLaboratory() {
		return developmentLaboratory;
	}

	public void setDevelopmentLaboratory(String developmentLaboratory) {
		this.developmentLaboratory = developmentLaboratory;
	}

	public String getDigitizingHardware() {
		return digitizingHardware;
	}
	
	public void setDigitizingHardware(String digitizingHardware) {
		this.digitizingHardware = digitizingHardware;
	}
	
	public String getDigitizingSoftware() {
		return digitizingSoftware;
	}
	
	public void setDigitizingSoftware(String digitizingSoftware) {
		this.digitizingSoftware = digitizingSoftware;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

	@Override
	public String toString() {
		return "HybridProcessRecord [processType=" + processType + ", rollId="
			+ rollId + ", dateOfDevelopment=" + dateOfDevelopment
			+ ", filmFormat=" + filmFormat + ", filmName=" + filmName
			+ ", filmExpirationDate=" + filmExpirationDate
			+ ", developerName=" + developerName + ", developerDilution="
			+ developerDilution + ", developmentTemperature="
			+ developmentTemperature + ", developmentTime="
			+ developmentTime + ", developmentTime2=" + developmentTime2
			+ ", developmentProcess=" + developmentProcess
			+ ", developmentAgitations=" + developmentAgitations
			+ ", developmentPullPushFstops=" + developmentPullPushFstops
			+ ", developmentHardware=" + developmentHardware
			+ ", developmentLaboratory=" + developmentLaboratory
			+ ", digitizingHardware=" + digitizingHardware
			+ ", digitizingSoftware=" + digitizingSoftware
			+ ", additionalInfo=" + additionalInfo + ", userComment="
			+ userComment + "]";
	}
}
