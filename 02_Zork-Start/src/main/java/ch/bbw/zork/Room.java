package ch.bbw.zork;
/**
 * Author:  Michael Kolling, Version: 1.1, Date: August 2000
 * refactoring: Rinaldo Lanza, September 2020
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Room.
 */
public class Room {
	
	private String description;
	/**
	 * The Room name.
	 */
	public String RoomName;
	private HashMap<String, Room> exits;

	/**
	 * Instantiates a new Room.
	 *
	 * @param description the description
	 * @param roomName    the room name
	 */
	public Room(String description, String roomName) {
		this.description = description;
		this.RoomName = roomName;
		this.exits = new HashMap<>();
	}

	/**
	 * Sets exits.
	 *
	 * @param norden the norden
	 * @param osten  the osten
	 * @param sueden the sueden
	 * @param westen the westen
	 */
	public void setExits(Room norden, Room osten, Room sueden, Room westen) {
		exits.put("norden", norden);
		exits.put("osten", osten);
		exits.put("sueden", sueden);
		exits.put("westen", westen);
	}

	/**
	 * Short description string.
	 *
	 * @return the string
	 */
	public String shortDescription() {
		return description;
	}

	/**
	 * Long description string.
	 *
	 * @return the string
	 */
	public String longDescription() {
		StringBuilder stringBuilder = new StringBuilder("Du bist in " + description + ".\n");
		stringBuilder.append(exitString());
		return stringBuilder.toString();
	}

	private String exitString() {
		return "Exits:" + String.join(" ", exits.keySet());
	}

	/**
	 * Next room room.
	 *
	 * @param direction the direction
	 * @return the room
	 */
	public Room nextRoom(String direction) {
		return exits.get(direction);
	}

}
