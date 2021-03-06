package ch.heigvd.model.prank;

import ch.heigvd.model.mail.Message;
import ch.heigvd.model.mail.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Prank {
    private Person victimSender;
    private final List<Person> victimRecipients = new ArrayList<>();
    private final List<Person> witnessRecipients = new ArrayList<>();
    private Message message;

    public Person getVictimSender() {
        return victimSender;
    }

    public void setVictimSender(Person victimSender) {
        this.victimSender = victimSender;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void addVictimRecipients(List<Person> victims) {
        victimRecipients.addAll(victims);
    }

    public List<Person> getVictimRecipients() {
        return new ArrayList(victimRecipients);
    }

    public void addWitnessRecipients(List<Person> witness) {
        witnessRecipients.addAll(witness);
    }

    public List<Person> getWitnessRecipients() {
        return new ArrayList(witnessRecipients);
    }

    public Message generateMailMessage() {
        Message msg = new Message();

        msg.setBody(this.message.getBody() + "\r\n" + victimSender.getFirstName());

        String[] to = victimRecipients
                .stream()
                .map(p -> p.getAddress())
                .collect(Collectors.toList())
                .toArray(new String[]{});
        msg.setTo(to);

        String[] cc = witnessRecipients
                .stream()
                .map(p -> p.getAddress())
                .collect(Collectors.toList())
                .toArray(new String[]{});
        msg.setCc(cc);

        msg.setFrom(victimSender.getAddress());
        msg.setSubject(this.message.getSubject());

        return msg;


    }
}
