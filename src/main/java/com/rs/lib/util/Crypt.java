package com.rs.lib.util;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.google.gson.JsonIOException;
import com.rs.lib.file.JsonFileManager;

public class Crypt {
	
	private static byte[] SALT = new byte[16];
	
	private static void loadSalt() {
		File file = new File("./data/salt.json");
		try {
			if (!file.exists()) {
				SecureRandom random = new SecureRandom();
				random.nextBytes(SALT);
				JsonFileManager.saveJsonFile(SALT, file);
			} else {
				SALT = JsonFileManager.loadJsonFile(new File("./data/salt.json"), byte[].class);
			}
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] encrypt(String password) {
		try {
			if (SALT == null)
				loadSalt();
			KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT, 65536, 128);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return factory.generateSecret(spec).getEncoded();
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean compare(byte[] encrypted, String unencrypted) {
		return Arrays.equals(encrypted, encrypt(unencrypted));
	}
}
