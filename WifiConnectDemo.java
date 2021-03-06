
package connectionTest;

import enumerations.ConnectionFlag;
import enumerations.ConnectionMode;
import enumerations.Dot11BSSType;
import functions.JWifiAPI;
import structures.AvailableNetwork;
import structures.Bss;
import structures.ConnectionAttributes;
import structures.ConnectionParameters;
import structures.InterfaceCapability;
import structures.InterfaceInfo;
import structures.PhyRadioState;

/**
 * WifiConnectDemo.java for WiFi Automation.
 *
 * @author Sunil Borad(boradsunil007@gmail.com).
 * @version 1.0
 */



public class WifiConnectDemo {

    InterfaceInfo[] interfaces;
    String guid;
    int negotiatedVersion;
    JWifiAPI jWifiAPI;
    AvailableNetwork[] networks;
    boolean networkConnectable;
    InterfaceCapability capability;
    PhyRadioState[] phy;
    ConnectionAttributes ca;
    static WifiConnectDemo wificonnectDemo;
    static LoadProperty loadProperty;  
    static  updateXml upadatexml;
   
    
    static {
        System.loadLibrary("JWifiAPI");
    }

    public WifiConnectDemo() {
        interfaces = JWifiAPI.enumInterfaces();
        negotiatedVersion = JWifiAPI.getNegotiatedVersion();
        guid = interfaces[0].interfaceGuid;
        loadProperty = new LoadProperty();
        upadatexml = new updateXml();
    }

    public static void main(String[] args) {
        wificonnectDemo = new WifiConnectDemo();

        wificonnectDemo.initApi();
        wificonnectDemo.enumarate_interface();
        wificonnectDemo.scan();
        wificonnectDemo.getNetworkList();
        loadProperty.FetchProperty();
        upadatexml.writeXml();
        wificonnectDemo.setProfile();
        wificonnectDemo.connect();
    }

    /**
     * To initialize jwifiapi before call other Functions.
     */
    public void initApi() {

        System.out.println("Negotiated Version: " + negotiatedVersion);
        System.out.println("Guid: " + guid);
        jWifiAPI = new JWifiAPI(negotiatedVersion, guid);
    }

    /**
     * To Fetch Detail of Wireless-lan Adapter(802.11n).
     */
    public void enumarate_interface() {
        System.out.println("-- Enumerating Interfaces... -------------------");
        interfaces = JWifiAPI.enumInterfaces();
        System.out.println("Total " + interfaces.length + " interfaces available.");

        for (int i = 0; i < interfaces.length; i++) {
            System.out.println(
                    "GUID: " + interfaces[i].interfaceGuid
                    + "\nDescription: " + interfaces[i].strInterfaceDescription
                    + "\nState: " + interfaces[i].isState.toString()
                    + "\n--"
            );
        }
    }

    /**
     * To connect with specified Wifi network.
     */
    public void connect() {
        System.out.println("-- Connecting to \"test\" (open network)..");
//        ConnectionParameters parameters = new ConnectionParameters(
//                ConnectionMode.profile, null, LoadProperty.ssid,
//                null,  Dot11BSSType.infrastructure, null
//        );
        
        ConnectionParameters parameters;
        parameters = new ConnectionParameters(
                ConnectionMode.profile, null, LoadProperty.ssid,
                null,  Dot11BSSType.infrastructure,null);
        jWifiAPI.connect(parameters);

    }

    /**
     * To get list of Available Network at Interface(wlan adapter).
     */
    public void getNetworkList() {
        System.out.println("-- Getting Available Network List... -----------");
        networks = jWifiAPI.getAvailableNetworkList(null);

        System.out.println("Total " + networks.length + " networks.");
        for (int i = 0; i < networks.length; i++) {
            AvailableNetwork network = networks[i];
            System.out.println(
                    "SSID: " + network.dot11Ssid
                    + "\nSecurity enabled: " + network.bSecurityEnabled
                    + "\nNumber of BssIds: " + network.uNumberOfBssids
                    + "\nBss type: " + network.dot11BssType
                    + "\nSignal quality: " + network.wlanSignalQuality + "%"
                    + "\nDefault authentication algorithm: "
                    + network.dot11DefaultAuthAlgorithm
                    + "\nDefault cipher algorithm: "
                    + network.dot11DefaultCipherAlgorithm
            );

            networkConnectable = network.bNetworkConnectable;
            System.out.println("Network connectable: " + networkConnectable);
            if (!networkConnectable) {
                System.out.println(
                        "Not connectable reason: "
                        + network.wlanNotConnectableReason
                );
            }
            System.out.println("*----*");
        }
    }

    /**
     * To set content of wifi profile.
     */
    public void setProfile() {
        System.out.println("-- Setting Profile... --------------------------");
        jWifiAPI.setProfile("./settings/MyXml.xml", null, true);
    }

    /**
     * To retrieves all information about a specified profile.
     */
    public void getProfile() {
        System.out.println("-- Getting Profile... --------------------------");
        String profile = jWifiAPI.getProfile("./settings/MyXml.xml");
        System.out.println("This is the profile:\n" + profile);
    }

    /**
     * To delete a profile from an interface or from the machine.
     */
    public void deleteProfile() {
        System.out.println("-- Deleting Profile... -------------------------");
        jWifiAPI.deleteProfile("./settings/MyXml.xml");
    }

