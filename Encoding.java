package itea.web07;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encoding {
	private static final String SALT = "Emerald";

	public static String sha256Encoding(String inputData) {

		String result = null;

		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256"); //bicrypt подключить
			messageDigest.update(StandardCharsets.UTF_8.encode(inputData+SALT));
			result = String.format("%064x", new BigInteger(messageDigest.digest()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return result;
	}
}
