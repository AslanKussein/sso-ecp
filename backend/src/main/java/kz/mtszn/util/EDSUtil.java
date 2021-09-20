package kz.mtszn.util;


import kz.mtszn.dto.EDSInfoGson;
import lombok.extern.java.Log;
import org.apache.xml.security.Init;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.StringJoiner;
import java.util.logging.Level;

@Log
public class EDSUtil {

    public EDSInfoGson getInfo(String signedXml) {
        Init.init();
        EDSInfoGson edsInfo = new EDSInfoGson();
        if (StringUtils.isNotEmpty(signedXml)) {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(true);
                DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
                Document doc = documentBuilder.parse(new ByteArrayInputStream(signedXml.getBytes(StandardCharsets.UTF_8)));

                Element sigElement;
                Element rootEl = (Element) doc.getFirstChild();

                NodeList list = rootEl.getElementsByTagName("ds:Signature");
                int length = list.getLength();
                for (int i = 0; i < length; i++) {
                    Node sigNode = list.item(length - 1);
                    sigElement = (Element) sigNode;
                    if (sigElement == null) {
                        System.err.println("Bad signature: Element 'ds:Reference' is not found in XML document");
                    }
                    XMLSignature signature = new XMLSignature(sigElement, "");
                    KeyInfo ki = signature.getKeyInfo();
                    X509Certificate certKey = ki.getX509Certificate();
                    if (certKey != null) {
                        edsInfo.setCertXml(Arrays.toString(signature.getSignatureValue()));

                        if (certKey.getSubjectDN().toString().contains("CN")) {
                            String certificate = certKey.getSubjectDN().toString();
                            edsInfo.setCn(certificate.substring(certificate.indexOf("CN=") + 3).split(",")[0]);
                        }
                        if (certKey.getSubjectDN().toString().contains("SURNAME")) {
                            String certificate = certKey.getSubjectDN().toString();
                            edsInfo.setSn(certificate.substring(certificate.indexOf("SURNAME=") + 8).split(",")[0]);
                        }
                        if (certKey.getSubjectDN().toString().contains("GIVENNAME")) {
                            String certificate = certKey.getSubjectDN().toString();
                            edsInfo.setGivenName(certificate.substring(certificate.indexOf("GIVENNAME=") + 10).split(",")[0]);
                        }

                        if (certKey.getSubjectDN().toString().contains("SERIALNUMBER=IIN")) {
                            String certificate = certKey.getSubjectDN().toString();
                            edsInfo.setIin(certificate.substring(certificate.indexOf("SERIALNUMBER=IIN") + 16, certificate.indexOf("SERIALNUMBER=IIN") + 28));
                        }

                        edsInfo.setFiz(certKey.getSigAlgName().startsWith("ECGOST"));

                        edsInfo.setDateBegin(certKey.getNotBefore());
                        edsInfo.setDateTo(certKey.getNotAfter());
                        edsInfo.setValid((new Date().getTime() >= certKey.getNotBefore().getTime() && new Date().getTime() <= certKey.getNotAfter().getTime()));

                        try {
                            rootEl.removeChild(sigElement);
                        } catch (DOMException ignored) {

                        }
                    }
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, "DEF_ERROR_PAR", e);
            }
        }
        return edsInfo;
    }

    public String getInfoString(String signedXml) {
        Init.init();
        StringJoiner s = new StringJoiner(" ");
        if (StringUtils.isNotEmpty(signedXml)) {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(true);
                DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
                Document doc = documentBuilder.parse(new ByteArrayInputStream(signedXml.getBytes(StandardCharsets.UTF_8)));

                Element sigElement = null;
                Element rootEl = (Element) doc.getFirstChild();

                NodeList list = rootEl.getElementsByTagName("ds:Signature");
                int length = list.getLength();
                if (length == 0) {
                    list = rootEl.getElementsByTagName("ns4:Signature");
                    length = list.getLength();
                }
                length = list.getLength();
                if (length == 0) {
                    list = rootEl.getElementsByTagName("ns2:Signature");
                    length = list.getLength();
                }
                length = list.getLength();
                if (length == 0) {
                    list = rootEl.getElementsByTagName("Signature");
                    length = list.getLength();
                }

                for (int i = 0; i < length; i++) {
                    Node sigNode = list.item(length - 1);
                    sigElement = (Element) sigNode;
                    if (sigElement == null) {
                        System.err.println("Bad signature: Element 'ds:Reference' is not found in XML document");
                    }
                    XMLSignature signature = new XMLSignature(sigElement, "");
                    KeyInfo ki = signature.getKeyInfo();
                    X509Certificate certKey = ki.getX509Certificate();
                    if (certKey != null) {
                        String certificate = certKey.getSubjectDN().toString();

                        if (certificate.contains("SERIALNUMBER=IIN")) {
                            s.add(certificate.substring(certificate.indexOf("SERIALNUMBER=IIN") + "SERIALNUMBER=IIN".length(), certificate.indexOf("SERIALNUMBER=IIN") + 28));
                        }

                        if (certificate.contains("CN")) {
                            s.add(certificate.substring(certificate.indexOf("CN=") + "CN=".length()).split(",")[0]);
                        }

                        if (certKey.getSubjectDN().toString().contains("EMAILADDRESS")) {
                            s.add(certificate.substring(certificate.indexOf("EMAILADDRESS=") + "EMAILADDRESS=".length()).split(",")[0]);
                        }

                        if (certificate.contains("OU")) {
                            s.add("<br/>")
                                    .add(certificate.substring(certificate.indexOf("OU=") + "OU=".length()).split(",")[0]);
                        }

                        if (certificate.contains("O")) {
                            s.add(certificate.substring(certificate.indexOf("O=") + "O=".length()).split(",")[0]);
                        }

                        try {
                            rootEl.removeChild(sigElement);
                        } catch (DOMException ignored) {

                        }
                    }
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, "DEF_ERROR_PAR", e);
            }
        }
        return s.toString();
    }
}
