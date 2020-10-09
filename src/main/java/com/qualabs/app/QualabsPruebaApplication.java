package com.qualabs.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import com.qualabs.app.model.User;
import com.qualabs.app.utils.FileUtils;

@SpringBootApplication
public class QualabsPruebaApplication {

	private static final String DIR = "C:/Users/rmachado/Desktop/pruebatecnica";
	private static final String SINGLE_QUOTE = "'";
	private static final String OPEN_PARENTHESIS = "{";
	private static final String CLOSE_PARENTHESIS = "}";
	private static final String TWO_POINTS = ":";
	private static final String OPEN_PARENTHESIS_STRAIGHT = "[";
	private static final String CLOSE_PARENTHESIS_STRAIGHT = "]";
	private static final String COMA = ",";
	private static final String LINE_BREAK = "\n";
	private static final String AUTH_MODULE = "auth_module";
	private static final String CONTENT_MODULE = "content_module";


	public static void main(String[] args) {
		List<User> users = FileUtils.loadUserListFromFiles(DIR);
		HashMap<String, List<String>> authModuleMap = loadModules(users, AUTH_MODULE);
		HashMap<String, List<String>> contentModuleMap = loadModules(users, CONTENT_MODULE);
		partA(users, authModuleMap, contentModuleMap);
		partB(authModuleMap, contentModuleMap);

	}
	
	private static void partA(List<User> users, HashMap<String, List<String>> authModuleMap, HashMap<String, List<String>> contentModuleMap) {
		StringBuilder result = new StringBuilder();
		result.append(OPEN_PARENTHESIS).append(LINE_BREAK);
		result.append(SINGLE_QUOTE).append(AUTH_MODULE).append(SINGLE_QUOTE).append(TWO_POINTS).append(LINE_BREAK);
		result.append(printModulesWithUsers(authModuleMap));
		result.append(SINGLE_QUOTE).append(CONTENT_MODULE).append(SINGLE_QUOTE).append(TWO_POINTS).append(LINE_BREAK);
		result.append(printModulesWithUsers(contentModuleMap));
		result.append(CLOSE_PARENTHESIS);
		System.out.println(result.toString());
	}
	
	private static void partB(HashMap<String, List<String>> authModuleMap, HashMap<String, List<String>> contentModuleMap) {
		HashMap<String, String> result = new HashMap<String, String>(); 
		for (Map.Entry<String, List<String>> entryAuthModule : authModuleMap.entrySet()) {
			String keyAuth = entryAuthModule.getKey();
			List<String> usersKeyAuth = entryAuthModule.getValue();
			for (String userAuth : usersKeyAuth) {
				String keyContent = getUserProvider(userAuth, contentModuleMap, result);
				if(!StringUtils.isEmpty(keyContent) && !result.containsKey(keyContent)) {
					result.put(keyAuth, userAuth);
					result.put(keyContent, userAuth);
					break;
				}
			}
		}
		printResultPartB(result);
	}
	
	private static String getUserProvider(String user, HashMap<String, List<String>> contentModuleMap, HashMap<String, String> result) {
		for (Map.Entry<String, List<String>> entryContentModule : contentModuleMap.entrySet()) {
			String keyContent = entryContentModule.getKey();
			if(result.containsKey(keyContent)) {
				continue;
			}
			List<String> usersKeyContent = entryContentModule.getValue();
			for(String userContent : usersKeyContent) {
				if(user.equals(userContent)) {
					return keyContent;
				}
			}
		}
		return null;
	}
	
	private static void printResultPartB(HashMap<String, String> map) {
		Set<String> users = new HashSet<String>();
		map.values().forEach(user -> {
			users.add(user);
		});
		StringBuilder result = new StringBuilder();
		result.append(OPEN_PARENTHESIS_STRAIGHT);
		users.forEach( user -> {
			result.append(SINGLE_QUOTE).append(user).append(SINGLE_QUOTE).append(COMA);
		});
		result.deleteCharAt(result.length() -1);
		result.append(CLOSE_PARENTHESIS_STRAIGHT);
		System.out.println(result.toString());
	}
	

	private static HashMap<String, List<String>> loadModules(List<User> users, String moduleType) {
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		String moduleUser;
		for (User user : users) {
			if (moduleType.equals(AUTH_MODULE)) {
				moduleUser = user.getProvider().getAuthModule();
			} else {
				moduleUser = user.getProvider().getContentModule();
			}
			if (StringUtils.isEmpty(moduleUser)) {
				continue;
			}
			if (!map.containsKey(moduleUser)) {
				List<String> usersByModule = new ArrayList<String>();
				usersByModule.add(user.getName());
				map.put(moduleUser, usersByModule);
			} else {
				map.get(moduleUser).add(user.getName());
			}
		}
		return map;
	}

	private static String printModulesWithUsers(HashMap<String, List<String>> map) {
		StringBuilder result = new StringBuilder();
		int keyCount = 0;
		result.append(OPEN_PARENTHESIS).append(LINE_BREAK);
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			String key = entry.getKey();
			keyCount++;
			result.append(SINGLE_QUOTE).append(key).append(SINGLE_QUOTE).append(TWO_POINTS);
			result.append(OPEN_PARENTHESIS_STRAIGHT);
			List<String> usersKey = entry.getValue();
			for (int i = 0; i < usersKey.size(); i++) {
				result.append(SINGLE_QUOTE).append(usersKey.get(i)).append(SINGLE_QUOTE);
				if (i < usersKey.size() -1) {
					result.append(COMA);
				}
			}
			result.append(CLOSE_PARENTHESIS_STRAIGHT);
			if (keyCount < map.size()) {
				result.append(COMA);
			}
			result.append(LINE_BREAK);
		}
		result.append(CLOSE_PARENTHESIS).append(LINE_BREAK);
		return result.toString();
	}
}
