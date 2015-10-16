

package connectionTest;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
/**
 * LoadProperty.java for Set the Property Value For Profile.
 *
 * @author Sunil .
 * @version 1.0
 */
public class LoadProperty {

    public static String HexsString, ssid, networkKey, networkName;

    /**
     * TO Fetch the Property Value From Property File.
     */
    public void FetchProperty() {

        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("./settings/config.properties");
            // load a properties file.
            prop.load(input);
            // get the property value. 
            ssid = prop.getProperty("ssid");
            HexsString = convertAsciiHex(ssid);
            networkKey = prop.getProperty("networkKey");
            networkName = prop.getProperty("networkName");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * To convert SSID in string into HEXString.
     * @param ssid
     * @return ssidInHex
     */
    public String convertAsciiHex(String ssid) {
        /**
         * convert ascii string to HEX string
         */
        String ssidInHex = "";
        try {
            char[] chars = ssid.toCharArray(); //convert string to individual chars
            for (int j = 0; j < chars.length; j++) {
                ssidInHex += Integer.toHexString(chars[j]);  //convert char to hex value
            }
        } catch (NumberFormatException e) {
            return ssid;
        }
        return ssidInHex;
    }
}
