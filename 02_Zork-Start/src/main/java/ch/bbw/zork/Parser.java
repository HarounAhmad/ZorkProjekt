package ch.bbw.zork;
/*
 * author:  Michael Kolling, Version: 1.0, Date: July 1999
 * refactoring: Rinaldo Lanza, September 2020
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * The type Parser.
 */
public class Parser {

	private CommandWords validCommandWords;
	private InputStream inputStream;

	/**
	 * Instantiates a new Parser.
	 *
	 * @param inputStream the input stream
	 */
	public Parser(InputStream inputStream) {
		this.inputStream = inputStream;
		this.validCommandWords = new CommandWords();
	}

	/**
	 * Gets command.
	 *
	 * @return the command
	 */
	public Command getCommand() {
		String inputLine;
		String word1;
		String word2;

		System.out.print("> ");

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));
		try {
			inputLine = bufferedReader.readLine();

			String[] tokens = inputLine.split(" ");
			switch(tokens.length) {
				case 2:
					if (validCommandWords.isCommand(tokens[0])) {
						return new Command(tokens[0], tokens[1], null);
					} else if(validCommandWords.isCommand(tokens[1])){
						// TODO: refactor this
						return new Command(null, tokens[1], null);
					}
					else {
						return new Command(null, null, tokens[2]);
					}
						
				case 1:
					if (validCommandWords.isCommand(tokens[0])) {
						return new Command(tokens[0]);
					} else {
						// TODO: refactor this
						return new Command(null);
					}
				default:
					// TODO: handle this error with an exception
					break;
			}
		} catch (java.io.IOException exc) {
			System.out.println("Error beim lesen: " + exc.getMessage());
		}
		// TODO: handle error
		return new Command(null);
	}

	/**
	 * Show commands string.
	 *
	 * @return the string
	 */
	public String showCommands() {
		return validCommandWords.showAll();
	}
}
