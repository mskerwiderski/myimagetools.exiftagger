package myimagetools.exiftagger;

import static org.junit.Assert.*;

import org.junit.Test;

import de.msk.myimagetools.exiftagger.ExifTaggerException;
import de.msk.myimagetools.exiftagger.cli.CmdLineParams;
import de.msk.myimagetools.exiftagger.cli.CmdLineParams.WriteMode;

public class TestParseCmdLineParams {

	private static final String TEST_DIR = "src/test/resources/";
	private static final String PARAMS_FILE = TEST_DIR + "params.cfg";
	
	private static final String[] ARGS = new String[] {
		// added to 'params.cfg': -exiftool usr/local/bin/exiftool
		// added to 'params.cfg': -configDir src/test/resources/cfg
		"-dataFile", TEST_DIR + "datafile.txt",
		"-imageDir", TEST_DIR + "images",
		"-imageFileFormat", "20[0-9]{2}_f[0-9]{3}_img([0-9]{2}).tif",
		"-writeKeywords",
		"-writeHybridInfoXmp",
		"-writeHybridInfoUserComment",
		// added to 'params.cfg': -writeGearInfo
		"-overwriteOriginalFilesInPlace",
		"-autoFile", TEST_DIR + "auto.cfg",
	};
	
	@Test
	public void testAllParams() {
		CmdLineParams cmdLineParams = null;
		try {
			cmdLineParams = CmdLineParams.parseCmdLineParams(ARGS, PARAMS_FILE);
			assertEquals("/usr/local/bin/exiftool", cmdLineParams.exiftool);
			assertEquals(TEST_DIR + "cfg/", cmdLineParams.configDir);
			assertEquals(TEST_DIR + "datafile.txt", cmdLineParams.dataFile);
			assertEquals(TEST_DIR + "images/", cmdLineParams.imageDir);
			assertEquals("20[0-9]{2}_f[0-9]{3}_img([0-9]{2}).tif", cmdLineParams.imageFileFormat);
			assertTrue(cmdLineParams.writeKeywords);
			assertTrue(cmdLineParams.writeHybridInfoXmp);
			assertTrue(cmdLineParams.writeHybridInfoUserComment);
			assertTrue(cmdLineParams.writeGearInfo);
			assertEquals(WriteMode.OverwriteOriginalFilesInPlace, cmdLineParams.writeMode);
			assertEquals("src/test/resources/auto.cfg", cmdLineParams.autoFile);
		} catch (ExifTaggerException e) {
			e.printStackTrace();
		}
	}

}
