package br.inatel.dm112.adapter;

import java.util.Iterator;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Service;

import br.inatel.dm112.model.MailRequestData;

@Service
@org.springframework.context.annotation.PropertySource("classpath:email.properties")
public class MailAdapter {

	@Autowired
	private Environment env;

	@SuppressWarnings("rawtypes")
	public void sendMail(MailRequestData mailData) {

		System.out.println("Enviando email para: " + mailData.getTo());

		Properties props = new Properties();

		// load all properties from email.properties
		for(Iterator it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext(); ) {
            PropertySource propertySource = (PropertySource) it.next();
            if (propertySource instanceof ResourcePropertySource) {
            	props.putAll(((MapPropertySource) propertySource).getSource());
            }
        }

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailData.getFrom(), mailData.getPassword());
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailData.getFrom()));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailData.getTo()));
			message.setSubject(mailData.getSubject());

			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPartText = new MimeBodyPart(); // texto
			messageBodyPartText.setText(mailData.getContent());
			multipart.addBodyPart(messageBodyPartText);

			if(mailData.getAttachment() != null) {
				BodyPart messageBodyPartAtt = new MimeBodyPart(); // anexo
				ByteArrayDataSource source = new ByteArrayDataSource(mailData.getAttachment(), "application/pdf");
				source.setName("Boleto.pdf");

				messageBodyPartAtt.setDataHandler(new DataHandler(source));
				messageBodyPartAtt.setFileName("Boleto_Venda.pdf");
				multipart.addBodyPart(messageBodyPartAtt);
			}

			message.setContent(multipart);

			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
