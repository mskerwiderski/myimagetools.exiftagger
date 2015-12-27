package myimagetools.exiftagger;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import de.msk.myimagetools.exiftagger.Main;
import de.msk.myimagetools.exiftagger.util.Utils;

public class TestNikonF6Parser {

	private static final String TEST_DIR = "src/test/resources/";
	
	private static final String[] ARGS = new String[] {
		"-exiftool", "/usr/local/bin/exiftool",
		"-configDir", TEST_DIR + "cfg",
		"-dataFile", TEST_DIR + "datafiles/datafile-nikon-f6.txt",
		"-imageDir", TEST_DIR + "images",
		"-imageFileFormat", "20[0-9]{2}_f[0-9]{3}_img([0-9]{2}).jpg",
		"-writeKeywords",
		"-writeHybridInfoXmp",
		"-writeHybridInfoUserComment",
		"-writeGearInfo",
		"-overwriteOriginalFilesInPlace",
		"-autoFile", TEST_DIR + "datafiles/auto-nikon-f6.cfg",
	};
	
	@Test
	public void testCompleteWorkflow() throws Exception{
		Utils.logcSep("Testing Nikon F6 Parser");
		Utils.logc("Setting up test directory ... ");		
		FileUtils.cleanDirectory(new File(TEST_DIR + "images"));
		FileUtils.copyDirectory(new File(TEST_DIR + "images_org"), new File(TEST_DIR + "images"));
		Utils.logcSep("done.");
		Main.main(ARGS);
		Utils.logcSep("Testing Nikon F6 Parser - DONE.");
	}

}
