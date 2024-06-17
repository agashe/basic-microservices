package com.mycompany.app.Util;

import java.util.HashMap;
import java.io.File;  
import java.io.FileNotFoundException; 
import java.util.Scanner;

public class DotEnv 
{
    private HashMap<String, String> vars = new HashMap<String, String>();

    public DotEnv(String path) throws FileNotFoundException
    {
        File file = new File(path);
        Scanner reader = new Scanner(file);

        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            
            // skip commented lines
            if (data.charAt(0) == '#') {
                continue;
            }
            
            String[] parts = data.split("=");
            vars.put(parts[0], parts[1].replaceAll("\"", ""));
        }
        
        reader.close();
    }

    public String get(String key)
    {
        return vars.get(key);
    }
}
