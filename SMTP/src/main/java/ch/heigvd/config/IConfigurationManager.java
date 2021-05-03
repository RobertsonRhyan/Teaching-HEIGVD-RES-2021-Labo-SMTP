package ch.heigvd.config;

import ch.heigvd.model.mail.Message;
import ch.heigvd.model.mail.Person;

import java.util.List;

public interface IConfigurationManager {

    List<Message> getMessages();

    int getGroupCount();

    int getVictimCount();

    List<Person> getVictims();

    List<Person> getWitnessToCC();

    String getServerAddress();

    int getPort();

}
