package com.erasmus.appli.service;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Async
	public void sendEmail(String[] emails, String subject, Map<Object, Object> data)
			throws MessagingException, IOException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper msg = new MimeMessageHelper(message, false);
		msg.setTo(emails);
		msg.setSubject(subject);

		String text = "<html><head><style>table {font-family: arial, sans-serif; border-collapse: collapse; width: 100%;} td, th { border: 1px solid #dddddd; text-align: left; padding: 8px;}</style></head><body>"
				+ "Dear " + data.get("First Name")+" " + data.get("Last Name")+ ",<br></br><br></br> Your Erasmus application status:"
				+ "<table style='font-family: arial, sans-serif;border-collapse: collapse;width: 100%;'>"
				+ "<tr><th>Application ID</th><th>University Name</th><th>Applied On</th><th>Status</th></tr>"
				+ "<tr><td>" + data.get("Application ID") + "</td>" + "<td>" + data.get("University") + "</td>" + "<td>"
				+ data.get("Applied on") + "</td>" + "<td>" + data.get("Status") + "</td>"
				+ "</tr></table>" + "<br></br>Thanks, <br></br>Erasmus Team" + "</body></html>";

		message.setHeader("Content-Type", "text/html; charset=UTF-8");
		msg.setText(text, true);

		javaMailSender.send(message);

	}

}