    /**
     * To request a scan for available networks. To get an updated available
     * network list.
     */
    public void scan() {
        //run a scan before calling 'getAvailableNetworkList' or 'getNetworkBssList'.
        System.out.println("-- Scanning... ---------------------------------");
        jWifiAPI.scan(null); //null for Display all the available Networks
    }

    /**
     * To retrieve the basic service sets (BSS) list of the wireless networks.
     */
    public void getBssList() {

        System.out.println("-- Getting BSS list... -------------------------");
        Bss[] bssList = jWifiAPI.getNetworkBssList(
                null, Dot11BSSType.infrastructure, false
        );

        System.out.println("Total " + bssList.length + " networks.");
        for (int i = 0; i < bssList.length; i++) {
            Bss network = bssList[i];

            System.out.println(
                    "PHY on which the AP is operating: " + network.uPhyId
                    + "\nMAC address: " + network.dot11Bssid
                    + "\nNetwork SSID: " + network.dot11Ssid
                    + "\nNetwork type: " + network.dot11BssType
                    + "\nPHY type: " + network.dot11BssPhyType
                    + "\nSignal strength (dBm): " + network.lRssi
                    + "\nLink quality (0-100): " + network.uLinkQuality
                    + "\nComplies with the conf. regulatory domain: "
                    + network.bInRegDomain
                    + "\nBeacon interval: "
                    + network.usBeaconPeriod
                    + "\nTimestamp from beacon packet or probe response: "
                    + network.ullTimestamp
                    + "\nHost timestamp value when beacon is received: "
                    + network.ullHostTimestamp
                    + "\nCapability value from the beacon packet: "
                    + network.usCapabilityInformation
                    + "\nFrequency of the center channel (kHz): "
                    + network.ulChCenterFrequency
                    + "\nOffset of the data blob: "
                    + network.ulIeOffset
                    + "\nSize of the data blob (bytes): "
                    + network.ulIeSize
                    + "\n--"
            );
        }
    }

    /**
     * To check interface(Adapter) capability.
     */
    public void interfaceCapability() {
        System.out.println("-- Getting Interface Capability... ------------");
        capability = jWifiAPI.getInterfaceCapability();

        System.out.println(
                "bDot11DSupported: " + capability.bDot11DSupported
                + "\ndwMaxDesiredBssidListSize: "
                + capability.dwMaxDesiredBssidListSize
                + "\ndwMaxDesiredSsidListSize: "
                + capability.dwMaxDesiredSsidListSize
                + "\ninterfaceType: " + capability.interfaceType
        );

        System.out.println(
                "Total " + capability.dot11PhyTypes.length + " phy types:"
        );
        for (int i = 0; i < capability.dot11PhyTypes.length; i++) {
            System.out.println("-" + capability.dot11PhyTypes[i]);
        }
    }

    /**
     * To retrieve information about the interface state..
     */
    public void queryingInterface() {

        System.out.println("-- Querying Interface... -----------------------");

        System.out.println("Radio State:");
        phy = jWifiAPI.queryInterfaceRadioState();
        for (int i = 0;
                i < phy.length;
                i++) {
            System.out.println(
                    "Hardware Radio State: " + phy[i].dot11HardwareRadioState
                    + "\nSoftware Radio State: " + phy[i].dot11SoftwareRadioState
            );
        }

        System.out.println(
                "Bss Type: " + jWifiAPI.queryInterfaceBssType()
                + "\nState: " + jWifiAPI.queryInterfaceState()
        );
    }

    /**
     * To retrieve information about the current connection of the interface.
     */
    public void quaryingCurrantConnection() {

        System.out.println("-- Querying Current Connection... --------------");
        ConnectionAttributes ca = jWifiAPI.queryInterfaceCurrentConnection();

        System.out.println(
                "isState: " + ca.isState
                + "\nstrProfileName: " + ca.strProfileName
                + "\nConnectionMode: " + ca.wlanConnectionMode
                + "\nBssType: " + ca.wlanAssociationAttributes.dot11BssType
                + "\ndot11Bssid: " + ca.wlanAssociationAttributes.dot11Bssid
                + "\nPhyType: " + ca.wlanAssociationAttributes.dot11PhyType
                + "\ndot11Ssid: " + ca.wlanAssociationAttributes.dot11Ssid
                + "\nuDot11PhyIndex: " + ca.wlanAssociationAttributes.uDot11PhyIndex
                + "\nulRxRate: " + ca.wlanAssociationAttributes.ulRxRate
                + "\nulTxRate: " + ca.wlanAssociationAttributes.ulTxRate
                + "\nwlanSignalQuality: "
                + ca.wlanAssociationAttributes.wlanSignalQuality
                + "\nbOneXEnabled: "
                + ca.wlanSecurityAttributes.bOneXEnabled
                + "\nbSecurityEnabled: "
                + ca.wlanSecurityAttributes.bSecurityEnabled
                + "\nAuthAlgorithm: "
                + ca.wlanSecurityAttributes.dot11AuthAlgorithm
                + "\nCipherAlgorithm: "
                + ca.wlanSecurityAttributes.dot11CipherAlgorithm
        );
    }

    /**
     * To disconnect the interface from its current network.
     */
    public void disconnect() {
        System.out.println("-- Disconnecting... ----------------------------");
        jWifiAPI.disconnect();
    }
}
