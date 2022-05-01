import org.json.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        try {
            /*Path filepath = Path.of(dialog.getSelectedFile().getPath());
            String XmlStr = Files.readString(filepath);
            */
            File JsonFile = new File("test.json");
            FileWriter ToJsonFile = new FileWriter(JsonFile);
            String XmlStr = load(dialog.getSelectedFile().getPath());
            JSONObject json = XML.toJSONObject(XmlStr);
            String jsonStr = json.toString(2);
            ToJsonFile.write(jsonStr);
            ToJsonFile.close();
            System.out.println(jsonStr);

        }catch (JSONException | IOException e){
            System.out.println(e.toString());
        }
    }

    private static String load(String dialog) {

        String old = "";
        BufferedReader br = null;
        FileWriter writer = null;
        String line = null;
        File fout = new File("linebyline.json");


        try {
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            br = new BufferedReader(new FileReader(dialog, StandardCharsets.UTF_8));

            while ((line = br.readLine()) != null) {
                //old = old + line +System.lineSeparator();
                old = old + line;
                bw.write(XML.toJSONObject(line).toString());
                bw.newLine();
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
