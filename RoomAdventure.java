package Java_Room_Adventure;

import java.util.Scanner; // Import scanner for reading user input

public class RoomAdventure { // Main class containing game logic

    // class variables
    private static Room currentRoom; // The room the player is currently in
    private static String[] inventory = {null, null, null, null, null}; // Player inventory slots
    private static String status; // Message to display after each action

    // constants
    final private static String DEFAULT_STATUS = 
        "Sorry, I do not understand.  Try [verb] [noun].  Valid verbs include 'go', 'look', and 'take'."; //Default error message
    
    
    
    private static void handleGo(String noun) { // Handles moving between rooms
        String[] exitDirections = currentRoom.getExitDrections(); // Get available directions
        Room[] exitDestinations = currentRoom.getExitDestinations(); // Get rooms in those directions
        status = "I don't see that room."; // Default if direciton is not found
        for (int i = 0; i < exitDirections.length; i++) { // Loop through directions
            if (noun.equals(exitDirections[i])) { // If user direction matches
                currentRoom = exitDestinations[i]; // Change current room
                status = "Changed Room"; // Update status
            }
        }
    }

    private static void handleLook(String noun) { // Handles inspection items
        String[] items = currentRoom.getItems(); // visible items in current room
        String[] itemDescriptions = currentRoom.getItemDescriptions(); // Descriptions for each item
        status = "I don't see that item"; // Default if Item is not found
        for (int i = 0; i < items.length; i++) { // Loop through items
            if ( noun.equals(items[i])) { // If user-noun matches an item
                status = itemDescriptions[i]; // Set status to tiem description
            }
        }
    }

    private static void handleTake(String noun) { // Handles picking up items
        String[] grabbables = currentRoom.getGrabbables(); // Itms that can be taken
        status = "I can't grab that item."; // Default if not grabbable
        for (String item : grabbables) { // Loop through grabbable items
            if (noun.equals(item)) { // if user-noun matches grabbable
                for (int j = 0; j < inventory.length; j++) { // Find empty inventory slot
                    if (inventory[j] == null) { // If slot is empty
                        inventory[j] = noun; // Add item to inventory
                        status = "Added it to the inventory"; //Update status
                        break; // Exit inventory loop
                    }
                }
            }
        }
    }

    private static void setupGame() { // Initializes game world
        Room room1 = new Room("Room 1"); // Create Room 1
        Room room2 = new Room("Room 2"); // Create Room 2

        String[] room1ExitDirections = {"east"}; // Room 1 exits
        Room[] room1ExitDestinations = {room2}; // Destination rooms for Room 1
        String[] room1Items = {"Chair", "desk"}; // Items in Room 1
        String[] room1ItemDescriptions = { // Descriptions for Room 1 items
            "It is a chiar",
            "It's a desk, there is a key on it."
        };
        String[] room1Grabbables = {"key"}; // Items you can take in Room 1
        room1.setExitDirections(room1ExitDirections); // Set exits
        room1.setExitDestinations(room1ExitDestinations); // Set exit destinations
        room1.setItems(room1Items); // Set visible items
        room1.setItemDescriptions(room1ItemDescriptions); // Set item descriptions
        room1.setGrabbables(room1Grabbables); // set grabbable items

        String[] room2ExitDirections = {"west"}; // Room 1 exits
        Room[] room2ExitDestinations = {room1}; // Destination rooms for Room 1
        String[] room2Items = {"fireplace", "rug"}; // Items in Room 1
        String[] room2ItemDescriptions = { // Descriptions for Room 1 items
            "It's on fire'",
            "There is a lump of coal on the rug."
        };
        String[] room2Grabbables = {"coal"}; // Items you can take in Room 1
        room2.setExitDirections(room2ExitDirections); // Set exits
        room2.setExitDestinations(room2ExitDestinations); // Set exit destinations
        room2.setItems(room2Items); // Set visible items
        room2.setItemDescriptions(room2ItemDescriptions); // Set item descriptions
        room2.setGrabbables(room2Grabbables); // set grabbable items

        currentRoom = room1; // Start game in Room 1
    }


    public static void main(String[] args) { // Entry point of the program
        setupGame(); // Initialize rooms, items, and starting room

        while (true) { //Game loop, runs until program is terminated
            System.out.print(currentRoom.toString()); // Display curretn room description
            System.out.print("inventory: "); // Prompt for inventory display

            for (int i = 0; i < inventory.length; i++) { // Loop through inventory slots
                System.out.print(inventory[i] + " "); // Print each inventory item
            }

            System.out.println("\nWhat would you like to do? "); // Prompt user for next action

            Scanner s = new Scanner(System.in); // Creeate Scanner to read input
            String input = s.nextLine(); // read entire line of input
            String[] words = input.split(" "); // Split input into words

            if (words.length != 2) { // Check for proper two-word command
                status = DEFAULT_STATUS; // Set status to error message 
                continue; // Skip to next loop iteration
            }

            String verb = words[0]; // First word is the actino verb
            String noun = words[1]; // Second word is the target noun

            switch (verb) { // Decide which action to take
                case "go": // if verb si 'go'
                    handleGo(noun); // Move to another room
                    break;
                case "look": // If verb is 'look'
                    handleLook(noun); // Describe an item
                    break;
                case "take": // If verb is 'take'
                    handleTake(noun); // Pick up an item
                    break;
                default: // If verb is unrecognized
                    status = DEFAULT_STATUS; // Set status to error message
            }

            System.out.println(status); // Print the status message
        }
    }


}







class Room { // Represents a game room
    private String name; // Room name
    private String[] exitDirections; // Directions you can go
    private Room[] exitDestinations; // Rooms reached by each direction
    private String[] items; // Items visible in the room
    private String[] itemDescriptions; // Descriptions for those items
    private String[] grabbables; // Items you can take

    public Room(String name) { // Constructor
        this.name = name; // Set the room's name
    }

    public String[] getExitDrections() { // getter for exit directions
        return exitDirections;
    }

    public void setExitDirections(String[] exitDirections){ // Setter for exit directions
        this.exitDirections = exitDirections;
    }

    public Room[] getExitDestinations(){ // getter for exit distinations
        return exitDestinations;
    }

    public void setExitDestinations(Room[] exitDestinations){ // setter for exit destinations
        this.exitDestinations = exitDestinations;
    }

    public String[] getItems(){ // getter for items
        return items;
    }

    public void setItems(String[] items){ // setter for items
        this.items = items;
    }

    public String[] getItemDescriptions(){ // getter for item descriptions
        return itemDescriptions;
    }

    public void setItemDescriptions(String[] itemDescriptions){ // setter for item descriptions
        this.itemDescriptions = itemDescriptions;
    }

    public String[] getGrabbables(){ // getter for grabbables
        return grabbables;
    }

    public void setGrabbables(String[] grabbables){ // setter for grabbables
        this.grabbables = grabbables;
    }



    public String toString() {// Customm print for the room
        String result = "\nLocation: " + name; // Show room name
        result += "\nYou See: "; //List items
        for (String item : items) { // Loop items
            result += item + " "; // Append each item
        }
        result += "\nExits: "; // List exits
        for (String direction : exitDirections) { // Loop exits
            result += direction + " "; // Append each direction
        }
        return result + "\n"; // Return full description
    }
}
