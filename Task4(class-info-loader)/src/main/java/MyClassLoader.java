import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MyClassLoader extends ClassLoader {
    @Override
    public Class findClass(String fileName) {
        byte[] b = loadClassFromFile(fileName);
        return defineClass(fileName, b, 0, b.length);
    }

    private byte[] loadClassFromFile(String fileName)  {
        String transformedFileName = fileName.replace('.', File.separatorChar) + ".class";

        InputStream inputStream = this.getClass().getResourceAsStream(transformedFileName);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int nextValue = 0;
        try {
            while ((nextValue = inputStream.read()) != -1) {
                byteStream.write(nextValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteStream.toByteArray();
    }
}
