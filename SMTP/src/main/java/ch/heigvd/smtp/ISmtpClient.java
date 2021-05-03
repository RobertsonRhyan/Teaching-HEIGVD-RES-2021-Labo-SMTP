package ch.heigvd.smtp;


import ch.heigvd.model.mail.Message;

import java.io.IOException;
import java.net.InetAddress;

public interface ISmtpClient {
    public void sendMessage(Message message) throws IOException;

}
