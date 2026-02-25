package com.example.server.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class EmailService {

  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

  private final Resend resend;
  private final String fromEmail;

  public EmailService(Dotenv dotenv) {
    String apiKey = dotenv.get("RESEND_API_KEY");
    this.fromEmail = dotenv.get("RESEND_FROM_EMAIL", "noreply@resend.dev");
    this.resend = new Resend(apiKey);
  }

  public void sendPasswordResetEmail(String toEmail, String token) {
    String resetLink = "http://localhost:5173/redefinir-senha?token=" + token;

    String html = """
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
          <h2 style="color: #333;">Redefinição de Senha - StudySocial</h2>
          <p>Olá,</p>
          <p>Recebemos uma solicitação para redefinir a senha da sua conta.</p>
          <p>Clique no botão abaixo para criar uma nova senha:</p>
          <div style="text-align: center; margin: 30px 0;">
            <a href="%s"
               style="background-color: #006FEE; color: white; padding: 12px 24px;
                      text-decoration: none; border-radius: 8px; font-weight: bold;">
              Redefinir Senha
            </a>
          </div>
          <p>Ou copie e cole este link no seu navegador:</p>
          <p style="color: #666; word-break: break-all;">%s</p>
          <p style="color: #999; font-size: 14px;">
            Este link expira em 1 hora. Se você não solicitou a redefinição de senha, ignore este email.
          </p>
        </div>
        """.formatted(resetLink, resetLink);

    CreateEmailOptions params = CreateEmailOptions.builder()
        .from("StudySocial <" + fromEmail + ">")
        .to(toEmail)
        .subject("Redefinição de Senha - StudySocial")
        .html(html)
        .build();

    try {
      resend.emails().send(params);
      logger.info("reset sent ->>>", toEmail);
    } catch (ResendException e) {
      logger.error("reset failed ->>>", toEmail, e.getMessage());
      throw new RuntimeException("Falha ao enviar email de redefinição de senha", e);
    }
  }
}
