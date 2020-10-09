package com.qualabs.app.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.qualabs.app.model.User;

public class FileUtils {

	public static List<User> loadUserListFromFiles(String directory){
		List<String> files = getFiles(directory);
		List<User> users = new ArrayList<User>();
		User user = null;
		for (String file : files) {
			String jsonContent = readFile(directory, file);
			user = parseJson(jsonContent);
			if(user != null) {
				users.add(user);
			}
		}
		return users;
	}
	
	private static User parseJson(String fileContent) {
		Gson gson = new Gson();
		User user = null;
		try {
			user = gson.fromJson(fileContent, User.class);
		} catch (Exception e) {
			System.err.println("Error al parsear User");
		}
		return user;
	}

	private static String readFile(String directory,String fileName) {
		StringBuilder content = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(directory.concat("/").concat(fileName)))) {
			stream.forEach(line -> {
				content.append(line);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}

	

	private static List<String> getFiles(String directory) {
		DirectoryStream<Path> stream = null;
		List<String> files = new ArrayList<String>();
		try {
			stream = Files.newDirectoryStream(Paths.get(directory));
			for (Path path : stream) {
				if (!Files.isDirectory(path)) {
					files.add(path.getFileName().toString());
				}
			}
		} catch (Exception e) {
			System.err.println("Error obteniendo los files del directorio: ".concat(directory));
		}
		return files;
	}

}
