package com.apiIc.api.entities.enums;

import java.text.Normalizer;

public enum Sexo {
	
	homem,
	mulher,
	outro;

	public static Sexo parse(String input) {
		if (input == null) return null;
		String s = normalize(input);
		// direct enum names
		for (Sexo v : values()) {
			if (v.name().equalsIgnoreCase(s)) return v;
		}
		// aliases
		switch (s) {
			case "m":
			case "masculino":
			case "male":
			case "homem":
				return homem;
			case "f":
			case "feminino":
			case "female":
			case "mulher":
				return mulher;
			case "o":
			case "outro":
			case "outros":
			case "other":
			case "nao-binario":
			case "nao_binario":
			case "naobinario":
			case "nb":
				return outro;
			default:
				return null;
		}
	}

	private static String normalize(String s) {
		String lower = s.trim().toLowerCase();
		String noAccents = Normalizer.normalize(lower, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		return noAccents;
	}
}
