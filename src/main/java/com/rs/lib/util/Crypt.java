// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Copyright © 2021 Trenton Kress
//  This file is part of project: Darkan
//
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
