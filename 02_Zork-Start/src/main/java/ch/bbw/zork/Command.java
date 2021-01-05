package ch.bbw.zork;
/**
 * Class Command - Part of the "Zork" game.
 * <p>
 * author: Michael Kolling version: 1.0 date: July 1999
 * refactoring: Rinaldo Lanza, September 2020
 */
public class Command {

	private String commandWord;
	private String secondWord;
	private String thirdWord;

	/**
	 * Instantiates a new Command.
	 *
	 * @param commandWord the command word
	 */
	public Command(String commandWord) {
		this(commandWord, null, null);
	}

	/**
	 * Instantiates a new Command.
	 *
	 * @param commandWord the command word
	 * @param secondWord  the second word
	 * @param thirdWord   the third word
	 */
	public Command(String commandWord, String secondWord, String thirdWord) {
		this.commandWord = commandWord;
		this.secondWord = secondWord;
		this.thirdWord = thirdWord;
	}

	/**
	 * Gets command word.
	 *
	 * @return the command word
	 */
	public String getCommandWord() {
		return commandWord;
	}

	/**
	 * Gets second word.
	 *
	 * @return the second word
	 */
	public String getSecondWord() {
		return secondWord;
	}

	/**
	 * Gets third word.
	 *
	 * @return the third word
	 */
	public String getThirdWord() {
		return thirdWord;
	}

	/**
	 * Is unknown boolean.
	 *
	 * @return the boolean
	 */
	public boolean isUnknown() {
		return (commandWord == null);
	}

	/**
	 * Has second word boolean.
	 *
	 * @return the boolean
	 */
	public boolean hasSecondWord() {
		return (secondWord != null);
	}
}
