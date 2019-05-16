package com.quytran.battletext;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class IMTester {
    String[] mainList;
    String[][] containsCheck, prefixCheck, matchesCheck;
    Trie trie;
    JSONObject myObj;
    //The constructor loads the file and initializes the trie
    public IMTester(String filename) {
        loadInfo(filename);

        trie = new Trie();
        trie.loadKeys(new java.util.ArrayList<String>(Arrays.asList(mainList)));
        System.out.println("The trie should now contain " + mainList.length + " words.");
    }

    //Loading initializes and assigns values to all 4 arrays
    private void loadInfo(String fileName) {
//        Scanner sc = null;
//        try {
//            sc = new Scanner(new File(filename));
//        } catch(FileNotFoundException e) {
//            System.out.println(e.getMessage());
//            System.exit(1);
//        }
        //Start by getting the Trie list
        try {
            InputStream iStream = new FileInputStream(fileName);
            int size = iStream.available();
            byte[] buffer = new byte[size];
            iStream.read(buffer);
            iStream.close();

            String json = new String(buffer, "UTF8");
            //String json = new String(Files.readAllBytes(Paths.get("path/to/json.txt")));
            myObj = new JSONObject(json);

            if(myObj==null){
                System.out.println("\nYour obj is null");
            }
            else{
                System.out.println("\nYour obj is not null");

            }
            Iterator keysIterator = myObj.keys();
            List<String> keysList = new ArrayList<String>();
            while(keysIterator.hasNext()) {
                String key = (String)keysIterator.next();
                keysList.add(key);
            }
            mainList = keysList.toArray(new String[0]);   //mainList

        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        mainList = new String[Integer.parseInt(sc.nextLine())];
//        for(int i = 0; i<mainList.length; i++) mainList[i] = sc.nextLine();

        //Then, get the getPrefix check split up
//        prefixCheck = new String[mainList.length][2];
//        for(int i = 0; i<prefixCheck.length; i++) prefixCheck[i] = mainList[i].split(" ");

    }



    public void testPrefix() {
        System.out.println("\nTime to test the getPrefix() method! The correct answer is shown in parentheses. ");
        for(String[] row : prefixCheck)
            System.out.printf("The longest prefix of '%s' = %s (%s)%n", row[0], this.trie.getPrefix(row[0]), (row.length == 1 ? "" : row[1]));
    }



    //For the testMatches block, need to print row[1:]
    private String matchesList(String[] row) {
        String result = "";
        for(int i=1; i<row.length-1; i++) result+=row[i] + ", ";
        if(row.length>1) result+=row[row.length-1];
        return result;
    }

    public static void main(String[] args) {
        //Insert the needed filename here; to be sure it works, write the whole path with double backslashes (because escape characters)
        //If you place it inside your src folder (ex: if you just drag the file onto your a3posted package), this link should work
        String filename = "D:/words1.json";
        IMTester test = new IMTester(filename);

        //test.testPrefix();
    }

}