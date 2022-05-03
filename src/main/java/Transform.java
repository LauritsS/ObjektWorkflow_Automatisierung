import org.json.*;

import java.awt.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Transform {
    public static void main(String[] args) {

        JOptionPane.showMessageDialog(null, "Please make sure that the file to be transformed is in the same directory as the JAR", "Notice", JOptionPane.INFORMATION_MESSAGE);

        //Erstellen des File-Open Dialogs
        JFileChooser dialog = new JFileChooser();

        //Anlegen der entsprechenden Filter
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML file", "xml");
        dialog.setFileFilter(filter);

        int returnvalue = dialog.showDialog(null, "Select Metamodel file");

        if (returnvalue == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "You chose this file to open " + dialog.getSelectedFile().getName(), "Selected file", JOptionPane.INFORMATION_MESSAGE);
        }

        JFrame f;
        f = new JFrame("Komponenten Auswahl");
        f.setLayout(new FlowLayout());

        JCheckBox interplay = new JCheckBox("Interplay aktivieren");
        JCheckBox prolongation = new JCheckBox("Prolongation aktivieren");

        JPanel p = new JPanel();

        p.add(interplay);
        p.add(prolongation);

        f.add(p);
        f.setSize(300, 300);
        //f.show();

        String head = "{\n" +
                "  \"ID\": \"DocumentRWFSettings\",\n" +
                "  \"classDefIDs\": [\n" +
                "    {\n" +
                "      \"ID\": \"{f803b58d-9ade-4e59-9c85-193af44d5461}\",\n" +
                "      \"name\": \"C_DOCUMENT\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"configIcon\": \"fa-file-text\",\n" +
                "  \"configNames\": {\n" +
                "    \"de\": \"Dokumente\",\n" +
                "    \"en\": \"Documents\",\n" +
                "    \"es\": \"Documentos\",\n" +
                "    \"fr\": \"Document\",\n" +
                "    \"pl\": \"Dokumenty\"\n" +
                "  },\n";

        //lesen des Files
        String XmlStr = load(dialog.getSelectedFile().getPath());
        JSONObject jsonObj = new org.json.JSONObject(XmlStr);
        String Period = jsonObj.getString("prolongationPeriod");

        if(interplay.isSelected()) {
            head += "  \"interplay\": {\n" +
                    "    \"active\": true,\n" +
                    "    \"firstSync\": true\n" +
                    "  },\n";
        } else {
            head += "  \"interplay\": {\n" +
                    "    \"active\": false,\n" +
                    "    \"firstSync\": true\n" +
                    "  },\n";
        }


        if(prolongation.isSelected()) {
            head += "  \"prolongation\": {\n" +
                    "    \"active\": true,\n" +
                    "    \"schedulerHour\": "+ Period + "\n" +
                    "  },\n";
        } else {
            head += "  \"prolongation\": {\n" +
                    "    \"active\": false,\n" +
                    "    \"schedulerHour\": 1\n" +
                    "  },\n";
        }

        Boolean Dryrun = jsonObj.getBoolean("dryRunTestEnabled");

        head += "  \"dryRun\": {\n" +
                "    \"active\": " + Dryrun + ",\n" +
                "    \"date\": \"2020-01-01\"\n" +
                "  },\n";

        String major = jsonObj.getString("versionMajorFormat");
        String minor = jsonObj.getString("versionMinorFormat");

        head += "  \"metamodelMappings\": {\n" +
                "    \"state\": {\n" +
                "      \"attrID\": \"{a12e9854-4ef4-4eb1-84ce-5e9accc224f7}\",\n" +
                "      \"attrName\": \"A_OBJECT_STATE\"\n" +
                "    },\n" +
                "    \"version\": {\n" +
                "      \"attrID\": \"{d8d180d5-8329-4c3a-9d19-0d67055a9b93}\",\n" +
                "      \"attrName\": \"MFB_RWF_VERSION\",\n" +
                "      \"config\": {\n" +
                "      \"majorFormat\": \" " +major + "\",\n" +
                "      \"minorFormat\": \" " +minor + "\"\n" +
                "      }\n" +
                "    },\n";

        String maxrows = jsonObj.getString("versionHistoryMaxRows");

        head += "    \"versionHistory\": {\n" +
                "      \"attrID\": \"{68f8836a-548c-429b-ac15-c5f1bca1300d}\",\n" +
                "      \"attrName\": \"A_OBJECT_VERSION_HISTORY\",\n" +
                "      \"config\": {\n" +
                "      \"maxRows\": \" " +maxrows+ "\"\n" +
                "      }\n" +
                "    },\n";

        head += "    \"predecessor\": {\n" +
                "      \"attrID\": \"{040a6f7d-22d2-43a8-81ef-569b68517f38}\",\n" +
                "      \"attrName\": \"RC_PREDECESSOR_OBJECT\"\n" +
                "    },\n" +
                "    \"stablePredecessor\": {},\n" +
                "    \"validFrom\": {\n" +
                "      \"attrID\": \"{fe3dcc9b-80a2-4277-8ace-9d5495a18b9a}\",\n" +
                "      \"attrName\": \"A_VALID_FROM_UTC\"\n" +
                "    },\n" +
                "    \"validUntil\": {\n" +
                "      \"attrID\": \"{51fabbb9-7df9-4caa-af76-74daf039ee0b}\",\n" +
                "      \"attrName\": \"A_VALID_UNTIL_UTC\"\n" +
                "    },\n";

        head += "\"responsible\": {\n" +
                "      \"deputy\": {\n" +
                "        \"classDefIDs\": [\n" +
                "          \"{f803b58d-9ade-4e59-9c85-193af44d5461}\"\n" +
                "        ],\n" +
                "        \"relID\": \"{7c90708c-41f9-4cab-b838-66a360277e66}\",\n" +
                "        \"relName\": \"RC_RESPONSIBLE_PERSON_DEPUTY\"\n" +
                "      },\n" +
                "      \"owner\": {\n" +
                "        \"classDefIDs\": [\n" +
                "          \"{f803b58d-9ade-4e59-9c85-193af44d5461}\"\n" +
                "        ],\n" +
                "        \"relID\": \"{5db02d0b-a06d-47ee-85d7-ea4fc04193ae}\",\n" +
                "        \"relName\": \"RC_DOCUMENT_OWNER\"\n" +
                "      },\n" +
                "      \"person\": {\n" +
                "        \"classDefIDs\": [\n" +
                "          \"{f803b58d-9ade-4e59-9c85-193af44d5461}\"\n" +
                "        ],\n" +
                "        \"relID\": \"{3d7b56f4-0dc1-4dad-9abe-f3e3fcd7eb84}\",\n" +
                "        \"relName\": \"RC_IS_APPLICATION_OWNER\"\n" +
                "      },\n" +
                "      \"role\": {\n" +
                "        \"classDefIDs\": [\n" +
                "          \"{f803b58d-9ade-4e59-9c85-193af44d5461}\"\n" +
                "        ],\n" +
                "        \"relID\": \"{d4f8f287-d188-4d62-b252-dbdf971da178}\",\n" +
                "        \"relName\": \"RC_RESPONSIBLE_ROLE_RS\"\n" +
                "      }\n" +
                "    },\n";

        head += "    \"votingState\": {\n" +
                "      \"attrID\": \"{60b21570-4a3a-4dc6-b55f-7aea425f10a2}\",\n" +
                "      \"attrName\": \"A_VOTING_STATE\"\n" +
                "    },\n" +
                "    \"configAttr\": {\n" +
                "      \"attrID\": \"{7d058588-3f19-4bd2-8976-6dc86fcdba4f}\",\n" +
                "      \"attrName\": \"A_RWF_CONFIG\"\n" +
                "    },\n" +
                "    \"resubmissionDate\": {\n" +
                "      \"attrID\": \"{b8750372-a464-4a96-9142-2b25ea5c85f1}\",\n" +
                "      \"attrName\": \"A_MFB_RWF_RESUBMISSION_DATE\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"states\": {\n";

        JSONArray states = jsonObj.getJSONArray("states");
        //List<String> statelist = new ArrayList<String>();
        for(int i = 0; i < states.length(); i++){
            //statelist.add(states.getJSONObject(i).getString("defaultValue"));
            String ID = states.getJSONObject(i).getString("defaultValue");
            JSONObject stateNames = states.getJSONObject(i).getJSONObject("modelGroupNames");
            String metaState = states.getJSONObject(i).getString("metaState");
            String stateIcon = states.getJSONObject(i).getString("stateIcon");
            String stateColor = states.getJSONObject(i).getString("stateColor");
            Boolean representAsGroup = states.getJSONObject(i).getBoolean("representAsModelGroup");

            head += "    \""+ ID +"\": {\n" +
                    "      \"ID\": \""+ID+"\",\n" +
                    "      \"classDefIDs\": [\n" +
                    "        {\n" +
                    "          \"ID\": \"{f803b58d-9ade-4e59-9c85-193af44d5461}\",\n" +
                    "          \"name\": \"C_DOCUMENT\"\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"stateNames\": {\n" +
                    "        \"de\": \""+stateNames.getString("de")+"\",\n" +
                    "        \"en\": \""+stateNames.getString("en")+"\",\n" +
                    "        \"es\": \""+stateNames.getString("es")+"\",\n" +
                    "        \"fr\": \""+stateNames.getString("fr")+"\",\n" +
                    "        \"pl\": \""+stateNames.getString("pl")+"\"\n" +
                    "      },\n" +
                    "      \"metaState\": \""+metaState+"\",\n" +
                    "      \"stateIcon\": \""+stateIcon+"\",\n" +
                    "      \"stateColor\": \""+stateColor+"\",\n" +
                    "      \"visible\": true,\n" +
                    "      \"metaData\": {},\n" +
                    "      \"representAsGroup\": "+representAsGroup+",\n" +
                    "      \"referencedState\": \"\",\n" +
                    "      \"customGroupNames\": {},\n";

            JSONObject roleAccess  = states.getJSONObject(i).getJSONObject("roleAccessMap");
            JSONArray keys = roleAccess.names();

            head += "      \"roleAccess\": {\n";

            for(int j = 0; j < keys.length(); j++){
                String key = keys.getString(j); //key
                int value = roleAccess.getJSONArray(key).getInt(0); //value
                head += "        \""+key+"\" : "+value+",\n";
            }

            head += "      },\n" +
                    "      \"groupID\": null\n" +
                    "    },\n";
        }


        try {
            JSONArray roles = jsonObj.getJSONArray("roles");
            List<String> names = new ArrayList<String>();
            for(int i = 0; i < roles.length(); i++){
                names.add(roles.getJSONObject(i).getString("name"));
            }

            File fout = new File("out.txt");
            FileOutputStream fos = new FileOutputStream(fout);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));


            bw.write(head);
            bw.close();



        }catch (JSONException | IOException e){
            System.out.println(e.toString());
        }
    }

    private static String load(String dialog) {

        String old = "";
        BufferedReader br = null;
        FileWriter writer = null;
        String line = null;


        try {
            br = new BufferedReader(new FileReader(dialog, StandardCharsets.UTF_8));

            while ((line = br.readLine()) != null) {
                //old = old + line +System.lineSeparator();
                old = old + line;
            }

            writer = new FileWriter(dialog,StandardCharsets.UTF_8);
            writer.write(old);
            br.close();
            writer.close();



        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                }
        }
        return old;
    }
}
