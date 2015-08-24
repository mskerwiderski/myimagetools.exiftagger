package de.msk.myimagetools.exiftagger;

public class ExifTaggerException extends Exception {

	private static final long serialVersionUID = 9154201260162207174L;

	public ExifTaggerException() {
	}

	public ExifTaggerException(String message) {
		super(message);
	}

	public ExifTaggerException(Throwable cause) {
		super(cause);
	}

	public ExifTaggerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExifTaggerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
