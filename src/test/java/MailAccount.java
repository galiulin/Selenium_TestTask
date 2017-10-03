import java.util.ArrayList;
import java.util.List;

public class MailAccount {
    private String login;
    private String password;
    private List<Letter> letters;

    {
        letters = new ArrayList<Letter>(5);
    }


    static class Letter{
        private String letterSender;
        private String letterSubject;

        Letter(String letterSender, String letterSubject){
            this.letterSender = letterSender;
            this.letterSubject = letterSubject;
        }

        public String getLetterSender() {
            return letterSender;
        }

        public void setLetterSender(String letterSender) {
            this.letterSender = letterSender;
        }

        public String getLetterSubject() {
            return letterSubject;
        }

        public void setLetterSubject(String letterSubject) {
            this.letterSubject = letterSubject;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Letter letter = (Letter) o;

            if (getLetterSender() != null ? !getLetterSender().equals(letter.getLetterSender()) : letter.getLetterSender() != null)
                return false;
            return getLetterSubject() != null ? getLetterSubject().equals(letter.getLetterSubject()) : letter.getLetterSubject() == null;
        }

        @Override
        public int hashCode() {
            int result = getLetterSender() != null ? getLetterSender().hashCode() : 0;
            result = 31 * result + (getLetterSubject() != null ? getLetterSubject().hashCode() : 0);
            return result;
        }
    }

    public MailAccount(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Letter> getLetters() {
        return letters;
    }

    public void addLetter(String letterSender, String letterSubject){
        letters.add(new Letter(letterSender, letterSubject));
    }

    public boolean containsLetter(String letterSender, String letterSubject){
        if (letters.size()==0) return false;
        for (Letter letter : letters){
            if (letter.getLetterSender().equals(letterSender) && letter.getLetterSubject().equals(letterSubject)){
                letters.remove(letter);
                return true;
            }
        }
        return false;
    }
}
