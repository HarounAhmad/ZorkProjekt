package ch.bbw.zork;

/*
 * author:  Michael Kolling, Version: 1.0, Date: July 1999
 * refactoring: Rinaldo Lanza, September 2020
 */

import java.util.Arrays;
import java.util.List;

/**
 * The type Command words.
 */
public class CommandWords {

	private List<String> validCommands = Arrays.asList("gehe", "beenden", "hilfe", "items", "drop");

	/**
	 * Is command boolean.
	 *
	 * @param commandWord the command word
	 * @return the boolean
	 */
	public boolean isCommand(String commandWord) {
		return validCommands.stream()
				.filter(c -> c.equals(commandWord))
				.count()>0;
	}

	/**
	 * Show all string.
	 *
	 * @return the string
	 */
	public String showAll() {
		return String.join(" ", validCommands);
	}

}






