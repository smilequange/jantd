package cn.jantd.core.exception;

/**
 * @Description
 * @Author 圈哥
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2019/7/2
 */
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
