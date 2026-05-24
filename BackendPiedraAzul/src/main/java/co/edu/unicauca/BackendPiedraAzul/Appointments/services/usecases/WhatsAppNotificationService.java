package co.edu.unicauca.BackendPiedraAzul.Appointments.services.usecases;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppNotificationService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.whatsapp-from}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendAppointmentConfirmation(String toPhone, String patientName, String doctorName, String date, String time) {
        String body = String.format(
                "Hola %s, tu cita con el Dr. %s fue agendada para el %s a las %s. " +
                        "Si necesitas cancelar o reagendar, comunícate con nosotros.",
                patientName, doctorName, date, time
        );
        sendMessage(toPhone, body);
    }

    public void sendAppointmentCancellation(String toPhone, String patientName, String date, String time) {
        String body = String.format(
                "Hola %s, tu cita del %s a las %s ha sido cancelada. " +
                        "Puedes agendar una nueva cuando lo desees.",
                patientName, date, time
        );
        sendMessage(toPhone, body);
    }

    public void sendAppointmentReschedule(String toPhone, String patientName, String doctorName, String newDate, String newTime) {
        String body = String.format(
                "Hola %s, tu cita ha sido reagendada con el Dr. %s para el %s a las %s.",
                patientName, doctorName, newDate, newTime
        );
        sendMessage(toPhone, body);
    }

    private void sendMessage(String toPhone, String body) {
        String formattedPhone = formatPhone(toPhone);

        try {
            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + formattedPhone),
                    new PhoneNumber(fromNumber),
                    body
            ).create();
            System.out.println("Mensaje enviado, SID: " + message.getSid());
        } catch (Exception e) {
            System.out.println("ERROR al enviar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Si el número no tiene +57, se lo agrega
    private String formatPhone(String phone) {
        if (phone == null) return "";
        String cleaned = phone.replaceAll("[^0-9+]", "");
        if (!cleaned.startsWith("+")) {
            return "+57" + cleaned;
        }
        return cleaned;
    }
}