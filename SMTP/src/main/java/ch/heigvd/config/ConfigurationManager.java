package ch.heigvd.config;

import ch.heigvd.model.mail.Message;
import ch.heigvd.model.mail.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigurationManager implements IConfigurationManager {
    private final static Logger LOG = Logger.getLogger(ConfigurationManager.class.getName());

    private String serverAddress;
    private int port;
    private final List<Person> victims = new ArrayList<>();
    private final List<Message> messages = new ArrayList<>();
    private int groupCount;
    private List<Person> witnesses;

    public ConfigurationManager() throws IOException {
        readAddressesFromFile("./config/victims.utf8");
        readMessagesFromFile("./config/messages.utf8");
        readPropertiesFromFile("./config/config.properties");

    }

    private void readAddressesFromFile(String filePath) throws FileNotFoundException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String address = reader.readLine();
                while (address != null) {
                    victims.add(new Person(address));
                    address = reader.readLine();
                }
            }
        } catch (IOException e) {
            LOG.info(e.getMessage());
            e.printStackTrace();
        }

    }

    private void readMessagesFromFile(String filePath) throws FileNotFoundException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String currentLine = reader.readLine();
                while (currentLine != null) {
                    String body = new String();
                    while ((currentLine != null) && (!currentLine.equals("=="))) {
                        body += currentLine;
                        body += "\r\n";
                        currentLine = reader.readLine();
                    }
                    this.messages.add(new Message(body));
                    currentLine = reader.readLine();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readPropertiesFromFile(String filePath) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(filePath));

        this.serverAddress = properties.getProperty("smtpServerAddress");
        this.port = Integer.parseInt(properties.getProperty("smtpServerPort"));
        this.groupCount = Integer.parseInt(properties.getProperty("numberOfGroups"));

        this.witnesses = new ArrayList<>();
        String witnesses = properties.getProperty("witnessesToCC");
        String[] witnessesAddresses = witnesses.split(",");
        for (String address : witnessesAddresses) {
            this.witnesses.add(new Person(address));
        }
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public int getGroupCount() {
        return groupCount;
    }

    @Override
    public int getVictimCount() {
        return victims.size();
    }

    @Override
    public List<Person> getVictims() {
        return victims;
    }

    @Override
    public List<Person> getWitnessToCC() {
        return witnesses;
    }

    @Override
    public String getServerAddress() {
        return serverAddress;
    }

    @Override
    public int getPort() {
        return port;
    }
}
