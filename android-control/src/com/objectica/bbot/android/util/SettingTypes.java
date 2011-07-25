package com.objectica.bbot.android.util;

public enum SettingTypes {
	BBOT_APP_NAME("com.objectica.bbot.android.appName"),
	BBOT_HOSTNAME("com.objectica.bbot.android.bbotHostUrl");
	private String value;
	
	private SettingTypes(String name) {
		this.value = name;
	}

	public String getValue() {
		return value;
	}

}
