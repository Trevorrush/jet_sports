package com.travel.xmlParser;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by raymondchen on 15-07-16.
 */
public class xmlHandler {
    private String TICKER_ENTRY_KEY = "ticker-entry";
    private String VISITING_TEAM_KEY = "visiting-team";
    private String HOME_TEAM_KEY = "home-team";
    private String DISPLAY_NAME_KEY = "displayname";
    private String NICK_NAME_KEY = "nickname";
    private String SCORE_KEY = "score";
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;
    public String xmlString;

    private String ticker_entry_value;
    private String visiting_team_name_value;
    private String home_team_name_value;

    public xmlHandler(String source_xml_String){
        //  Setup the xmlString
        xmlString = source_xml_String;
    }

    public String getTicker_entry_value(){
        return ticker_entry_value;
    }

    public String getVisiting_team_name_value() {
        return visiting_team_name_value;
    }

    public String getHome_team_name_value(){
        return home_team_name_value;
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text=null;

        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();

                switch (event){
                    case XmlPullParser.START_TAG:
                        if(name.equals(VISITING_TEAM_KEY)) {
                            visiting_team_name_value = myParser.getAttributeValue(null, NICK_NAME_KEY);
                            Log.d("HELLO", visiting_team_name_value);
                        }

                        if (name.equals(HOME_TEAM_KEY)){
                            home_team_name_value = myParser.getAttributeValue(null, NICK_NAME_KEY);
                            Log.d("Hello", home_team_name_value);
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        if(name.equals(TICKER_ENTRY_KEY)){
                            //ticker_entry_value = text;
                        }

                        else{
                        }
                        break;
                }
                event = myParser.next();
            }
            parsingComplete = false;
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchXML(){
        Log.d("HELLO",xmlString);
                try {

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    InputStream stream = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);

                    parseXMLAndStoreIt(myparser);
                    stream.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
    }

}
