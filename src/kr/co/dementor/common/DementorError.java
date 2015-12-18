package kr.co.dementor.common;

public class DementorError
{
	// encoding 실패
	static public final int ERROR_ENCODING_UNKONWN = 201000;
	static public final int ERROR_ENCODING_INVALID_KEY = ERROR_ENCODING_UNKONWN + 1;
	static public final int ERROR_ENCODING_UNSUPPORTED_ENCODING = ERROR_ENCODING_INVALID_KEY + 1;
	static public final int ERROR_ENCODING_NO_SUCH_PADDING = ERROR_ENCODING_UNSUPPORTED_ENCODING + 1;
	static public final int ERROR_ENCODING_NO_SUCH_ALGORITHM = ERROR_ENCODING_NO_SUCH_PADDING + 1;
	static public final int ERROR_ENCODING_INVALID_ALGORITHM_PARAM = ERROR_ENCODING_NO_SUCH_ALGORITHM + 1;
	static public final int ERROR_ENCODING_ILLEGAL_BLOCK_SIZE = ERROR_ENCODING_INVALID_ALGORITHM_PARAM + 1;
	static public final int ERROR_ENCODING_BAD_PADDING = ERROR_ENCODING_ILLEGAL_BLOCK_SIZE + 1;

	// decoding 실패
	static public final int ERROR_DECODING_UNKONWN = 201100;
	static public final int ERROR_DECODING_INVALID_KEY = ERROR_DECODING_UNKONWN + 1;
	static public final int ERROR_DECODING_UNSUPPORTED_ENCODING = ERROR_DECODING_INVALID_KEY + 1;
	static public final int ERROR_DECODING_NO_SUCH_PADDING = ERROR_DECODING_UNSUPPORTED_ENCODING + 1;
	static public final int ERROR_DECODING_NO_SUCH_ALGORITHM = ERROR_DECODING_NO_SUCH_PADDING + 1;
	static public final int ERROR_DECODING_INVALID_ALGORITHM_PARAM = ERROR_DECODING_NO_SUCH_ALGORITHM + 1;
	static public final int ERROR_DECODING_ILLEGAL_BLOCK_SIZE = ERROR_DECODING_INVALID_ALGORITHM_PARAM + 1;
	static public final int ERROR_DECODING_BAD_PADDING = ERROR_DECODING_ILLEGAL_BLOCK_SIZE + 1;	
	
}
