package de.msk.myimagetools.exiftagger.exiftool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.msk.myimagetools.exiftagger.ExifTaggerException;
import de.msk.myimagetools.exiftagger.cli.CmdLineParams;
import de.msk.myimagetools.exiftagger.util.ExifSpecUtils;
import de.msk.myimagetools.exiftagger.util.Utils;

public class ExiftoolUtils {

	private ExiftoolUtils() {
	}

	public static final String EXIFTOOL = "exiftool";
	
	public static void deleteAllXmpMskInfos(String fileName, CmdLineParams.WriteMode writeMode) throws ExifTaggerException {
		List<String> exiftoolArgs = new ArrayList<String>();
		exiftoolArgs.add("-" + ExifSpecUtils.NAMESPACE_MSK + ":all=");
		exiftoolArgs.add("-" + ExifSpecUtils.NAMESPACE_MSK_GI + ":all=");
		exiftoolArgs.add("-" + ExifSpecUtils.NAMESPACE_MSK_HP + ":all=");
		exiftoolArgs.add(fileName);
		ExecExiftoolResult result = execExiftool(exiftoolArgs, writeMode);
		if (!result.success) {
			throw new ExifTaggerException("deleting xmp-msk infos failed: " + result);
		}
	}
	
	public static boolean exiftoolFound() {
		List<String> exiftoolArgs = new ArrayList<String>();
		exiftoolArgs.add("-ver");
		ExecExiftoolResult execExiftoolResult =
			execExiftool(exiftoolArgs, null);	
		if (execExiftoolResult.isSuccess()) {
			Utils.logcSep("Program '" + EXIFTOOL + "' with version '" + 
				execExiftoolResult.result + "' found.");
		} else {
			Utils.logcLn("Program '" + EXIFTOOL + "' not found.");
			Utils.logcLn("Make sure that '" + EXIFTOOL + "' is installed and");
			Utils.logcSep("that the path to '" + EXIFTOOL + "' is in your system path.");
			Utils.logcSep(Utils.EXIFTAGGER + " terminated.");
		}
		return execExiftoolResult.success;
	}
	
	public static class ExecExiftoolResult {
		private boolean success;
		private String result;
		private ExecExiftoolResult(boolean success, String result) {
			this.success = success;
			this.result = result;
		}
		public boolean isSuccess() {
			return success;
		}
		public String getResult() {
			return result;
		}
		@Override
		public String toString() {
			return "ExecExiftoolResult [success=" + success + ", result="
					+ result + "]";
		}
	}

	public static ExecExiftoolResult execExiftool(List<String> exiftoolArgs,
		CmdLineParams.WriteMode writeMode) {
		boolean success = false;
		String result = null;
		exiftoolArgs.add(0, EXIFTOOL);
		if (writeMode != null) {
			if (writeMode.equals(
				CmdLineParams.WriteMode.OverwriteOriginalFiles)) {
				exiftoolArgs.add("-overwrite_original");
			} else if (writeMode.equals(
				CmdLineParams.WriteMode.OverwriteOriginalFilesInPlace)) {
				exiftoolArgs.add("-overwrite_original_in_place");
			}
		}
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(exiftoolArgs);
		Process process;
		try {
			process = processBuilder.start();
			while (process.isAlive()) {
				Thread.sleep(300);
			}
			success = (process.exitValue() == 0);
			result = new BufferedReader(new InputStreamReader(
				process.getInputStream())).readLine();
		} catch (Exception e) {
			success = false;
			result = e.getMessage();
		} 
		return new ExecExiftoolResult(success, result);
	}
}
