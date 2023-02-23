package com.icom.studyscript.macbookpro;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class WebContentAnalyzer {
    private static final String TARGET_URL = "https://www.apple.com.cn/shop/refurbished/mac/512gb-14-英寸-macbook-pro";
    private static final String TARGET_STRING_1 = "11,539";
    private static final String TARGET_STRING_2 = "12,699";
    private static final String TARGET_STRING_3 = "13,269";
    private static final String TARGET_STRING_4 = "13,849";
    private static int printCount = 0;

    public static void main(String[] args) {
        while (true) {
            try {
                // Connect to the target URL and retrieve its content
                URL url = new URL(TARGET_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                Scanner scanner = new Scanner(connection.getInputStream());
                scanner.useDelimiter("\\Z");
                String content = scanner.next();

                // Analyze the content and print the results
                int count1 = countSubstring(content, TARGET_STRING_1);
                int count2 = countSubstring(content, TARGET_STRING_2);
                int count3 = countSubstring(content, TARGET_STRING_3);
                int count4 = countSubstring(content, TARGET_STRING_4);
                if (count1 > 0 || count2 > 0) {
                    String str = "找到 " + count1 + " 个 " + TARGET_STRING_1 + " 和 "
                            + count2 + " 个 " + TARGET_STRING_2 + " 和 " + count3 + " 个 " + TARGET_STRING_3
                            + " 和 " + count4 + " 个 " + TARGET_STRING_4;
                    System.out.println(str);
                    printCount++;

                    // Sleep for 30 minutes if the program has printed more than 5 times
                    if (printCount > 5) {
                        System.out.println("Printing suspended for 30 minutes");
//                        Thread.sleep(30 * 60 * 1000);
                        printCount = 0;
                    }
                } else {
                    System.out.println("No occurrence of " + TARGET_STRING_1 + " or " + TARGET_STRING_2);
                }

                // Wait for 5 seconds before the next request
                Thread.sleep(5 * 1000);
            } catch (IOException | InterruptedException e) {
                System.err.println("Error while analyzing web content: " + e.getMessage());
            }
        }
    }

    // Count the number of occurrences of a substring in a string
    private static int countSubstring(String string, String substring) {
        int count = 0;
        int index = 0;
        while ((index = string.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }

    public void sendEmail(String content) {
        // Sender's email address and password
        final String senderEmail = "your_email@example.com";
        final String senderPassword = "your_email_password";

        // Recipient's email address
        final String recipientEmail = "854262144@qq.com";

        // Mail server configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Authenticate with the mail server
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("Message from Java program");
            message.setText(content);

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully");
        } catch (MessagingException e) {
            System.err.println("Error while sending email: " + e.getMessage());
        }
    }
}

