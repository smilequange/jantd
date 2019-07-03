package cn.jantd.core.exception;

public class JantdBootException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JantdBootException(String message){
		super(message);
	}

	public JantdBootException(Throwable cause)
	{
		super(cause);
	}

	public JantdBootException(String message, Throwable cause)
	{
		super(message,cause);
	}
}
