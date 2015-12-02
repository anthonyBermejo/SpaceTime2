package org.soen387.ser.name;

import java.io.IOException;

public class NameFactory {
	static NameGenerator gen = null;
	
	public static synchronized String getName() throws IOException {
		if(gen == null) {

			gen = new NameGenerator(NameGenerator.class.getClassLoader().getResource("syllablefile.txt").getFile().replace("%20", " "));
		}
		return gen.compose(2) + " " + gen.compose(3); 
	}
}
