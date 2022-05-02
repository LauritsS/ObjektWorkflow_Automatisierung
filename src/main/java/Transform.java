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
                "  },";

        //lesen des Files
        String XmlStr = load(dialog.getSelectedFile().getPath());
        JSONObject prolongPeriod = new org.json.JSONObject(XmlStr);
        String Period = prolongPeriod.getString("prolongationPeriod");

        if(interplay.isSelected()) {
            head += "\"interplay\": {\n" +
                    "    \"active\": true,\n" +
                    "    \"firstSync\": true\n" +
                    "  },";
        } else {
            head += "\"interplay\": {\n" +
                    "    \"active\": false,\n" +
                    "    \"firstSync\": true\n" +
                    "  },";
        }


        if(prolongation.isSelected()) {
            head += "\"prolongation\": {\n" +
                    "    \"active\": true,\n" +
                    "    \"schedulerHour\": "+ Period + "\n" +
                    "  },";
        } else {
            head += "\"prolongation\": {\n" +
                    "    \"active\": false,\n" +
                    "    \"schedulerHour\": 1\n" +
                    "  },";
        }



        try {
            JSONObject jsonObject = new org.json.JSONObject(XmlStr);
            JSONArray roles = jsonObject.getJSONArray("roles");
            List<String> names = new ArrayList<String>();
            for(int i = 0; i < roles.length(); i++){
                names.add(roles.getJSONObject(i).getString("name"));
            }






        }catch (JSONException e){
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
