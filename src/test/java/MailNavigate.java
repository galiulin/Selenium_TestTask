import org.openqa.selenium.WebElement;

import java.util.List;

public interface MailNavigate {
    void login(String login, String password);

    void logout();

    void gotoMainPage();

    void openMail();

    String getMessageContent(MessageContant mailElement);

    List<WebElement> findMailsInPage();

    List<WebElement> filterElements(String letterSender, String letterSubject, ReadStatus readStatus);

    void shutdown();


    enum ReadStatus {
        //        READ, /*Не реализовано*/
        UNREAD,
        NOT_METTER
    }

    enum MessageContant {
        SUBJECT,
        MESSAGE_SENDER_EMAIL,
        MESSAGE_SENDER_NAME,
        TEXT_CONTENT;
    }
}
