package de.msk.myimagetools.exiftagger.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.msk.myimagetools.exiftagger.util.Utils;

public class JavaUtils {

	private JavaUtils() {
	}

	public static final String JAVA = "java";
	
	public static boolean javaRuntimeFound() {
		List<String> javaArgs = new ArrayList<String>();
		javaArgs.add("-version");
		ExecJavaResult execJavaResult = execJava(javaArgs);
		
		if (execJavaResult.isSuccess()) {
			String version = StringUtils.substringBetween(execJavaResult.result, "\"");
			Utils.logcSep("Program '" + JAVA + "' with version '" + 
				version + "' found.");
		} else {
			Utils.logcLn("Program '" + JAVA + "' not found.");
			Utils.logcLn("Make sure that '" + JAVA + "' is installed and");
			Utils.logcSep("that the path to '" + JAVA + "' is in your system path.");
			Utils.logcSep(Utils.EXIFTAGGER + " terminated.");
		}
		return execJavaResult.success;
	}
	
	public static class ExecJavaResult {
		private boolean success;
		private String result;
		private ExecJavaResult(boolean success, String result) {
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
			return "ExecJavaResult [success=" + success + ", result="
					+ result + "]";
		}
	}
	
	public static ExecJavaResult execJava(List<String> exiftoolArgs) {
		boolean success = false;
		String result = null;
		exiftoolArgs.add(0, JAVA);
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
				process.getErrorStream())).readLine();
		} catch (Exception e) {
			success = false;
			result = e.getMessage();
		} 
		return new ExecJavaResult(success, result);
	}
}
