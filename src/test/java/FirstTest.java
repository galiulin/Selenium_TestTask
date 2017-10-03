import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FirstTest {
    MailAccount mailAccount;
    MailAccount.Letter letter;

    @Before
    public void setup() {
        mailAccount = new MailAccount("nonnamedn@yandex.ru", "Qwerty123");
        letter = new MailAccount.Letter("NoNNamed NoFF", "hkhkhb test fsd");
    }


    @Test
    public void findAndCheckSomeMail() {
        MailNavigate mailNavigate = new YandexMailNavigate();
        mailNavigate.login(mailAccount.getLogin(), mailAccount.getPassword());

        mailNavigate.findMailsInPage();


        mailNavigate.filterElements(letter.getLetterSender(), letter.getLetterSubject(), YandexMailNavigate.ReadStatus.NOT_METTER);

        mailNavigate.openMail();

        assertEquals("nonnamedn@yandex.ru", mailNavigate.getMessageContent(YandexMailNavigate.MessageContant.MESSAGE_SENDER_EMAIL));
        assertEquals("тело письма", mailNavigate.getMessageContent(YandexMailNavigate.MessageContant.TEXT_CONTENT));
        assertTrue(mailNavigate.getMessageContent(YandexMailNavigate.MessageContant.SUBJECT).endsWith("hkhkhb test fsd"));
        assertEquals("NoNNamed NoFF", mailNavigate.getMessageContent(YandexMailNavigate.MessageContant.MESSAGE_SENDER_NAME));

        mailNavigate.gotoMainPage();

        mailNavigate.logout();

        mailNavigate.shutdown();
    }

    @After
    public void shutdown() {

    }
}