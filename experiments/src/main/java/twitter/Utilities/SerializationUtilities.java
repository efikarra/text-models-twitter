package twitter.Utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 *
 * @author gap2
 */

public class SerializationUtilities {

    public static Object loadSerializedObject(String fileName) {
        Object object = null;
        try{
            InputStream file = new FileInputStream(fileName);
            InputStream buffer = new BufferedInputStream( file );
            ObjectInput input = new ObjectInputStream ( buffer );
            try{
                object = input.readObject();
            }
            finally{
                input.close();
            }
        }
        catch(ClassNotFoundException cnfEx){
            cnfEx.printStackTrace();
        }
        catch(IOException ioex){
            ioex.printStackTrace();
        }

        return object;
    }

    public static void storeSerializedObject(Object object, String outputPath) {
        try{
          OutputStream file = new FileOutputStream(outputPath);
          OutputStream buffer = new BufferedOutputStream( file );
          ObjectOutput output = new ObjectOutputStream( buffer );
          try{
            output.writeObject(object);
          }
          finally{
            output.close();
          }
        }
        catch(IOException ioex){
          ioex.printStackTrace();
        }
    }
}