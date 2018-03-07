package com.eliezer.program;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class ObjectSaver {
	
	public static void save(Object object, String path) {
		 
        try {
          FileOutputStream saveFile = new FileOutputStream(path);
          ObjectOutputStream stream = new ObjectOutputStream(saveFile);

           // save the object
          stream.writeObject(object);

          stream.close();
        } catch (Exception exc) {
     	   System.out.println("saving error..." + exc);
          exc.printStackTrace();
        }
 }

}
