import java.util.Scanner; // Import Scanner for reading user input

public class RoomAdventure { // Main class containing game logic

    // class variables
    private static Room currentRoom; // The room the player is currently in
    private static String[] inventory = {null, null, null, null, null}; // Player inventory slots
    private static String status; // Message to display after each action

    // constants
    final private static String DEFAULT_STATUS =
        "Sorry, I do not understand. Try [verb] [noun]. Valid verbs include 'go', 'look', 'eat' and 'take'."; // Default error message



    private static void handleGo(String noun) { // Handles moving between rooms
        String[] exitDirections = currentRoom.getExitDirections(); // Get available directions
        Room[] exitDestinations = currentRoom.getExitDestinations(); // Get rooms in those directions
        status = "I don't see that room."; // Default if direction not found
        for (int i = 0; i < exitDirections.length; i++) { // Loop through directions
            if (noun.equals(exitDirections[i])) { // If user direction matches
                currentRoom = exitDestinations[i]; // Change current room
                status = "Changed Room"; // Update status
            }
        }
    }

    private static void handleLook(String noun) { // Handles inspecting items
        String[] items = currentRoom.getItems(); // Visible items in current room
        String[] itemDescriptions = currentRoom.getItemDescriptions(); // Descriptions for each item
        status = "I don't see that item."; // Default if item not found
        for (int i = 0; i < items.length; i++) { // Loop through items
            if (noun.equals(items[i])) { // If user-noun matches an item
                status = itemDescriptions[i]; // Set status to item description
            }
        }
    }

    private static void handleTake(String noun) { // Handles picking up items
        String[] grabbables = currentRoom.getGrabbables(); // Items that can be taken
        status = "I can't grab that."; // Default if not grabbable
        for (String item : grabbables) { // Loop through grabbable items
            if (noun.equals(item)) { // If user-noun matches grabbable
                for (int j = 0; j < inventory.length; j++) { // Find empty inventory slot
                    if (inventory[j] == null) { // If slot is empty
                        inventory[j] = noun; // Add item to inventory
                        status = "Added it to the inventory"; // Update status
                        break; // Exit inventory loop
                    }
                }
            }
        }
    }

    private static void handleEat(String noun) { // Handles using items
        String[] edibleItems = currentRoom.getEdibleItems();
        status = "I can't eat that.";
        for (String item : edibleItems) {
            if (noun.equals(item)) {
                for (int j = 0; j < inventory.length; j++) {
                    if (inventory[j] == edibleItems[j]) {
                        inventory[j] = null;
                        status = "Item was eaten and removed from inventory";
                        break;
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
        String[] room1Items = {"chair", "desk"}; // Items in Room 1
        String[] room1ItemDescriptions = { // Descriptions for Room 1 items
            "It is a chair",
            "It's a desk, there is a key on it."
        };
        String[] room1Grabbables = {"key"}; // Items you can take in Room 1
        String[] room1EdibleItems = {null};
        room1.setExitDirections(room1ExitDirections); // Set exits
        room1.setExitDestinations(room1ExitDestinations); // Set exit destinations
        room1.setItems(room1Items); // Set visible items
        room1.setItemDescriptions(room1ItemDescriptions); // Set item descriptions
        room1.setGrabbables(room1Grabbables); // Set grabbable items
        room1.setEdibleItems(room1EdibleItems);

        String[] room2ExitDirections = {"west"}; // Room 2 exits
        Room[] room2ExitDestinations = {room1}; // Destination rooms for Room 2
        String[] room2Items = {"fireplace", "rug"}; // Items in Room 2
        String[] room2ItemDescriptions = { // Descriptions for Room 2 items
            "It's on fire",
            "There is a lump of coal on the rug."
        };
        String[] room2Grabbables = {"coal"}; // Items you can take in Room 2
        String[] room2EdibleItems = {null}; // CHANGE IF Edible Item is Added
        room2.setExitDirections(room2ExitDirections); // Set exits
        room2.setExitDestinations(room2ExitDestinations); // Set exit destinations
        room2.setItems(room2Items); // Set visible items
        room2.setItemDescriptions(room2ItemDescriptions); // Set item descriptions
        room2.setGrabbables(room2Grabbables); // Set grabbable items
        room2.setEdibleItems(room2EdibleItems);

        currentRoom = room1; // Start game in Room 1
    }
    
    @SuppressWarnings("java:S2189")
    public static void main(String[] args) { // Entry point of the program
        setupGame(); // Initialize rooms, items, and starting room

        while (true) { // Game loop, runs until program is terminated
            System.out.print(currentRoom.toString()); // Display current room description
            System.out.print("Inventory: "); // Prompt for inventory display

            for (int i = 0; i < inventory.length; i++) { // Loop through inventory slots
                System.out.print(inventory[i] + " "); // Print each inventory item
            }

            System.out.println("\nWhat would you like to do? "); // Prompt user for next action

            Scanner s = new Scanner(System.in); // Create Scanner to read input
            String input = s.nextLine(); // Read entire line of input
            String[] words = input.split(" "); // Split input into words

            if (words.length != 2) { // Check for proper two-word command
                status = DEFAULT_STATUS; // Set status to error message
                continue; // Skip to next loop iteration
            }

            String verb = words[0]; // First word is the action verb
            String noun = words[1]; // Second word is the target noun

            switch (verb) { // Decide which action to take
                case "go": // If verb is 'go'
                    handleGo(noun); // Move to another room
                    break;
                case "look": // If verb is 'look'
                    handleLook(noun); // Describe an item
                    break;
                case "take": // If verb is 'take'
                    handleTake(noun); // Pick up an item
                case "use": // if verb is 'use'
                    handleGo(noun); // Use an item ONLY IN INVENTORY
                    break;
                case "eat":
                    handleEat(noun);
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
    private String[] edibleItems; // Items you can eat

    public Room(String name) { // Constructor
        this.name = name; // Set the room's name
    }

    public void setExitDirections(String[] exitDirections) { // Setter for exits
        this.exitDirections = exitDirections;
    }

    public String[] getExitDirections() { // Getter for exits
        return exitDirections;
    }

    public void setExitDestinations(Room[] exitDestinations) { // Setter for exit destinations
        this.exitDestinations = exitDestinations;
    }

    public Room[] getExitDestinations() { // Getter for exit destinations
        return exitDestinations;
    }

    public void setItems(String[] items) { // Setter for items
        this.items = items;
    }

    public String[] getItems() { // Getter for items
        return items;
    }

    public void setItemDescriptions(String[] itemDescriptions) { // Setter for descriptions
        this.itemDescriptions = itemDescriptions;
    }

    public String[] getItemDescriptions() { // Getter for descriptions
        return itemDescriptions;
    }

    public void setGrabbables(String[] grabbables) { // Setter for grabbable items
        this.grabbables = grabbables;
    }

    public String[] getGrabbables() { // Getter for grabbable items
        return grabbables;
    }

    public void setEdibleItems(String[] edibleItems) {
        this.edibleItems = edibleItems;
    }

    public String[] getEdibleItems() {
        return edibleItems;
    }

    @Override
    public String toString() { // Custom print for the room
        String result = "\nLocation: " + name; // Show room name
        result += "\nYou See: "; // List items
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