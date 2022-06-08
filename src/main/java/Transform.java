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

        //Erstellen des File-Open Dialogs
        JFileChooser dialog = new JFileChooser();

        //Anlegen der entsprechenden Filter
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML file", "xml");
        dialog.setFileFilter(filter);

        int returnvalue = dialog.showDialog(null, "Select Model-RWF file");

        JOptionPane.showMessageDialog(null, "The produced object RWF file is stored in the same directory as the JAR", "Notice", JOptionPane.INFORMATION_MESSAGE);

        /*if (returnvalue == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "You chose this file to open " + dialog.getSelectedFile().getName(), "Selected file", JOptionPane.INFORMATION_MESSAGE);
        }*/

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



        head += "  \"prolongation\": {\n" +
                    "    \"active\": "+jsonObj.getBoolean("useProlongation")+",\n" +
                    "    \"schedulerHour\": "+ Period + "\n" +
                    "  },\n";



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
                boolean representAsGroup = states.getJSONObject(i).getBoolean("representAsModelGroup");

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
                    if(keys.length() - j != 1) {
                        head += "        \"" + key + "\" : " + value + ",\n";
                    } else {
                        head += "        \"" + key + "\" : " + value + "\n";
                    }
                }

                if(states.length() - i != 1) {
                    head += "      },\n" +
                            "      \"groupID\": null\n" +
                            "    },\n";
                } else {
                    head += "      },\n" +
                            "      \"groupID\": null\n" +
                            "    }\n";
                }
        }

        head += "  },\n" +
                "  \"transitions\": {";

        //transitions
        JSONArray transitions = jsonObj.getJSONArray("transitions");

        for(int i = 0; i < transitions.length(); i++) {
            if(i != 0) head += "    },\n";;
            String ID = transitions.getJSONObject(i).getString("LIName");
            JSONArray transitionNames = transitions.getJSONObject(i).getJSONArray("LDNames");
            JSONArray fromStateIDs = transitions.getJSONObject(i).getJSONArray("fromStates");

            head += "\""+ID+"\": {\n" +
                    "      \"ID\": \""+ID+"\",\n" +
                    "      \"classDefIDs\": [\n" +
                    "        {\n" +
                    "          \"ID\": \"{f803b58d-9ade-4e59-9c85-193af44d5461}\",\n" +
                    "          \"name\": \"C_DOCUMENT\"\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"transitionNames\": {\n" +
                    "        \"de\": \""+transitionNames.getString(0)+"\",\n" +
                    "        \"en\": \""+transitionNames.getString(1)+"\",\n" +
                    "        \"es\": \""+transitionNames.getString(2)+"\",\n" +
                    "        \"fr\": \""+transitionNames.getString(3)+"\",\n" +
                    "        \"pl\": \""+transitionNames.getString(4)+"\"\n" +
                    "      },\n" +
                    "      \"fromStateIDs\": [\n";

            for(int k = 0; k<fromStateIDs.length(); k++){
                if(fromStateIDs.length()-k != 1) {
                    head += "        \"" + fromStateIDs.getString(k) + "\",\n";
                } else {
                    head += "        \"" + fromStateIDs.getString(k) + "\"\n";
                }
            }

            String toStateID = transitions.getJSONObject(i).getString("toState");

            boolean visible = transitions.getJSONObject(i).getBoolean("visible");
            boolean reauth = transitions.getJSONObject(i).getBoolean("reauth");
            boolean writeVersionHistory = transitions.getJSONObject(i).getJSONObject("execActions").getBoolean("noHistoryEntry");
            String transitionIcon = transitions.getJSONObject(i).getString("transitionIcon");

            boolean inital = false;
            if(transitions.getJSONObject(i).getString("fromState").equals("")) inital = true;

            boolean createNewVersion = transitions.getJSONObject(i).getJSONObject("execActions").getBoolean("createNewModelCopy");

            head +=
                    "      ],\n" +
                    "      \"toStateID\": \""+toStateID+"\"," +
                    "      \"visible\": "+visible+",\n" +
                    "      \"reauth\": "+reauth+",\n" +
                    "      \"writeVersionHistory\": "+!writeVersionHistory+",\n" +
                    "      \"transitionIcon\": \""+transitionIcon+"\",\n" +
                    "      \"initialTransition\": "+inital+",\n" +
                    "      \"createNewVersion\": "+createNewVersion+",\n";

            JSONObject checkActions = transitions.getJSONObject(i).getJSONObject("checkActions");
            JSONObject execActions = transitions.getJSONObject(i).getJSONObject("execActions");

            JSONArray blockTrans = checkActions.getJSONObject("RWF_VALIDATION_SETTINGS").getJSONArray("blockTrans");
            JSONArray confirmTrans = checkActions.getJSONObject("RWF_VALIDATION_SETTINGS").getJSONArray("confirmTrans");

            JSONArray keys = checkActions.names ();

            head += "\"checkActions\": {\n" +
                    "        \"active\": [\n";

            int written = 0;

            for(int l = 0; l < keys.length(); l++){
                if(l != 0 && written>1) {
                    head += "          },\n";
                    if(written != 1) written--;
                }

                String key = keys.getString(l);

                JSONArray arrayvalue = null;
                JSONObject objectvalue = null;
                boolean boolvalue = true;
                String stringvalue = "";


                if(checkActions.get(key) instanceof Boolean) {
                    boolvalue = checkActions.getBoolean (key);
                } else if(checkActions.get(key) instanceof JSONObject){
                    objectvalue = checkActions.getJSONObject(key);
                } else if(checkActions.get(key) instanceof JSONArray){
                    arrayvalue = checkActions.getJSONArray (key);
                } else if(checkActions.get(key) instanceof String){
                    stringvalue = checkActions.getString(key);
                }


                if(key.equals("checkValidityPeriod") && boolvalue){
                    head += "          {\n" +
                            "            \"data\": {\n" +
                            "              \"validityPeriod\": "+ execActions.getInt("validityPeriod")+"\n" +
                            "           },\n" +
                            "            \"id\": \"MFB_RWF_CORE_checkValidityPeriod\" \n";
                    written++;
                }

                if(key.equals("RWF_VALIDATION") && objectvalue != null && objectvalue.has("STANDARD_name_tasks") && objectvalue.getBoolean("STANDARD_name_tasks")){
                    head += "          {\n" +
                            "            \"id\": \"MFB_RWF_checkTempName\"\n";
                }/* GEHT NOCH NICHT */
            }


            if(written>0) head += "          }\n";

            head += "        ],\n" +
                    "        \"settings\": {\n" +
                    "          \"active\": true,\n" +
                    "          \"blockTransitions\": {\n" +
                    "            \"error\": "+blockTrans.getBoolean(0)+",\n" +
                    "            \"info\": "+blockTrans.getBoolean(1)+",\n" +
                    "            \"todo\": "+blockTrans.getBoolean(2)+",\n" +
                    "            \"warning\": "+blockTrans.getBoolean(3)+"\n" +
                    "          },\n" +
                    "          \"confirmTransitions\": {\n" +
                    "            \"error\": "+confirmTrans.getBoolean(0)+",\n" +
                    "            \"info\": "+confirmTrans.getBoolean(1)+",\n" +
                    "            \"todo\": "+confirmTrans.getBoolean(2)+",\n" +
                    "            \"warning\": "+confirmTrans.getBoolean(3)+"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      },\n";

            head += "      \"metaData\": {},\n" +
                    "      \"comment\": {\n" +
                    "        \"mandatoryComment\": false,\n" +
                    "        \"optionalComment\": false\n" +
                    "      },\n" +
                    "      \"successMessage\": false,\n" +
                    "      \"replacePredecessor\": false,\n" +
                    "      \"setVersion\": true,\n" +
                    "      \"resetVersionHistory\": false,\n" +
                    "      \"execActions\": [],\n" +
                    "      \"notifications\": {\n" +
                    "        \"emails\": {\n" +
                    "          \"active\": false,\n" +
                    "          \"body\": {\n" +
                    "            \"de\": \"<html><head><meta charset=\\\"utf-8\\\"></head><body style=\\\"font-family:'Arial'; font-size:11pt\\\">Sehr geehrte(r) %NAME% %LASTNAME%,<br/><br/>das Objekt <b><a href=\\\"%URL%\\\">\\\"%ARTEFACTNAME%\\\"</a></b> vom Typ %ARTEFACTTYPE% wurde vom Benutzer \\\"%SENDER%\\\" in den Zustand <i>\\\"%STATE%\\\"</i> gesetzt.<br/><br/><table style=\\\"font-size:10pt\\\"><tbody align=\\\"left\\\"><tr><th width=\\\"250px\\\" bgcolor=\\\"#e5e5e5\\\" style=\\\"color:#2F4F99;text-align:left;\\\">Letzter Akteur</th><td style=\\\"color:#404041\\\" bgcolor=\\\"#e5e5e5\\\">%SENDER%</td></tr><tr><th bgcolor=\\\"#EFEFEF\\\" style=\\\"color:#2F4F99;text-align:left;\\\">Objekt</th><td style=\\\"color:#404041\\\" bgcolor=\\\"#EFEFEF\\\"><a href=\\\"%URL%\\\">%ARTEFACTNAME%</a></td></tr><tr><th bgcolor=\\\"#e5e5e5\\\" style=\\\"color:#2F4F99;text-align:left;\\\">Letzter Änderungsgrund</th><td style=\\\"color:#404041\\\" bgcolor=\\\"#e5e5e5\\\">%VERSIONHISTORY%</td></tr></tbody></table><br/><br/>Mit freundlichen Grüßen!</body></html>\",\n" +
                    "            \"en\": \"<html><body style=\\\"font-family:'Arial'; font-size:11pt\\\">Dear %NAME% %LASTNAME%,<br/><br/>The %ARTEFACTTYPE% <b><a href=\\\"%URL%\\\">\\\"%ARTEFACTNAME%\\\"</a></b> has been set to state <i>\\\"%STATE%\\\"</i> by the user \\\"%SENDER%\\\".<br/><br/><table style=\\\"font-size:10pt\\\"><tbody align=\\\"left\\\"><tr><th width=\\\"250px\\\" bgcolor=\\\"#e5e5e5\\\" style=\\\"color:#2F4F99;text-align:left;\\\">Last Actor</th><td style=\\\"color:#404041\\\" bgcolor=\\\"#e5e5e5\\\">%SENDER%</td></tr><tr><th bgcolor=\\\"#EFEFEF\\\" style=\\\"color:#2F4F99;text-align:left;\\\">Object</th><td style=\\\"color:#404041\\\" bgcolor=\\\"#EFEFEF\\\"><a href=\\\"%URL%\\\">%ARTEFACTNAME%</a></td></tr><tr><th bgcolor=\\\"#e5e5e5\\\" style=\\\"color:#2F4F99;text-align:left;\\\">Reason for the last change</th><td style=\\\"color:#404041\\\" bgcolor=\\\"#e5e5e5\\\">%VERSIONHISTORY%</td></tr></tbody></table><br/><br/>Best regards!</body></html>\",\n" +
                    "            \"es\": \"<html><body style=\\\"font-family:'Arial'; font-size:11pt\\\">Estimado %NAME% %LASTNAME%,<br/><br/>El %ARTEFACTTYPE% <b><a href=\\\"%URL%\\\">\\\"%ARTEFACTNAME%\\\"</a></b> ha pasado al estado <i>\\\"%STATE%\\\"</i>  por el usuario \\\"%SENDER%\\\".<br/><br/><br/><table style=\\\"font-size:10pt\\\"><tbody align=\\\"left\\\"><tr><th width=\\\"250px\\\" bgcolor=\\\"#e5e5e5\\\" style=\\\"color:#2F4F99;text-align:left;\\\">Último usuario</th><td style=\\\"color:#404041\\\" bgcolor=\\\"#e5e5e5\\\">%SENDER%</td></tr><tr><th bgcolor=\\\"#EFEFEF\\\" style=\\\"color:#2F4F99;text-align:left;\\\">Objeto</th><td style=\\\"color:#404041\\\" bgcolor=\\\"#EFEFEF\\\"><a href=\\\"%URL%\\\">%ARTEFACTNAME%</a></td></tr><tr><th bgcolor=\\\"#e5e5e5\\\" style=\\\"color:#2F4F99;text-align:left;\\\">Razón del último cambio</th><td style=\\\"color:#404041\\\" bgcolor=\\\"#e5e5e5\\\">%VERSIONHISTORY%</td></tr></tbody></table><br/><br/>Saludos</body></html>\",\n" +
                    "            \"fr\": \"<html><body style=\\\"font-family:'Arial'; font-size:11pt\\\">Bonjour %NAME% %LASTNAME%,<br/><br/>The %ARTEFACTTYPE% <b><a href=\\\"%URL%\\\">\\\"%ARTEFACTNAME%\\\"</a></b> a été indiqué pour le statut <i>\\\"%STATE%\\\"</i> par l'utilisateur %SENDER%\\\".<br/><br/><table style=\\\"font-size:10pt\\\"><tbody align=\\\"left\\\"><tr><th width=\\\"250px\\\" bgcolor=\\\"#e5e5e5\\\" style=\\\"color:#2F4F99\\\">Dernier acteur</th><td style=\\\"color:#404041\\\" bgcolor=\\\"#e5e5e5\\\">%SENDER%</td></tr><tr><th bgcolor=\\\"#EFEFEF\\\" style=\\\"color:#2F4F99\\\">Objet</th><td style=\\\"color:#404041\\\" bgcolor=\\\"#EFEFEF\\\"><a href=\\\"%URL%\\\">%ARTEFACTNAME%</a></td></tr><tr><th bgcolor=\\\"#e5e5e5\\\" style=\\\"color:#2F4F99\\\">Raison des dernières modifications</th><td style=\\\"color:#404041\\\" bgcolor=\\\"#e5e5e5\\\">%VERSIONHISTORY%</td></tr></tbody></table><br/><br/>Cordialement!</body></html>\",\n" +
                    "            \"pl\": \"<html><body style=\\\"font-family:'Arial'; font-size:11pt\\\">Drogi/Droga %NAME% %LASTNAME%,<br/><br/>Status %ARTEFACTTYPE% <b><a href=\\\"%URL%\\\">\\\"%ARTEFACTNAME%\\\"</a></b> został zmieniony na <i>\\\"%STATE%\\\"</i> przez użytkownika \\\"%SENDER%\\\".<br/><br/><br/><table style=\\\"font-size:10pt\\\"><tbody align=\\\"left\\\"><tr><th width=\\\"250px\\\" bgcolor=\\\"#e5e5e5\\\" style=\\\"color:#2F4F99;text-align:left;\\\">Ostatni użytkownik</th><td style=\\\"color:#404041\\\" bgcolor=\\\"#e5e5e5\\\">%SENDER%</td></tr><tr><th bgcolor=\\\"#EFEFEF\\\" style=\\\"color:#2F4F99;text-align:left;\\\">Obiekt</th><td style=\\\"color:#404041\\\" bgcolor=\\\"#EFEFEF\\\"><a href=\\\"%URL%\\\">%ARTEFACTNAME%</a></td></tr><tr><th bgcolor=\\\"#e5e5e5\\\" style=\\\"color:#2F4F99;text-align:left;\\\">Powód ostatniej zmiany</th><td style=\\\"color:#404041\\\" bgcolor=\\\"#e5e5e5\\\">%VERSIONHISTORY%</td></tr></tbody></table><br/><br/></body></html>\"\n" +
                    "          },\n" +
                    "          \"fixedAddresses\": {\n" +
                    "            \"active\": false,\n" +
                    "            \"bcc\": [],\n" +
                    "            \"cc\": [],\n" +
                    "            \"to\": []\n" +
                    "          },\n" +
                    "          \"responsibilityBased\": {\n" +
                    "            \"active\": false,\n" +
                    "            \"bcc\": [],\n" +
                    "            \"cc\": [],\n" +
                    "            \"to\": []\n" +
                    "          },\n" +
                    "          \"roleBased\": {\n" +
                    "            \"active\": false,\n" +
                    "            \"bcc\": false,\n" +
                    "            \"cc\": false,\n" +
                    "            \"to\": false\n" +
                    "          },\n" +
                    "          \"subject\": {\n" +
                    "            \"de\": \"%ARTEFACTTYPE% \\\"%ARTEFACTNAME%\\\" wurde in den Zustand \\\"%STATE%\\\" gesetzt (automatische Benachrichtigung durch ADONIS)\",\n" +
                    "            \"en\": \"%ARTEFACTTYPE% \\\"%ARTEFACTNAME%\\\" has been set to state \\\"%STATE%\\\" (automatic notification by ADONIS)\",\n" +
                    "            \"es\": \"%ARTEFACTTYPE% \\\"%ARTEFACTNAME%\\\" ha pasado al estado \\\"%STATE%\\\" (Notificación automática de ADONIS)\",\n" +
                    "            \"fr\": \"%ARTEFACTTYPE% \\\"%ARTEFACTNAME%\\\" a été indiqué pour le statut \\\"%STATE%\\\" (Notification automatique par ADONIS NP)\",\n" +
                    "            \"pl\": \"%ARTEFACTTYPE% \\\"%ARTEFACTNAME%\\\" został przesłany do stanu \\\"%STATE%\\\" (automatyczne powiadomienie przez ADONIS)\"\n" +
                    "          },\n" +
                    "          \"systemAddresses\": {\n" +
                    "            \"active\": false,\n" +
                    "            \"bcc\": [],\n" +
                    "            \"cc\": [],\n" +
                    "            \"to\": []\n" +
                    "          }\n" +
                    "        },\n" +
                    "        \"tasks\": {\n" +
                    "          \"active\": false,\n" +
                    "          \"responsibilityBased\": {\n" +
                    "            \"active\": false,\n" +
                    "            \"receivers\": []\n" +
                    "          },\n" +
                    "          \"roleBased\": false\n" +
                    "        }\n" +
                    "      },\n" +
                    "      \"versionHistoryText\": {\n" +
                    "        \"de\": \"\",\n" +
                    "        \"en\": \"\",\n" +
                    "        \"es\": \"\",\n" +
                    "        \"fr\": \"\",\n" +
                    "        \"pl\": \"\"\n" +
                    "      },\n" +
                    "      \"followupForPredecessor\": null,\n" +
                    "      \"followUpForCurrent\": null,\n" +
                    "      \"systemType\": null,\n" +
                    "      \"conditions\": [],\n" +
                    "      \"roles\": [\n" +
                    "        \"AUTHOR\",\n" +
                    "        \"ADMIN_DOK\"\n" +
                    "      ]\n";
        }

        head += "    }\n";

        head += "  },\n" +
                "  \"statesOrderPerClass\": {\n" +
                "    \"global\": [\n" +
                "      \"v0\",\n" +
                "      \"v1\",\n" +
                "      \"v2\",\n" +
                "      \"v3\"\n" +
                "    ],\n" +
                "    \"{f803b58d-9ade-4e59-9c85-193af44d5461}\": [\n" +
                "      \"v0\",\n" +
                "      \"v1\",\n" +
                "      \"v2\",\n" +
                "      \"v3\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"transitionsOrderPerClass\": {\n" +
                "    \"{f803b58d-9ade-4e59-9c85-193af44d5461}\": [\n" +
                "      \"TRANS_CREATE\",\n" +
                "      \"TRANS_REVIEW\",\n" +
                "      \"TRANS_REJECT\",\n" +
                "      \"TRANS_RELEASE\",\n" +
                "      \"TRANS_NEW_VERSION\",\n" +
                "      \"TRANS_ARCHIVE\",\n" +
                "      \"TRANS_SYSTEM_ARCHIVE\",\n" +
                "      \"TRANS_PROLONGATE\",\n" +
                "      \"TRANS_ACCEPT\",\n" +
                "      \"TRANS_INVALIDATE\",\n" +
                "      \"TRANS_REMINDER\",\n" +
                "      \"TRANS_REMINDER_INVALID\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"metaData\": {},\n" +
                "  \"mandatoryAttrs\": [\n" +
                "    \"state\",\n" +
                "    \"version\",\n" +
                "    \"versionHistory\",\n" +
                "    \"predecessor\",\n" +
                "    \"configAttr\",\n" +
                "    \"validFrom\",\n" +
                "    \"validUntil\"\n" +
                "  ],\n";


        try {
            JSONArray roles = jsonObj.getJSONArray("roles");

            head += "\"roles\": {\n";
            for(int i = 0; i < roles.length(); i++){
                if(roles.length()-i == 1){
                    head += "    \""+roles.getJSONObject(i).getString("name")+"\": {\n" +
                            "      \"ID\": \""+ roles.getJSONObject(i).getString("name")+"\",\n" +
                            "      \"classDefIDs\": [],\n" +
                            "      \"roleNames\": {\n" +
                            "        \"de\": \""+roles.getJSONObject(i).getJSONObject("langSpecificRoleNames").getString("de")+"\",\n" +
                            "        \"en\": \""+roles.getJSONObject(i).getJSONObject("langSpecificRoleNames").getString("en")+"\",\n" +
                            "        \"es\": \""+roles.getJSONObject(i).getJSONObject("langSpecificRoleNames").getString("es")+"\",\n" +
                            "        \"fr\": \""+roles.getJSONObject(i).getJSONObject("langSpecificRoleNames").getString("fr")+"\",\n" +
                            "        \"pl\": \""+roles.getJSONObject(i).getJSONObject("langSpecificRoleNames").getString("pl")+"\"\n" +
                            "      },\n" +
                            "      \"createNewArtefact\": "+roles.getJSONObject(i).getBoolean("createModelFunc")+",\n" +
                            "      \"skipMailNotification\": false\n" +
                            "    }\n"+
                            "  }\n"+
                            "}";
                } else {
                    head += "    \""+roles.getJSONObject(i).getString("name")+"\": {\n" +
                            "      \"ID\": \""+ roles.getJSONObject(i).getString("name")+"\",\n" +
                            "      \"classDefIDs\": [],\n" +
                            "      \"roleNames\": {\n" +
                            "        \"de\": \""+roles.getJSONObject(i).getJSONObject("langSpecificRoleNames").getString("de")+"\",\n" +
                            "        \"en\": \""+roles.getJSONObject(i).getJSONObject("langSpecificRoleNames").getString("en")+"\",\n" +
                            "        \"es\": \""+roles.getJSONObject(i).getJSONObject("langSpecificRoleNames").getString("es")+"\",\n" +
                            "        \"fr\": \""+roles.getJSONObject(i).getJSONObject("langSpecificRoleNames").getString("fr")+"\",\n" +
                            "        \"pl\": \""+roles.getJSONObject(i).getJSONObject("langSpecificRoleNames").getString("pl")+"\"\n" +
                            "      },\n" +
                            "      \"createNewArtefact\": "+roles.getJSONObject(i).getBoolean("createModelFunc")+",\n" +
                            "      \"skipMailNotification\": false\n" +
                            "    },\n";
                }
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
                if(!line.contains("<")){
                    old = old + line;
                } else if(line.contains("<json>{")){
                    old = old + "{\n";

                } else if(line.contains("}</json>")){
                    old = old + "}";
                }
            }

            old = old.replaceAll("&quot;", "\"");

            //writer = new FileWriter(dialog,StandardCharsets.UTF_8);
            //writer.write(old);
            br.close();
            //writer.close();



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
