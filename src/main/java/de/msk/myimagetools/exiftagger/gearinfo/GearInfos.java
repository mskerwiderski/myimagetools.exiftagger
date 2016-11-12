package de.msk.myimagetools.exiftagger.gearinfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

import de.msk.myimagetools.exiftagger.ExifTaggerException;
import de.msk.myimagetools.exiftagger.util.csv.CsvUtils;

public class GearInfos {

	private static final String CFG_FILE_NAME_EXEC_MODES = "execmodes.cfg";
	private Map<String, GearInfoExecMode> mapExecModes = null;
	private List<GearInfoExecMode> listExecModes = null;
	
	private static final String CFG_FILE_NAME_HYBRID_PROCESSES = "hybridprocesses.cfg";
	private Map<String, GearInfoHybridProcess> mapHybridProcesses = null;
	private List<GearInfoHybridProcess> listHybridProcesses = null;
	
	private static final String CFG_FILE_NAME_ARTISTS = "artists.cfg";
	private List<GearInfoArtist> listArtists = null;
	
	private static final String CFG_FILE_NAME_LENSES = "lenses.cfg";
	private Map<String, GearInfoLens> mapLenses = null;
		
	private static final String CFG_FILE_NAME_CAMERAS = "cameras.cfg";
	private Map<String, GearInfoCamera> mapCameras = null;
	private List<GearInfoCamera> listCameras = null;
	
	private static final String CFG_FILE_NAME_DEVELOPERS = "developers.cfg";
	private List<GearInfoDeveloper> listDevelopers = null;
	
	private static final String CFG_FILE_NAME_DEVELOPMENT_PROCESSES = "devprocesses.cfg";
	private List<GearInfoDevelopmentProcess> listDevelopmentProcesses = null;
	
	private static final String CFG_FILE_NAME_DEVELOPMENT_TEMPERATURE = "devtemperatures.cfg";
	private List<GearInfoDevelopmentTemperature> listDevelopmentTemperatures = null;
	
	private static final String CFG_FILE_NAME_DEVELOPMENT_AGITATIONS = "devagitations.cfg";
	private List<GearInfoDevelopmentAgitations> listDevelopmentAgitations = null;
	
	private static final String CFG_FILE_NAME_DEVELOPMENT_HARDWARE = "devhardware.cfg";
	private List<GearInfoDevelopmentHardware> listDevelopmentHardware = null;
	
	private static final String CFG_FILE_NAME_FILMS = "films.cfg";
	private Map<String, GearInfoFilm> mapFilms = null;
	private List<GearInfoFilm> listFilms = null;
	
	private static final String CFG_FILE_NAME_INSTANT_FILMS = "instantfilms.cfg";
	private Map<String, GearInfoInstantFilm> mapInstantFilms = null;
	private List<GearInfoInstantFilm> listInstantFilms = null;
	
	private static final String CFG_FILE_NAME_FILM_FORMATS = "filmformats.cfg";
	private List<GearInfoFilmFormat> listFilmFormats = null;
	
	private static final String CFG_FILE_NAME_DEVELOPMENT_LABORATORIES = "devlaboratories.cfg";
	private List<GearInfoDevelopmentLaboratory> listDevelopmentLaboratories = null;
	
	private static final String CFG_FILE_NAME_DIGITIZING_HARDWARE = "dighardware.cfg";
	private List<GearInfoHardware> listDigitizingHardware = null;
	
	private static final String CFG_FILE_NAME_DIGITIZING_SOFTWARE = "digsoftware.cfg";
	private List<GearInfoSoftware> listDigitizingSoftware = null;
	
	private static final String CFG_FILE_NAME_TAGS = "tags.cfg";
	private Map<String, GearInfoTag> mapTags = null;
	private List<GearInfoTag> listTags = null;
	
	private String configDir;
	
	public List<GearInfoExecMode> getExecModes() {
		return this.listExecModes;
	}

