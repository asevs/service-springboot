package pl.lukasz.service.utilities;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pl.lukasz.service.user.User;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserUtilities {


    public static String getLoggedUser() {
        String username = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            username = authentication.getName();
        }

        return username;
    }

    public static List<User> userDataLoader(File file) {
        List<User> userList = new ArrayList<User>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("user");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    User user = new User();
                    user.setEmail(element.getElementsByTagName("email").item(0).getTextContent());
                    user.setPassword(element.getElementsByTagName("password").item(0).getTextContent());
                    user.setName(element.getElementsByTagName("name").item(0).getTextContent());
                    user.setLastName(element.getElementsByTagName("last_name").item(0).getTextContent());
                    user.setActive(Integer.valueOf(element.getElementsByTagName("active").item(0).getTextContent()));
                    user.setNrRole(Integer.valueOf(element.getElementsByTagName("nrRole").item(0).getTextContent()));
                    userList.add(user);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }


}
