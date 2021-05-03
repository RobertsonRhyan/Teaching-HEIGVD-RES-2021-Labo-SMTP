package ch.heigvd.model.prank;

import ch.heigvd.config.IConfigurationManager;
import ch.heigvd.model.mail.Group;
import ch.heigvd.model.mail.Message;
import ch.heigvd.model.mail.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class PrankGenerator {
    private final static int MIN_VIC = 3;

    private final static Logger LOG = Logger.getLogger(PrankGenerator.class.getName());

    private IConfigurationManager configurationManager;

    public PrankGenerator(IConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public List<Prank> generatePranks() throws Exception {
        List<Prank> pranks = new ArrayList<>();

        List<Message> messages = configurationManager.getMessages();
        int messageIndex = 0;

        int groupCount = configurationManager.getGroupCount();
        int victimCount = configurationManager.getVictimCount();

        if (victimCount / groupCount < MIN_VIC) {
            groupCount = victimCount / MIN_VIC;
            LOG.warning("A group must have at least " + MIN_VIC + " victims.");
            throw new Exception("Not enough victims per Group");
        }

        List<Group> groups = generateGroups(configurationManager.getVictims(), groupCount);

        for (Group group : groups) {
            Prank prank = new Prank();

            List<Person> victims = group.getMembers();
            Collections.shuffle(victims);
            Person sender = victims.remove(0);
            prank.setVictimSender(sender);
            prank.addVictimRecipients(victims);

            prank.addWitnessRecipients(configurationManager.getWitnessToCC());

            Message message = messages.get(messageIndex);
            messageIndex = (messageIndex + 1) % messages.size();
            prank.setMessage(message);

            pranks.add(prank);
        }
        return pranks;
    }

    public List<Group> generateGroups(List<Person> victims, int groupCount) {
        List<Person> availableVictims = new ArrayList(victims);
        Collections.shuffle(availableVictims);
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < groupCount; ++i) {
            Group group = new Group();
            groups.add(group);
        }

        int turn = 0;
        Group targetGroup;
        while (availableVictims.size() > 0) {
            targetGroup = groups.get(turn);
            turn = (turn + 1) % groups.size();
            Person victim = availableVictims.remove(0);
            targetGroup.addMember(victim);
        }
        return groups;
    }


}