	public GearInfoExecMode getExecMode(String execMode) { 
		return this.mapExecModes.get(execMode);
	}
	
	public List<GearInfoHybridProcess> getHybridProcesses() {
		return this.listHybridProcesses;
	}
	
	public GearInfoHybridProcess getHybridProcess(String hybridProcessType) { 
		return this.mapHybridProcesses.get(hybridProcessType);
	}
	
	public List<GearInfoArtist> getArtists() {
		return this.listArtists;
	}
	
	public GearInfoLens getLens(String lensId) { 
		return this.mapLenses.get(lensId);
	}
	
	public GearInfoCamera getCamera(String cameraId) { 
		return this.mapCameras.get(cameraId);
	}
	
	public List<GearInfoCamera> getCameras() {
		return this.listCameras;
	}
	
	public List<GearInfoDeveloper> getDevelopers() {
		return this.listDevelopers;
	}
	
	public List<GearInfoDevelopmentProcess> getDevelopmentProcesses() {
		return listDevelopmentProcesses;
	}

	public List<GearInfoDevelopmentTemperature> getDevelopmentTemperatures() {
		return listDevelopmentTemperatures;
	}
	
	public List<GearInfoDevelopmentAgitations> getDevelopmentAgitations() {
		return this.listDevelopmentAgitations;
	}
	
	public List<GearInfoDevelopmentHardware> getDevelopmentHardware() {
		return this.listDevelopmentHardware;
	}
	
	public String getFilmOrInstantFilmName(String filmOrInstantFilmId) {
		String result = null;
		GearInfoFilm film = this.getFilm(filmOrInstantFilmId);
		if (film != null) {
			result = film.getDesc();
		} else {
			GearInfoInstantFilm instantFilm = this.getInstantFilm(filmOrInstantFilmId);
			if (instantFilm != null) {
				result = instantFilm.getDesc();
			}
		}
		return result;
	}
	
	public GearInfoFilm getFilm(String filmId) { 
		return this.mapFilms.get(filmId);
	}
	
	public List<GearInfoFilm> getFilms() {
		return this.listFilms;
	}
	
	public GearInfoInstantFilm getInstantFilm(String instantFilmId) { 
		return this.mapInstantFilms.get(instantFilmId);
	}
	
	public List<GearInfoInstantFilm> getInstantFilms() {
		return this.listInstantFilms;
	}
	
	public List<GearInfoFilmFormat> getFilmFormats() {
		return this.listFilmFormats;
	}
	
	public List<GearInfoDevelopmentLaboratory> getDevelopmentLaboratories() {
		return this.listDevelopmentLaboratories;
	}
	
	public List<GearInfoHardware> getDigitizingHardware() {
		return this.listDigitizingHardware;
	}
	
	public List<GearInfoSoftware> getDigitizingSoftware() {
		return this.listDigitizingSoftware;
	}
	
	public List<GearInfoTag> getListTags() {
		return listTags;
	}

	public GearInfoTag getTag(String tagId) { 
		return this.mapTags.get(tagId);
	}
	
