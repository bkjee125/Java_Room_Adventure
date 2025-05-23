import java.util.Scanner; // Import Scanner for reading user input

public class RoomAdventure { // Main class containing game logic

    // class variables
    private static Room currentRoom; // The room the player is currently in
    private static String[] inventory = {null, null, null, null, null}; // Player inventory slots
    private static String status; // Message to display after each action
    private static String[] edibleItems = {"apple", "bread", "jar_of_peanut_butter"}; // edible items
    private static int maxHealth = 100; // maximum health is 100
    private static int health = maxHealth; // health starts at 100
    private static Room win;
    private static Room room10; // chat gpt assisted wiht being able to use these variables in this class was getting error
    private static Room room9;
    private static Room room8;
    private static Room room2;
    private static Room room6;
    private static Room room3;
    private static boolean burning = false;
    private static boolean bookPlaced = false;

    // important functions
    public static void putBook() {
        System.out.println("Do you want to put the book on the bookshelf? (yes/no): ");
        String input = SCANNER.nextLine();
        if (input.equals("yes")) {
            bookPlaced = true;
            room9.unlock();
            System.out.println("You put the book on the bookshelf and the bookshelf cracked open revealing a secret door!");
            System.out.println("Would you like to enter? (yes/no): ");
            String answer = SCANNER.nextLine();
            if (answer.equals("yes")) {
                currentRoom = room9;
                status = "You enter the secret room";
            }
            else {
                status = "You decide not to enter the secret room";
            }
        }
    }

    public static void putCoal() {
        System.out.println("Do you want to put the coal in the fireplace and light it? (yes/no): ");
        String input = SCANNER.nextLine();
        if (input.equals("yes")) {
            burning = true;
            status = "The fireplace is now burning!  It's so warm!  I wonder if this could be useful though";
        }
    }

    public static void putTorch() {
        if (burning == false) {
            status = "Nothing happens...";
        }
        else {
            status = "The torch lit up!  You can use this as a light source if you need it.";
        }
    }

    // constants
    final private static Scanner SCANNER = new Scanner(System.in); // chat gpt gave this so i didn't have so many new scanners
    final private static String CODE = "318225";
    final private static String DEFAULT_STATUS =
        "Sorry, I do not understand. Try [verb] [noun]. Valid verbs include 'go', 'look', 'eat', 'drop', 'use' 'speakTo' and 'take'."; // Default error message



    private static void handleGo(String noun) { // Handles moving between rooms
        String[] exitDirections = currentRoom.getExitDirections(); // Get available directions
        Room[] exitDestinations = currentRoom.getExitDestinations(); // Get rooms in those directions
        status = "I don't see that room."; // Default if direciton is not found
        for (int i = 0; i < exitDirections.length; i++) { // Loop through directions
            if (noun.equals(exitDirections[i])) { // If user direction matches
                if (exitDestinations[i].getLocked() && (exitDestinations[i].equals(room2) || exitDestinations[i].equals(room6))) { // chat gpt assisted with this line
                    boolean opened = false;
                    for (int j = 0; j < inventory.length; j++) { // Loop through inventory to find a key
                        if ("key".equals(inventory[j])){
                            System.out.println("Do you want to use the key to open the door? (yes/no)");
                            String input = SCANNER.nextLine();
                            if (input.equals("yes")) {
                                exitDestinations[i].unlock();
                                currentRoom = exitDestinations[i];
                                inventory[j] = null;
                                status = "You use the key and enter the room.  The key disappeared";
                                opened = true;
                            }
                            else {
                                status = "You decide to not use the key and stay in the current room.";
                                break;
                            }
                        }
                    
                    }
                    if (!opened) {
                        status = "This door is locked. Maybe there is a key somewhere";
                        break;
                    }
                }
                else if (exitDestinations[i].equals(room10)) {
                    System.out.println("Enter the code corrctly to open the door: ");
                    String input = SCANNER.nextLine();
                    if (input.equals(CODE)) {
                        exitDestinations[i].unlock();
                        currentRoom = exitDestinations[i];
                        status = "The door slowly opens";
                    }
                    else {
                        status = "You have entered the incorrect code and triggered a trap door.  You have died";
                    }
                }

                else if (exitDestinations[i].equals(room8)) {
                    for (int t = 0; t < inventory.length; t++) {
                        if ("lit_torch".equals(inventory[t])) {
                            room8.unlock();
                            currentRoom = exitDestinations[i];
                        }
                        else {
                            status = "The room is too dark you cannot see.  Find something to use as a light source";
                        }
                    }
                }

                else if (exitDestinations[i].equals(room9)) {
                    if (!bookPlaced) {
                        status = "That is just a bookshelf";
                    }
                    else {
                        currentRoom = room9;
                    }
                }

                else if (exitDestinations[i].equals(win)) {
                    for (int k = 0; k < inventory.length; k++) {
                        if ("wkey".equals(inventory[k])){
                            win.unlock();
                            currentRoom = exitDestinations[i];
                        }
                    }
                }

                else{
                    currentRoom = exitDestinations[i]; // Change current room
                    status = "Changed Room"; // Update status
                }
            }
        }
    }

