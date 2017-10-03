import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class YandexMailNavigate implements MailNavigate {
    private WebDriver driver;
    private WebDriverWait wait;
    private List<WebElement> mails;
    private ReadStatus readStatus;

    private static HashMap<MailNavigate.MessageContant, String> mapSeElem;

    static {
        mapSeElem = new HashMap<MessageContant, String>(4);
        mapSeElem.put(MessageContant.SUBJECT, "div.mail-Message-Toolbar-Subject");
        mapSeElem.put(MessageContant.MESSAGE_SENDER_EMAIL, "span.mail-Message-Sender-Email");
        mapSeElem.put(MessageContant.MESSAGE_SENDER_NAME, "span.ns-view-message-head-sender-name");
        mapSeElem.put(MessageContant.TEXT_CONTENT, "div.mail-Message-Body-Content");
    }

    private void seetup() {
        if (driver == null && wait == null) {
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
            wait = new WebDriverWait(driver, 10);
        }
        if (mails == null) {
            mails = new ArrayList<WebElement>(5);
        }

        readStatus = ReadStatus.NOT_METTER;
    }

    @Override
    public void login(String login, String password) {
        seetup();
        driver.navigate().to("https://mail.yandex.ru/");
        driver.findElement(By.name("login")).clear();
        driver.findElement(By.name("login")).sendKeys(login);
        driver.findElement(By.name("passwd")).sendKeys(password);
        driver.findElement(By.cssSelector("[type=submit]")).click();
    }

    @Override
    public void logout() {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.findElement(By.cssSelector("div.ns-view-head-user")).click();
        driver.findElement(By.cssSelector("a.b-mail-dropdown__item__content[data-metric$=Выход]")).click();
    }

    @Override
    public void gotoMainPage() {
        driver.findElement(By.cssSelector("[class*=mail-FolderList-Item_inbox][title^=Входящие]")).click();
        driver.navigate().refresh(); //collapse open thread TODO write algorithm
    }

    @Override
    public List<WebElement> findMailsInPage() {
        return mails = driver.findElements(By.cssSelector("div[class^=ns-view-messages-item-wrap]"));
    }

    //  ToDo rewrite for universality
    public List<WebElement> filterElements(String letterSender, String letterSubject, ReadStatus readStatus) {
        if (readStatus != null) {
            this.readStatus = readStatus;
        }

        List<WebElement> filteredMails = new ArrayList<WebElement>();
        for (WebElement mail : mails) {
            if (letterSender != null && !checkSender(mail, letterSender)) {
                continue;
            }
            if (letterSubject != null && !checkSubject(mail, letterSubject)) {
                continue;
            }
            if (readStatus != null && !checkUnread(mail, readStatus)) {
                continue;
            }
            filteredMails.add(mail);
        }
        return mails = filteredMails;
    }

    private boolean checkSender(WebElement mail, String letterSender) {
        return mail.findElement(By.cssSelector("span[class*=mail-MessageSnippet-Item_left] span[class=mail-MessageSnippet-FromText]")).getText().equals(letterSender);
    }

    private boolean checkSubject(WebElement mail, String letterSubject) {
        return mail.findElement(By.cssSelector("span[class*=mail-MessageSnippet-Item_text] span[class$=mail-MessageSnippet-Item_subject]")).getText().equals(letterSubject);
    }

    private boolean checkUnread(WebElement mail, ReadStatus status) {
        boolean r = false;
        switch (status) {
            case UNREAD:
                try {
                    mail.findElement(By.cssSelector("span[class*=mail-MessageSnippet-Item_body] span[title=\"Отметить как прочитанное\"]"));
                    r = true;
                } catch (NoSuchElementException ex) {
                    r = false;
                }
                break;

            case NOT_METTER:
                r = true;
        }
        return r;
    }

    @Override
    public void openMail() {
        if (mails.size() == 0) {
            throw new NoSuchElementException("No mails found");
        }
        mails.get(0).click();
        ifThreadOpenMailInThread(this.readStatus);
    }

    @Override
    public String getMessageContent(MessageContant mailElement) {

        return driver.findElement(By.cssSelector(mapSeElem.get(mailElement))).getText();
    }

    private void ifThreadOpenMailInThread(ReadStatus status) {
        WebElement threadElement;
        List<WebElement> threadElements;

        try {
            threadElement = driver.findElement(By.cssSelector("div[class$=mail-MessageSnippet-Thread]"));
            threadElements = threadElement.findElements(By.cssSelector("div.mail-MessageSnippet-Wrapper"));
        } catch (NoSuchElementException ex) {
            return;
        }
        switch (status) {
            case NOT_METTER:
                threadElements.get(0).click();
                break;
            case UNREAD:
                for (WebElement tElement : threadElements) {
                    try {
                        tElement.findElement(By.cssSelector("span[title=\"Отметить как прочитанное\"]"));
                        tElement.click();
                        break;
                    } catch (NoSuchElementException ex) {
                        continue;
                    }
                }
                break;
        }
    }

    @Override
    public void shutdown() {
        driver.close();
        driver = null;
    }
}
