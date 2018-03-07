package com.eliezer.program;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class ObjectRestorer {
	
	public static Object restore(String path) {
		 
        Object object = null;
       
        try {
               FileInputStream fileInputStream = new FileInputStream(path);
               ObjectInputStream stream = new ObjectInputStream(fileInputStream);

               // retrieves the object
               object = stream.readObject();

               stream.close();
        } catch (Exception e) {
        	System.out.println("restore error... " + e);
               e.printStackTrace();
        }

        return object;
 }

}
