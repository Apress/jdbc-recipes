package jcb.util;

import java.io.*;

/**
 *
 * This utility class provides methods for closing InputStream
 * and FileOutputStream objects. Also, this class provides
 * some utility methods for converting InputStream into a
 * String object.
 *
 *
 * <p>
 * Date: October 2, 2002
 * <p>
 * @author Mahmoud Parsian
 * <p>
 * @since 1.4
 * <p>
 */
public class IOUtil {

	/**
	 * Default character set for creating strings
	 */
	private static final String DEFAULT_CHARACTER_SET = "UTF-8";

	/**
	 * Size of the buffer to read files
	 */
    private static final int BUFFER_SIZE = 1024;

  /**
   * This method closes a given InputStream
   * @param in InputStream to close
   */
  	public static void close(InputStream in) {
		try {
			if (in != null) {
				in.close();
			}
		}
		catch (Exception e) {
			//ignore
		}
	}

  /**
   * This method closes a given FileOutputStream
   * @param out FileOutputStream to close
   */
  	public static void close(FileOutputStream out) {
		try {
			if (out != null) {
				out.close();
			}
		}
		catch (Exception e) {
			//ignore
		}
	}

  /**
   * This method closes a given ByteArrayOutputStream
   * @param out ByteArrayOutputStream to close
   */
  	public static void close(ByteArrayOutputStream out) {
		try {
			if (out != null) {
				out.close();
			}
		}
		catch (Exception e) {
			//ignore
		}
	}

  /**
   * This method reads all the data from a given InputStream
   * and returns that data in a byte array (byte[]).
   *
   * @param input InputStream to convert to a byte[]
   * @return byte[] - converted byte array
   * @throws IOException Failed to convert InputStream to a byte[]
   */
   public static byte[] inputStreamToByteArray(final InputStream input)
   		throws IOException {

		if (input == null) {
			return null;
		}

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		int length;
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			while ((length = input.read(buffer)) != -1) {
			  output.write(buffer, 0, length);
			}
		}
		finally {
			close(output);
		}

		return output.toByteArray();
  }


  /**
   * This method converts the data read from an InputStream
   * to a java.lang.String object.
   *
   * @param input InputStream to convert to String
   * @return String - resulting from conversion of InputStream
   * @throws IOException Failed to read InputStream
   */
  public static String inputStreamToString(final InputStream input)
    throws IOException {
    String result = null;
    if (input != null) {
      result = new String(inputStreamToByteArray(input), DEFAULT_CHARACTER_SET);
    }
    return result;
  }


}
