package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.List;

public class GearInfoHybridProcess extends AbstractGearInfo {

	private static final long serialVersionUID = 5769135246353065343L;

	public enum HybridProcessType {
		FilmSelfNegative(           
			true, true, true, true, true, true, true, false, true, true, false, true, true, true, true, true, true, true, true, false, true, true),
		FilmSelfSlide(
			true, true, true, true, true, true, true, false, true, true, false, true, true, true, true, true, true, true, true, true, true, true),
		FilmLabNegative(
			true, true, true, true, true, true, true, false, true, true, true, true, true, true, false, false, false, false, false, false, false, false),
		FilmLabSlide(
			true, true, true, true, true, true, true, false, true, true, true, true, true, true, false, false, false, false, false, false, false, false),
		InstantFilm(
			true, true, true, false, false, true, false, true, true, false, false, true, true, true, false, false, false, false, false, false, false, false),
		InstantFilmBleachedNegative(
			true, true, true, false, false, true, false, true, true, false, false, true, true, true, false, false, false, false, false, false, false, false),
		DigitalOnly(
			true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
		
		private boolean getArtist;
		private boolean getCamera;
		private boolean getFilmSpeed;
		private boolean getRoleId;
		private boolean getDateOfDevelopment;
		private boolean getFilmFormat;
		private boolean getFilmName;
		private boolean getInstantFilmName;
		private boolean getFilmExpirationDate;
		private boolean getDevelopmentPullPushFstops;
		private boolean getDevelopmentLaboratory;
		private boolean getDigitizingHardware;
		private boolean getDigitizingSoftware;
		private boolean getAdditionalInfo;
		private boolean getDeveloperName;
		private boolean getDeveloperDilution;
		private boolean getDevelopmentProcess;
		private boolean getDevelopmentTemperature;
		private boolean getDevelopmentTime;
		private boolean getDevelopmentTime2;
		private boolean getDevelopmentAgitations;
		private boolean getDevelopmentHardware;
		
		private HybridProcessType(boolean getArtist, boolean getCamera,
			boolean getFilmSpeed, boolean getRoleId,
			boolean getDateOfDevelopment, boolean getFilmFormat,
			boolean getFilmName, boolean getInstantFilmName, 
			boolean getFilmExpirationDate,
			boolean getDevelopmentPullPushFstops,
			boolean getDevelopmentLaboratory,
			boolean getDigitizingHardware, boolean getDigitizingSoftware,
			boolean getAdditionalInfo, boolean getDeveloperName,
			boolean getDeveloperDilution, boolean getDevelopmentProcess,
			boolean getDevelopmentTemperature, boolean getDevelopmentTime,
			boolean getDevelopmentTime2, boolean getDevelopmentAgitations,
			boolean getDevelopmentHardware) {
			this.getArtist = getArtist;
			this.getCamera = getCamera;
			this.getFilmSpeed = getFilmSpeed;
			this.getRoleId = getRoleId;
			this.getDateOfDevelopment = getDateOfDevelopment;
			this.getFilmFormat = getFilmFormat;
			this.getFilmName = getFilmName;
			this.getInstantFilmName = getInstantFilmName;
			this.getFilmExpirationDate = getFilmExpirationDate;
			this.getDevelopmentPullPushFstops = getDevelopmentPullPushFstops;
			this.getDevelopmentLaboratory = getDevelopmentLaboratory;
			this.getDigitizingHardware = getDigitizingHardware;
			this.getDigitizingSoftware = getDigitizingSoftware;
			this.getAdditionalInfo = getAdditionalInfo;
			this.getDeveloperName = getDeveloperName;
			this.getDeveloperDilution = getDeveloperDilution;
			this.getDevelopmentProcess = getDevelopmentProcess;
			this.getDevelopmentTemperature = getDevelopmentTemperature;
			this.getDevelopmentTime = getDevelopmentTime;
			this.getDevelopmentTime2 = getDevelopmentTime2;
			this.getDevelopmentAgitations = getDevelopmentAgitations;
			this.getDevelopmentHardware = getDevelopmentHardware;
		}
		
		public boolean isGetArtist() {
			return getArtist;
		}
		public boolean isGetCamera() {
			return getCamera;
		}
		public boolean isGetFilmSpeed() {
			return getFilmSpeed;
		}
		public boolean isGetRoleId() {
			return getRoleId;
		}
		public boolean isGetDateOfDevelopment() {
			return getDateOfDevelopment;
		}
		public boolean isGetFilmFormat() {
			return getFilmFormat;
		}
		public boolean isGetFilmName() {
			return getFilmName;
		}
		public boolean isGetInstantFilmName() {
			return getInstantFilmName;
		}
		public boolean isGetFilmExpirationDate() {
			return getFilmExpirationDate;
		}
		public boolean isGetDevelopmentPullPushFstops() {
			return getDevelopmentPullPushFstops;
		}
		public boolean isGetDevelopmentLaboratory() {
			return getDevelopmentLaboratory;
		}
		public boolean isGetDigitizingHardware() {
			return getDigitizingHardware;
		}
		public boolean isGetDigitizingSoftware() {
			return getDigitizingSoftware;
		}
		public boolean isGetAdditionalInfo() {
			return getAdditionalInfo;
		}
		public boolean isGetDeveloperName() {
			return getDeveloperName;
		}
		public boolean isGetDeveloperDilution() {
			return getDeveloperDilution;
		}
		public boolean isGetDevelopmentProcess() {
			return getDevelopmentProcess;
		}
		public boolean isGetDevelopmentTemperature() {
			return getDevelopmentTemperature;
		}
		public boolean isGetDevelopmentTime() {
			return getDevelopmentTime;
		}
		public boolean isGetDevelopmentTime2() {
			return getDevelopmentTime2;
		}
		public boolean isGetDevelopmentAgitations() {
			return getDevelopmentAgitations;
		}
		public boolean isGetDevelopmentHardware() {
			return getDevelopmentHardware;
		}
	}
	
	private String hybridProcessId;
	private String hybridProcessDesc;
	
	public GearInfoHybridProcess() {
		
	}

	@Override
	public void initAux(List<String> record, int startIdx) {
		this.hybridProcessId = record.get(startIdx++);
		this.hybridProcessDesc = record.get(startIdx++);
	}

	@Override
	public int getColCnt() {
		return 2;
	}

	@Override
	public String getId() {
		return this.hybridProcessId;
	}

	public String getDesc() {
		return this.hybridProcessDesc;
	}

	@Override
	public String toString() {
		return "GearInfoHybridProcess [hybridProcessId=" + hybridProcessId
			+ ", hybridProcessDesc=" + hybridProcessDesc + "]";
	}
}