    private static void handleLook(String noun) { // Handles inspecting items
        String[] items = currentRoom.getItems(); // Visible items in current room
        String[] damageables = currentRoom.getDamageables(); // Damageables in the current room
        String[] itemDescriptions = currentRoom.getItemDescriptions(); // Descriptions for each item
        status = "I don't see that item."; // Default if item not found
        for (int i = 0; i < items.length; i++) { // Loop through items
            if (noun.equals(items[i])) { // If user-noun matches an item
                status = itemDescriptions[i]; // Set status to item description
                for (int j = 0; j < damageables.length; j++) { // loops through damageables
                    if (noun.equals(damageables[j])) { // If user-noun matches a damageable
                        health -=25; // player takes damage
                    }
                }
            }
        }
    }

    private static void handleUse(String noun) {
        String[] useables = currentRoom.getUseables();  // items that can be used
        status = "I can't use that item"; // Default if not useable
        if ("6-pack".equals(noun)) {
            for (String i : inventory) {
                if ("6-pack".equals(i)) {
                    status = "You drink the 6 pack and feel much better.  Health restored";
                    i = null;
                }
            }
        }
        if ("can_opener".equals(noun)) {
            for (int i = 0; i < inventory.length; i++) {
                if ("can_opener".equals(inventory[i])) {
                 // found the opener, now look for the food
                    for (int j = 0; j < inventory.length; j++) {
                        if ("old_can_of_dog_food".equals(inventory[j])) {
                            System.out.println(
                            "Do you want to use the can opener to open the can of dog food? (yes/no): "
                            );
                            String input = SCANNER.nextLine();
                            if (input.equalsIgnoreCase("yes")) {
                                status = "You opened the can of dog food but there is a key inside!";
                                inventory[j] = "wkey";   // ← replace the slot in the array
                                System.out.println(
                                "you are now carrying the win key.  Find the last room and escape"
                                );
                            } else {
                                status = "you don't open the can of dog food";
                            }
                            return;
                        }
                    }
                    status = "you don't have anything to use the can opener on";
                    return;
                }
            }
            status = "You don't have that item";
            return;
        }

        for (String use : useables) { // Loop through useable items
            if(noun.equals(use)) {
                for (int c = 0; c < inventory.length; c++) {// Loop through inventory to see if you are holding useable
                    if (noun.equals(inventory[c])){
                        if ("book".equals(noun)) {
                            putBook();
                            inventory[c] = null;
                            break;
                        }
                        else if ("coal".equals(noun)){
                            putCoal();
                            inventory[c] = null;
                            break;
                        }
                        else {
                            putTorch();
                            inventory[c] = "lit_torch";
                            break;
                        }
                    }
                        
                    }
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
        int inventoryIndex = -1; // used for if statement checking if an item is in the inventory
        
        for (int index = 0; index < inventory.length; index++) { // for loop checks to see if item is in inventory by iterating through inventory
            if (noun.equals(inventory[index])) {
                inventoryIndex = index;
                break;
            }
        }

        if (inventoryIndex == -1) { // if statement saying there is no item to eat since index did not change after iterating through inventory
            status = "You don't have a " + noun + " to eat";
            return;
        }

        boolean isEdible = false; // boolean value representing if an item is edible or not
        for (String item : edibleItems) { // for loop integrating through edibleItems array; and if 
            if (item.equals(noun)) {      // the noun passed in is an item in the array, it changes isEdible value to true
                isEdible = true;
                break;
            }
        }

        if (!isEdible) { // if statement saying if the item is not edible, it says a statement ssaying you cannot eat the item
            status = "I can't eat that.";
            return;
        }

        int healAmount = 20;
        health = Math.min(maxHealth, health + healAmount);
        inventory[inventoryIndex] = null; // removes item from inventory
        status = "You eat the " + noun + " and gain " + healAmount + " health. Current health: " + health + ".";
    }

    private static void handleDrop(String noun){ // handles dropping items
        status = "You can't drop that item. Try dropping an item in your inventory."; // default if the item is not in your inventory
        for (String item : inventory){ // loop through inventory
            if (noun.equals(item)){ // if item exists in inventory
                for (int i = 0; i < inventory.length; i++){ // loop checks to see if the item is in your inventory
                    if (item.equals(inventory[i])){         // if the above is true
                        inventory[i] = null;                // you "drop" the item (the inventory slot is set to null)
                        status = String.format("Your %s was dropped.", noun); // Update status
                        break; // exit the inventory loop
                    }
                }
            }
        }
    }

    

    private static void handleSpeakTo(String noun) {
        String[] items = currentRoom.getItems();
        boolean isSpeakable = false;

        for (String item : items){ // iterates through items in current room.
            if (item.equals(noun)){ // if in the room, it sets isSpeakable to true. (since it is in the room with the user.)
                isSpeakable = true; 
                break;
            }
        }
        
        if (!isSpeakable) {
            status = "You speak to something not here. Surprisingly, nothing responds to you.";
            return;
        }

        switch (noun) { // easier way to check if the noun is a specific one instead of many if-else statements for each interactable
            case "villager": // the noun you input
                System.out.println("I have been stuck in this cold room for a while. Maybe there is some way of lighting up the fire place."); // what prints out once you speak to that item
                break;
            case "mysterious hungry dude":
                System.out.println("I have been so hungry, and all that I have found is this can of dog food in the closet; but there is no way to open it. Maybe there is a can opener somewhere but not in this room.");
                break;
            }
    }

    private static void setupGame() { // Initializes game world
        Room room1 = new Room("Room 1"); // Create Room 1
        room2 = new Room("Room 2"); // Create Room 2
        room3 = new Room("Room 3"); // Create Room 3
        Room room4 = new Room("Room 4"); // Create Room 3
        Room room5 = new Room("Room 5"); // Create Room 3
        room6 = new Room("Room 6"); // Create Room 3
        Room room7 = new Room("Closet"); // Create the Closet
        room8 = new Room("Basement"); // Create the Basement
        room9 = new Room("Hidden Room"); // Create The Hidden Room
        room10 = new Room("Sealed Room"); // Create the Sealed Room
        win = new Room("Escaped"); // Create the winning room


        String[] room1ExitDirections = {"east", "south"}; // Room 1 exits
        Room[] room1ExitDestinations = {room2, room3}; // Destination rooms for Room 1
        String[] room1Items = {"chair", "desk"}; // Items in Room 1
        String[] room1ItemDescriptions = { // Descriptions for Room 1 items
            "It is a chair. Nothing special about it",
            "It's a desk, there is a book resting on it.  There is a key inside one of the drawers"
        };
        String[] room1Grabbables = {"book"}; // Items you can take in Room 1
        String[] room1GrabbableDescriptions = { // Descriptions for Room 1 grabbables
            "Maybe there is something written in it"
        };
        String[] room1Damageables = {""}; // Items that can damage you
        room1.setDamageables(room1Damageables); // Set damageables
        String[] room1Useables = {"6-pack", "can_opener"};

        room1.setExitDirections(room1ExitDirections); // Set exits
        room1.setExitDestinations(room1ExitDestinations); // Set exit destinations
        room1.setItems(room1Items); // Set visible items
        room1.setItemDescriptions(room1ItemDescriptions); // Set item descriptions
        room1.setGrabbables(room1Grabbables); // Set grabbable items
        room1.setGrabbableDescriptions(room1GrabbableDescriptions); // Set grabbale descriptions
        room1.setUseables(room1Useables);

        String[] room2ExitDirections = {"west", "south", "east"}; // Room 2 exits
        Room[] room2ExitDestinations = {room1, room4, room5}; // Destination rooms for Room 2
        String[] room2Items = {"fireplace", "rug", "villager"}; // Items in Room 2
        String[] room2ItemDescriptions = { // Descriptions for Room 2 items
            "It's unlit with no coal in it",
            "There is a lump of coal on the rug.  There seems to be something underneath it too...  It's a key!",
            "Just a villager in the room. You don't know how or why it got in here."
        };
        String[] room2Grabbables = {"coal", "key"}; // Items you can take in Room 2
        String[] room2GrabbableDescriptions = { // Descriptions for Room 2 grabbables
            "An unused piece of coal. This could be used to light the fireplace",
            "This key is rusted too, although much bigger and heavier.  It is labeled 'Room 6'"
        };
        String[] room2Damageables = {""}; // Items that can damage you
        room2.setDamageables(room2Damageables); // Set damageables
        String[] room2Useables = {"coal", "unlit_torch", "6-pack", "can_opener"};
        room2.setExitDirections(room2ExitDirections); // Set exits
        room2.setExitDestinations(room2ExitDestinations); // Set exit destinations
        room2.setItems(room2Items); // Set visible items
        room2.setItemDescriptions(room2ItemDescriptions); // Set item descriptions
        room2.setGrabbables(room2Grabbables); // Set grabbable items
        room2.setGrabbableDescriptions(room2GrabbableDescriptions); // Set grabbable descriptions
        room2.setUseables(room2Useables);
        room2.lock();


        String[] room3ExitDirections = {"north", "east", "west"};
        Room[] room3ExitDestinations = {room1, room4, room9};
        String[] room3Items = {"bookshelves", "statue", "desk"};
        String[] room3ItemDescriptions = {"Something is off about these bookshelves...",
            "There is nothing special about it", "The statue is resting on it. There is a key inside. There is also an apple on it."};
        String[] room3Grabbables = {"key", "apple"};
        String[] room3GrabbableDescriptions = {"It is small and rusted.  It is labeld 'Room 2.'", "An apple lies on top of the desk; it looks tasty and succulent"};
        String[] room3Useables = {"book", "6-pack", "can_opener"};
        String[] room3Damageables = {""}; // Items that can damage you
        room3.setDamageables(room3Damageables); // Set damageables

        room3.setExitDirections(room3ExitDirections);
        room3.setExitDestinations(room3ExitDestinations);
        room3.setItems(room3Items);
        room3.setItemDescriptions(room3ItemDescriptions);
        room3.setGrabbables(room3Grabbables);
        room3.setGrabbableDescriptions(room3GrabbableDescriptions);
        room3.setUseables(room3Useables);

        String[] room4ExitDirections = {"north", "east", "south", "west"};
        Room[] room4ExitDestinations = {room2, null, room8, room3}; // null = death
        String[] room4Items = {"brew-rig", "staircase",};
        String[] room4ItemDescriptions = {
            "You see someone brewing something on the brew rig.  Must be a resident of this strange place",
            "Looks like they lead to the basement"
        };
        String[] room4Grabbables = {"6-pack"};
        String[] room4GrabbableDescriptions = {"They seem to be fresh and unopened"};
        String[] room4Useables = {"6-pack", "can_opener"};
        String[] room4Damageables = {""}; // Items that can damage you
        room4.setDamageables(room4Damageables); // Set damageables

        room4.setExitDirections(room4ExitDirections);
        room4.setExitDestinations(room4ExitDestinations);
        room4.setItems(room4Items);
        room4.setItemDescriptions(room4ItemDescriptions);
        room4.setGrabbables(room4Grabbables);
        room4.setGrabbableDescriptions(room4GrabbableDescriptions);
        room4.setUseables(room4Useables);

        String[] room5ExitDirections = {"north", "west", "south"};
        Room[] room5ExitDestinations = {room7, room2, room6};
        String[] room5Items = {"torn_note"};
        String[] room5ItemDescriptions = {
            "It is hanging on the wall.  You can barley make out that it says 'Find the hidden rooms to escape...'"
        };
        String[] room5Useables = {"6-pack", "can_opener"};
        String[] room5Damageables = {""}; // Items that can damage you
        room5.setDamageables(room5Damageables); // Set damageables
        room5.setExitDirections(room5ExitDirections);
        room5.setExitDestinations(room5ExitDestinations);
        room5.setItems(room5Items);
        room5.setItemDescriptions(room5ItemDescriptions);
        room5.setUseables(room5Useables);

        String[] room6ExitDirections = {"north", "east", "south", "west"};
        Room[] room6ExitDestinations = {room5, null, null, null}; // null = death
        String[] room6Items = {"bed", "nightstand"};
        String[] room6ItemDescriptions = {
            "Looks comfortable",
            "You open up the drawer and see an unlit_torch...  Could be useful. There is also bread in another drawer.",
        };
        String[] room6Grabbables = {"unlit_torch", "bread"};
        String[] room6GrabbableDescriptions = {
            "who keeps an unlit torch in a nightstand?", 
            "The bread looks stale from being in the drawer; however, it looks like it can give nourishment." 
        };
        String[] room6Useables = {"6-pack", "can_opener"};
        String[] room6Damageables = {""}; // Items that can damage you
        room6.setDamageables(room6Damageables); // Set damageables

        room6.setExitDirections(room6ExitDirections);
        room6.setExitDestinations(room6ExitDestinations);
        room6.setItems(room6Items);
        room6.setItemDescriptions(room6ItemDescriptions);
        room6.setGrabbables(room6Grabbables);
        room6.setGrabbableDescriptions(room6GrabbableDescriptions);
        room6.setUseables(room6Useables);
        room6.lock();

        String[] room7ExitDirections = {"south"};
        Room[] room7ExitDestinations = {room5}; 
        String[] room7Items = {"shelf"};
        String[] room7ItemDescriptions = {"You see an old_can_of_dog_food on the shelf as well as a jar of peanut butter"};
        String[] room7Grabbables = {"old_can_of_dog_food", "jar_of_peanut_butter"};
        String[] room7GrabbableDescriptions = {
            "This expired 10 years ago!", 
            "looks like it has been preserved or been bought recently because of its supposed freshness."
        };
        String[] room7Useables = {"6-pack", "can_opener"};
        String[] room7Damageables = {""}; // Items that can damage you
        room7.setDamageables(room7Damageables); // Set damageables

        room7.setExitDirections(room7ExitDirections);
        room7.setExitDestinations(room7ExitDestinations);
        room7.setItems(room7Items);
        room7.setItemDescriptions(room7ItemDescriptions);
        room7.setGrabbables(room7Grabbables);
        room7.setGrabbableDescriptions(room7GrabbableDescriptions);
        room7.setUseables(room7Useables);

        String[] room8ExitDirections = {"north", "south",};
        Room[] room8ExitDestinations = {room4, null}; // null = death
        String[] room8Items = {"desk"};
        String[] room8ItemDescriptions = {"Hmm.  Another desk.  You open the drawer and see a can_opener"};
        String[] room8Grabbables = {"can_opener"};
        String[] room8GrabbableDescriptions = {"This could probably open the can of dog food you found"};
        String[] room8Useables = {"6-pack", "can_opener"};
        String[] room8Damageables = {""}; // Items that can damage you
        room8.setDamageables(room8Damageables); // Set damageables

        room8.setExitDirections(room8ExitDirections);
        room8.setExitDestinations(room8ExitDestinations);
        room8.setItems(room8Items);
        room8.setItemDescriptions(room8ItemDescriptions);
        room8.setGrabbables(room8Grabbables);
        room8.setGrabbableDescriptions(room8GrabbableDescriptions);
        room8.setUseables(room8Useables);
        room8.lock();

        String[] room9ExitDirections = {"north", "east"};
        Room[] room9ExitDestinations = {room10, room3}; // null = death
        String[] room9Items = {"steel_door"};
        String[] room9ItemDescriptions = {
            "It's a solid steel door.  No handle, no keyhole.  There is a number pad beside it though. Maybe you can open it with a code..."
        };
        String[] room9Damageables = {""}; // Items that can damage you
        room9.setDamageables(room9Damageables); // Set damageables
        room9.setExitDirections(room9ExitDirections);
        room9.setExitDestinations(room9ExitDestinations);
        room9.setItems(room9Items);
        room9.setItemDescriptions(room9ItemDescriptions);
        room9.lock();

        String[] room10ExitDirections = {"north", "east", "south", "west"};
        Room[] room10ExitDestinations = {win, null, room9, null}; // null = death
        String[] room10Items = {"3 doors"};
        String[] room10Itemdescriptions = {"Choose wisely... Your life may depend on it"};
        String[] room10Damageables = {""}; // Items that can damage you
        room10.setDamageables(room10Damageables); // Set damageables
        room10.setExitDirections(room10ExitDirections);
        room10.setExitDestinations(room10ExitDestinations);
        room10.setItems(room10Items);
        room10.setItemDescriptions(room10Itemdescriptions);
        room10.lock();

        String[] winExitDirections = {};
        Room[] winExitDestinations = {};
        String[] winItems = {};
        String[] winItemDescriptions = {};
        String[] winDamageables = {""}; // Items that can damage you
        win.setDamageables(winDamageables); // Set damageables

        win.setExitDirections(winExitDirections);
        win.setExitDestinations(winExitDestinations);
        win.setItems(winItems);
        win.setItemDescriptions(winItemDescriptions);
        win.lock();

        currentRoom = room1; // Start game in Room 1
    }
    
    @SuppressWarnings("java:S2189")
    public static void main(String[] args) { // Entry point of the program
        setupGame(); // Initialize rooms, items, and starting room

        while (health > 0) { // Game loop, runs until health reaches 0
            System.out.print(currentRoom.toString()); // Display current room description
            System.out.print("Inventory: "); // Prompt for inventory display

            for (int i = 0; i < inventory.length; i++) { // Loop through inventory slots
                System.out.print(inventory[i] + " "); // Print each inventory item
            }
            System.out.println("\nHealth: " + health + "/" + maxHealth); // displays current health

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
                    break;
                case "use": // if verb is 'use'
                    handleUse(noun); // Use an item ONLY IN INVENTORY
                    break;
                case "eat":
                    handleEat(noun);
                case "drop":
                    handleDrop(noun); // drop an item ONLY IN INVENTORY
                    break;
                case "speak to":
                    handleSpeakTo(noun);
                    break;
                default: // If verb is unrecognized
                    status = DEFAULT_STATUS; // Set status to error message
            }

            System.out.println(status); // Print the status message

            if (health <= 0) { // if the player's health reaches 0, they die
                System.out.println("You died. Game over."); // death message is printed
                s.close(); // the scanner closess
            }
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
    private String[] grabbableDescriptions; // Descriptions for grabbables
    private String[] damageables; // items that can damage you
        private String[] useables; // items you can use and do things with
    private boolean locked = false; // variable to lock doors

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

    public String[] getDamageables() { // Getter for damageable items
        return damageables;
    }

    public void setDamageables(String[] damageables) { // Setter for damageable items
        this.damageables = damageables;
    }

    public String[] getUseables() { // getter for useables
        return useables;
    }

    public void setUseables(String[] usables) { // setter for useable items
        this.useables = usables;
    }

    public String[] getGrabbableDescriptions(){ // getter for grabbable descriptions
        return grabbableDescriptions;
    }

    public void setGrabbableDescriptions(String[] grabbableDescriptions){ // setter for grabbable descriptions
        this.grabbableDescriptions = grabbableDescriptions;
    }   

    public boolean getLocked(){ //getter for locking doors
        return locked;
    }

    public void setLocked(boolean locked){ // setter for locking doors
        this.locked = locked;
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
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
