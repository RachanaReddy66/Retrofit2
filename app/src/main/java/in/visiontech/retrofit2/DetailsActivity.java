package in.visiontech.retrofit2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    DetailsAdapter adapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String xmlResponse=getIntent().getStringExtra("XmlString");
        System.out.println("Xml string :"+xmlResponse);

        recyclerView=findViewById(R.id.recycler_view);

        ArrayList<fpsDetails> details_list=xml_parsing_details(xmlResponse);
        adapter=new DetailsAdapter(getApplicationContext(),details_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

    }

    public ArrayList<fpsDetails> xml_parsing_details(String xmlString){
            fpsDetails fpsDetails = null;
            ArrayList<fpsDetails> fps = new ArrayList<>();
            int i=0;
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(xmlString));
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT && i<3) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equals("fpsDetails")) {
                            fpsDetails = new fpsDetails();
                        } else if (xpp.getName().equals("dealer_type")) {
                            eventType = xpp.next();
                            if (eventType == XmlPullParser.TEXT) {
                                fpsDetails.dealer_type = (xpp.getText());
                                System.out.println("dealer_type =================" + xpp.getText());
                            }
                        } else if (xpp.getName().equals("delName")) {
                            eventType = xpp.next();
                            if (eventType == XmlPullParser.TEXT) {
                                fpsDetails.dealer_name = xpp.getText();
                                System.out.println("Dealer name:" + xpp.getText());
                            }
                        } else if (xpp.getName().equals("delUid")) {
                            eventType = xpp.next();
                            if (eventType == XmlPullParser.TEXT) {
                                fpsDetails.dealer_uid = xpp.getText();
                                System.out.println("Dealer UID:" + xpp.getText());
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        System.out.println("Else block");
                        if (xpp.getName().equals("fpsDetails")) {
                            System.out.println("Fpsdetails end tag");
                            i++;
                            fps.add(fpsDetails);
                        }
                    }
                    eventType = xpp.next();
                }
            } catch (XmlPullParserException e) {
                System.out.println("Error in parsing:" + e);
            } catch (IOException e) {
                System.out.println("Error :" + e);
            }
            return fps;
        }

}