package org.example.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

public class Mailing {
    public static void sendMessage(String fullName, String gmail, int code) throws MessagingException, InterruptedException {
        Runnable runnable = () -> {
            try {
                System.out.println("Message sent successfully");
                Properties properties = new Properties();
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "465");
                properties.put("mail.smtp.ssl.enable", "true");
                properties.put("mail.smtp.auth", "true");

                String username = "axmadullo2000@gmail.com";
                String password = "uqpdzkbakvvqiopm";

                Session session = getSession( properties, username, password );

                Message message = new MimeMessage( session );

                message.setSubject("Dear " + fullName + "! You have received verification code");

                message.setContent("<h2>Your code is: </h2>" + "<h1>" + code + "</h1>", "text/html");

                message.setFrom(new InternetAddress(username));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(gmail));
                Transport.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
    }

    public static Session getSession( Properties properties, String username, String password ) {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication( username, password );
            }
        });
    }
}
