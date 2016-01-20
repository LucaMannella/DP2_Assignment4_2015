package it.polito.dp2.WF.lab4;

public class ServiceUnavailableException extends Exception {

	public ServiceUnavailableException() {
	}

	public ServiceUnavailableException(String arg0) {
		super(arg0);
	}

	public ServiceUnavailableException(Throwable arg0) {
		super(arg0);
	}

	public ServiceUnavailableException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ServiceUnavailableException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
