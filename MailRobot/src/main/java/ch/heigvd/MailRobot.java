package ch.heigvd;

import ch.heigvd.config.ConfigurationManager;
import ch.heigvd.model.prank.Prank;
import ch.heigvd.model.prank.PrankGenerator;
import ch.heigvd.smtp.SmtpClient;

import java.util.List;


public class MailRobot {

    public static void main(String[] args) throws Exception {
        System.out.println("Test");
        ConfigurationManager configurationManager = new ConfigurationManager();
        PrankGenerator prankGenerator = new PrankGenerator(configurationManager);
        List<Prank> pranks = prankGenerator.generatePranks();

        SmtpClient smtpClient = new SmtpClient(configurationManager.getServerAddress(), configurationManager.getPort());

        for (Prank prank : pranks) {
            smtpClient.sendMessage(prank.generateMailMessage());
        }


    }

}
