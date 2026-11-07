package potatoxf.helper.api.lang;

import java.io.InputStream;

/**
 * @author potatoxf
 * @date 2022/7/16
 */
public interface SecretService {

  byte[] encrypt(byte[] data, byte[] key) throws SecretException;

  byte[] decrypt(byte[] data, byte[] key) throws SecretException;

  InputStream encrypt(InputStream data, byte[] key) throws SecretException;

  InputStream decrypt(InputStream data, byte[] key) throws SecretException;
}
