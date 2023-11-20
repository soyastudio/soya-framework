package soya.framework.commons.bean;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ClassPathTree extends TreeBase<String> {

    public ClassPathTree() {
        //setRoot(new DefaultTreeNode<String>("/", "/"));

        String[] paths = System.getProperty("java.class.path").split(";");
        Arrays.stream(paths).forEach(e -> {
            if(e.endsWith(".jar")) {
                read(new File(e));
            }
        });
    }

    private void read(File file) {
        try {
            ZipFile zip = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zip.entries(); //get entries from the zip file...
            if (entries != null) {
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    System.out.println(entry.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
