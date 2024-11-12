package com.example.contactsmanager;




import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private EditText contactNameInput, contactNumberInput;
    private Button addContactButton, deleteContactButton, searchContactButton, editContactButton, favoriteContactButton;
    private TextView stackContacts, favoriteContacts;

    private ContactStack contactStack; // Declare ContactStack class instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        contactNameInput = findViewById(R.id.contactNameInput);
        contactNumberInput = findViewById(R.id.contactNumberInput);
        addContactButton = findViewById(R.id.addContactButton);
        deleteContactButton = findViewById(R.id.deleteContactButton);
        searchContactButton = findViewById(R.id.searchContactButton);
        editContactButton = findViewById(R.id.editContactButton);
        favoriteContactButton = findViewById(R.id.favoriteContactButton);
        stackContacts = findViewById(R.id.stackContacts);
        favoriteContacts = findViewById(R.id.favoriteContacts);

        // Initialize the ContactStack instance
        contactStack = new ContactStack();

        // Set up button click listeners
        addContactButton.setOnClickListener(v -> addContact());
        deleteContactButton.setOnClickListener(v -> deleteLastContact());
        searchContactButton.setOnClickListener(v -> searchContact());
        editContactButton.setOnClickListener(v -> editContact());
        favoriteContactButton.setOnClickListener(v -> addToFavorites());
    }

    private void addContact() {
        String name = contactNameInput.getText().toString().trim();
        String number = contactNumberInput.getText().toString().trim();

        if (name.isEmpty() || number.isEmpty()) {
            Toast.makeText(this, "Please enter both name and number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for duplicate name
        if (contactStack.isDuplicateName(name)) {
            Toast.makeText(this, "Name already exists. Please enter a different name.", Toast.LENGTH_SHORT).show();
            return;
        }

        contactStack.add(new Contact(name, number));
        updateStackDisplay();

        contactNameInput.setText("");
        contactNumberInput.setText("");
        Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show();
    }

    private void deleteLastContact() {
        if (contactStack.isEmpty()) {
            Toast.makeText(this, "No contacts to delete", Toast.LENGTH_SHORT).show();
        } else {
            contactStack.removeLast(); // Remove the last entered contact
            updateStackDisplay();
            Toast.makeText(this, "Last Contact Deleted", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchContact() {
        String name = contactNameInput.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Enter a name to search", Toast.LENGTH_SHORT).show();
            return;
        }

        String result = contactStack.searchByName(name);
        if (!result.isEmpty()) {
            Toast.makeText(this, "Contact Found:\n" + result, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Contact Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void editContact() {
        String name = contactNameInput.getText().toString().trim();
        String newNumber = contactNumberInput.getText().toString().trim();

        if (name.isEmpty() || newNumber.isEmpty()) {
            Toast.makeText(this, "Please enter both name and new number", Toast.LENGTH_SHORT).show();
            return;
        }

        contactStack.editContact(name, newNumber);
        updateStackDisplay();
        Toast.makeText(this, "Contact Updated", Toast.LENGTH_SHORT).show();
    }

    private void addToFavorites() {
        String name = contactNameInput.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Enter a name to mark as favorite", Toast.LENGTH_SHORT).show();
            return;
        }

        String result = contactStack.favoriteContact(name);
        if (!result.isEmpty()) {
            updateFavoritesDisplay();
            Toast.makeText(this, name + " added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Contact Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateStackDisplay() {
        StringBuilder stackText = new StringBuilder("Contacts Stack:\n");
        for (Contact contact : contactStack.getContacts()) {
            stackText.append(contact.getName()).append(" - ").append(contact.getNumber()).append("\n");
        }
        stackContacts.setText(stackText.toString());
    }

    private void updateFavoritesDisplay() {
        StringBuilder favoriteText = new StringBuilder("Favorite Contacts:\n");
        for (Contact contact : contactStack.getFavorites()) {
            favoriteText.append(contact.getName()).append("\n");
        }
        favoriteContacts.setText(favoriteText.toString());
    }
}

class ContactStack {
    private LinkedList<Contact> contacts; // Use LinkedList to maintain the order of contacts
    private ArrayList<Contact> favorites;

    public ContactStack() {
        this.contacts = new LinkedList<>();
        this.favorites = new ArrayList<>();
    }

    public void add(Contact contact) {
        contacts.add(contact); // Add contact to the end of the list
    }

    public void removeLast() {
        if (!isEmpty()) {
            contacts.removeLast(); // Remove the last entered contact
        }
    }

    public boolean isEmpty() {
        return contacts.isEmpty();
    }

    public String searchByName(String name) {
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                return contact.getName() + " - " + contact.getNumber();
            }
        }
        return "";
    }

    public void editContact(String name, String newNumber) {
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                contact.setNumber(newNumber);
                return;
            }
        }
    }

    public boolean isDuplicateName(String name) {
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public String favoriteContact(String name) {
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                favorites.add(contact);
                return name; // Return the name if successfully added to favorites
            }
        }
        return ""; // Return empty string if not found
    }

    public ArrayList<Contact> getFavorites() {
        return favorites;
    }

    public ArrayList<Contact> getContacts() {
        ArrayList<Contact> contactList = new ArrayList<>(contacts);
        return contactList;
    }
}

class Contact {
    private String name;
    private String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
