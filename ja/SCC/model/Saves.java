package SCC.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Saves {
    public static File getFolder(String Nombre){
        String appDataPath = System.getenv("APPDATA");
        
        // Fallback por si la variable no existe o no estás en Windows (apunta al home del usuario)
        if (appDataPath == null) {
            appDataPath = System.getProperty("user.home");
        }
        
        // 2. Crear la ruta combinada con el nombre de tu aplicación
        File appFolder = new File(appDataPath, Nombre);
        
        // 3. Crear la carpeta física si aún no existe
        if (!appFolder.exists()) {
            appFolder.mkdirs();
        }
        
        return appFolder;
    }
    
    public static void SaveGCC(GCC Datos){
        File Carpeta = getFolder("LinkUp");

        File Guardado = new File(Carpeta,"Datos.dat");

        try(FileOutputStream fos = new FileOutputStream(Guardado)){
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(Datos);
            System.out.println("Datos guardados con éxito en: " + Guardado.getAbsolutePath());
        }catch(IOException e){
            System.err.println("Ha Ocurrido un error cargando los datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static GCC LoadGCC(){
        File Carpeta = getFolder("LinkUp");

        File Guardado = new File(Carpeta,"Datos.dat");

        if(!Guardado.exists()){
            System.out.println("No existe ese archivo");
            return null;
        }

        try (FileInputStream fis = new FileInputStream(Guardado);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            
            System.out.println("Datos Leidos correctamente de: " + Guardado.getAbsolutePath());
            return (GCC) ois.readObject();

        }catch(IOException | ClassNotFoundException e){
            System.err.println("Ha Ocurrido un error cargando los datos: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