	public GearInfos(String configDir) throws ExifTaggerException {
		this.configDir = configDir;
		this.listExecModes = initList(
			GearInfoExecMode.class, 
			CFG_FILE_NAME_EXEC_MODES);
		this.mapExecModes = initMap(
			GearInfoExecMode.class, 
			CFG_FILE_NAME_EXEC_MODES);
		this.listHybridProcesses = initList(
			GearInfoHybridProcess.class, 
			CFG_FILE_NAME_HYBRID_PROCESSES);
		this.mapHybridProcesses = initMap(
			GearInfoHybridProcess.class, 
			CFG_FILE_NAME_HYBRID_PROCESSES);
		this.listArtists = initList(
			GearInfoArtist.class, 
			CFG_FILE_NAME_ARTISTS);
		this.mapLenses = initMap(
			GearInfoLens.class, 
			CFG_FILE_NAME_LENSES);
		this.mapCameras = initMap(
			GearInfoCamera.class, 
			CFG_FILE_NAME_CAMERAS);
		this.listCameras = initList(
			GearInfoCamera.class, 
			CFG_FILE_NAME_CAMERAS);
		this.listDevelopers = initList(
			GearInfoDeveloper.class, 
			CFG_FILE_NAME_DEVELOPERS);
		this.listDevelopmentProcesses = initList(
			GearInfoDevelopmentProcess.class, 
			CFG_FILE_NAME_DEVELOPMENT_PROCESSES);
		this.listDevelopmentTemperatures = initList(
			GearInfoDevelopmentTemperature.class, 
			CFG_FILE_NAME_DEVELOPMENT_TEMPERATURE);
		this.listDevelopmentAgitations = initList(
			GearInfoDevelopmentAgitations.class, 
			CFG_FILE_NAME_DEVELOPMENT_AGITATIONS);
		this.listDevelopmentHardware = initList(
			GearInfoDevelopmentHardware.class, 
			CFG_FILE_NAME_DEVELOPMENT_HARDWARE);
		this.mapFilms = initMap(
			GearInfoFilm.class, 
			CFG_FILE_NAME_FILMS);
		this.listFilms = initList(
			GearInfoFilm.class, 
			CFG_FILE_NAME_FILMS);
		this.mapInstantFilms = initMap(
			GearInfoInstantFilm.class, 
			CFG_FILE_NAME_INSTANT_FILMS);
		this.listInstantFilms = initList(
			GearInfoInstantFilm.class, 
			CFG_FILE_NAME_INSTANT_FILMS);
		this.listFilmFormats = initList(
			GearInfoFilmFormat.class, 
			CFG_FILE_NAME_FILM_FORMATS);
		this.listDevelopmentLaboratories = initList(
			GearInfoDevelopmentLaboratory.class,
			CFG_FILE_NAME_DEVELOPMENT_LABORATORIES);
		this.listDigitizingHardware = initList(
			GearInfoHardware.class, 
			CFG_FILE_NAME_DIGITIZING_HARDWARE);
		this.listDigitizingSoftware = initList(
			GearInfoSoftware.class, 
			CFG_FILE_NAME_DIGITIZING_SOFTWARE);
		this.listTags = initList(
			GearInfoTag.class, 
			CFG_FILE_NAME_TAGS);
		this.mapTags = initMap(
			GearInfoTag.class, 
			CFG_FILE_NAME_TAGS);
	}		
	
	private <T extends AbstractGearInfo> List<T> initList(
		Class<T> gearInfoClass, String cfgFileName) 
		throws ExifTaggerException {
		List<T> gearInfoList = new ArrayList<T>();
		try {
			List<CSVRecord> csvRecords = 
				CsvUtils.readCsvComplete(this.configDir + cfgFileName);
			for (int idx=1; idx < csvRecords.size(); idx++) {
				T gearInfo = gearInfoClass.newInstance();
				gearInfo.init(csvRecords.get(idx));
				gearInfoList.add(gearInfo);
			}
			Collections.sort(gearInfoList);
		} catch (Exception e) {
			throw new ExifTaggerException(e);
		}
		return gearInfoList;
	}
	
	private <T extends AbstractGearInfo> Map<String, T> initMap(
		Class<T> gearInfoClass, String cfgFileName) 
		throws ExifTaggerException {
		Map<String, T> gearInfoMap = new HashMap<String, T>();
		try {
			List<CSVRecord> csvRecords = 
				CsvUtils.readCsvComplete(this.configDir + cfgFileName);
			for (int idx=1; idx < csvRecords.size(); idx++) {
				T gearInfo = gearInfoClass.newInstance();
				gearInfo.init(csvRecords.get(idx));
				gearInfoMap.put(gearInfo.getId(), gearInfo);
			}
		} catch (Exception e) {
			throw new ExifTaggerException(e);
		}
		return gearInfoMap;
	}
}
