
package RecipientList;
import entity.Bank;

/**
 *
 * @author nikolai
 */
public class BanksSetup {
    
    Bank bank1 = new Bank("1009", "JsonBankSchool", 100, "queueJsonBank");
    Bank bank2 = new Bank("9001", "XmlBankSchool", 1000, "queueXmlBank");
    Bank bank3 = new Bank("9090", "OurJsonBank1", 1009, "queueOurJsonBank1");
    Bank bank4 = new Bank("6548", "OurJsonBank2", 7652, "OurJsonBank2");
    
}
