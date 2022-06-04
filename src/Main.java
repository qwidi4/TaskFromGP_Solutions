import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        List<String> listNameHotels = new ArrayList<>();
        List<String> listPriceHotels = new ArrayList<>();
        List<String> listAddressHotels = new ArrayList<>();

        try {
            //читаем данные из XML файла
            File fXmlFile = new File("Hotels.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            NodeList nodeList = doc.getElementsByTagName("Hotel");
            System.out.println("------------------------");
            for (int i =0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;

                    String hotelName = element.getElementsByTagName("Name").item(0).getTextContent();
                    String hotelState = element.getElementsByTagName("State").item(0).getTextContent();

                    String hotelPrice = element.getAttribute("Price");
                    String hotelAddress = element.getElementsByTagName("AddressLine").item(0).getTextContent();


                    if(availabilityHotelInString(hotelName) && checkingState(hotelState)) {
                        addInListNameHotels(hotelName, listNameHotels);
                        addInListNameHotels(hotelPrice, listPriceHotels);
                        addInListNameHotels(hotelAddress, listAddressHotels);

                    }

                }
            }
            System.out.println("Hotels: ");
            writeListInConsole(listNameHotels);
            System.out.println("Price: ");
            writeListInConsole(listPriceHotels);
            System.out.println("Address: ");
            writeListInConsole(listAddressHotels);

            //создаём новый XML файл
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();

            Document newDoc = builder.newDocument();
            Element rootElement = newDoc.createElement("Lists");
            Element Names =newDoc.createElement("Names");
            Element Prices = newDoc.createElement("Prices");
            Element Address = newDoc.createElement("Address");

            newDoc.appendChild(rootElement);
            rootElement.appendChild(Names);
            rootElement.appendChild(Prices);
            rootElement.appendChild(Address);

            for(int i = 0; i < listNameHotels.size(); i++) {
                String str = listNameHotels.get(i);
                addNameInXML(Names, str, newDoc);
                String str1 = listPriceHotels.get(i);
                addPriceInXML(Prices, str1, newDoc);
                String str2 = listAddressHotels.get(i);
                addAddressInXML(Address, str2, newDoc);
            }











            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(newDoc), new StreamResult(new FileOutputStream("NewHotels.xml")));

        }catch (ParserConfigurationException | SAXException | IOException | TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }



    private static boolean availabilityHotelInString(String str) {  //метод проверяет наличие слова "Hotel" в названии отеля
        int index = str.indexOf("Hilton");
        int count = 0;
        while (index != -1){
            index = str.indexOf("Hilton", index + 1);
            count++;
        }
        if (count > 0) return true;
        else return false;
    }

    private static boolean checkingState(String str){             //проверяет нахождение отеля(Нью-Йорк)

        if(str.equals("NY") || str.equals("New York") || str.equals("NEW YORK")) {
            return true;
        }
        else return false;
    }

    static List<String> addInListNameHotels(String str, List<String> listNameHotels){ //записывает нужные отели в отдельный лист
        listNameHotels.add(str);

        return listNameHotels;
    }

    static void writeListInConsole(List<String> List){ //выводит на консоль отели
        for(int i  = 0; i < List.size(); i++){
            System.out.println(List.get(i));
        }
    }

    private static Node getHotel(Document doc, String name){
        Element Hotel = doc.createElement("Hotel");

        return Hotel;
    }

    static void addNameInXML(Element Names,String str, Document newDoc){
        Element Name = newDoc.createElement("Name");
        Names.appendChild(Name);
        Name.appendChild(newDoc.createTextNode(str));
    }

    static void addPriceInXML(Element Prices,String str, Document newDoc){
        Element Price = newDoc.createElement("Price");
        Prices.appendChild(Price);
        Price.appendChild(newDoc.createTextNode(str));
    }

    static void addAddressInXML(Element Addresses,String str, Document newDoc){
        Element Address = newDoc.createElement("Address");
        Addresses.appendChild(Address);
        Address.appendChild(newDoc.createTextNode(str));
    }




}