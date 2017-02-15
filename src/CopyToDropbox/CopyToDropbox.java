package CopyToDropbox;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CopyToDropbox {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    public static String productContent = "\\\\172.16.55.197\\design\\Smartwares - Product Content\\PRODUCTS\\";
    public static String logFile = "\\\\srvdata\\Data\\Automatisering\\Artur\\Logs\\CopyToDropbox.log";

    public static void main(String[] args) throws IOException {
        File file = new File(productContent);

        String[] directories = file.list(new FilenameFilter() {

            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        for (int i = 0; i < directories.length; i++) {
            CopyToDropbox cd = new CopyToDropbox();
            String source = productContent + directories[i];
            File src = new File(source);
            String destination = "C:\\Dropbox\\SSL Lighting-update artwork\\" + directories[i];
            File dst = new File(destination);
            cd.CopyToDropbox(src, dst);
        }
    }

    public void CopyToDropbox(File srcPath, File dstPath) throws IOException {
        FileWriter fw = new FileWriter(logFile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        if (srcPath.isDirectory()) {
            if (!dstPath.exists()) {
                dstPath.mkdir();
            }
            String files[] = srcPath.list();
            int x = 0;
            for (int i = 0; i < files.length; i++) {
//                if (files[i].startsWith("Packag") || files[i].startsWith("Rating")
//                        || files[i].startsWith("Installation") || files[i].startsWith("Inlay")
//                        || files[i].startsWith("CDlabel") || files[i].startsWith("Sticker")
//                        || files[i].startsWith("Banderol") || files[i].startsWith("Silkscreen")
//                        || files[i].startsWith("Manual_")) {
                if ((!files[i].contains("Thumbs.db")) && (!files[i].contains(".DS_Store")) &&
                        (((new File(srcPath + "\\" + files[i])).exists() && !(new File(dstPath + "\\" + files[i])).exists())
                        //|| (sdf.format((new File(srcPath + "\\" + files[i])).lastModified()).equals(sdf.format((new Date().getTime()))))
                        || (new Date(new File((srcPath + "\\" + files[i])).lastModified()).after(new Date(new Date().getTime() - ((long)1 * 1000 * 60 * 60 * 24)))))) {

                    CopyToDropbox(new File(srcPath, files[i]),
                            new File(dstPath, files[i]));
                    System.out.println(srcPath + "\\" + files[i] + " - " + dstPath + "\\" + files[i]);
                    bw.newLine();
                    bw.write(sdf.format(new Date().getTime()) + "\t - " + files[i]);
                }
//                }
//                if (x == 0) {
//                    if (files[i].startsWith("Manual_IN") && ((new File(srcPath + "\\" + files[i])).exists() && !(new File(dstPath + "\\" + files[i])).exists() || (new File(srcPath + "\\" + files[i])).lastModified() == (new Date().getTime()))) {
//                            CopyToDropbox(new File(srcPath, files[i]),
//                                    new File(dstPath, files[i]));
//                            System.out.println(srcPath + "\\" + files[i] + " - " + dstPath + "\\" + files[i]);
//                            x = x + 1;
//                            break;
//                    } else if (files[i].startsWith("Manual_EN") && ((new File(srcPath + "\\" + files[i])).exists() && !(new File(dstPath + "\\" + files[i])).exists() || (new File(srcPath + "\\" + files[i])).lastModified() == (new Date().getTime()))) {
//                            CopyToDropbox(new File(srcPath, files[i]),
//                                    new File(dstPath, files[i]));
//                            System.out.println(srcPath + "\\" + files[i] + " - " + dstPath + "\\" + files[i]);
//                            x = x + 1;
//                            break;
//                    } else if (files[i].startsWith("Manual_NL") && ((new File(srcPath + "\\" + files[i])).exists() && !(new File(dstPath + "\\" + files[i])).exists() || (new File(srcPath + "\\" + files[i])).lastModified() == (new Date().getTime()))) {
//                            CopyToDropbox(new File(srcPath, files[i]),
//                                    new File(dstPath, files[i]));
//                            System.out.println(srcPath + "\\" + files[i] + " - " + dstPath + "\\" + files[i]);
//                            x = x + 1;
//                            break;
//                    }
//                }
            }
        } else {
            if (!srcPath.exists()) {
                System.out.println("File or directory does not exist.");
//                System.exit(0);
            } else {
                InputStream in = new FileInputStream(srcPath);
                OutputStream out = new FileOutputStream(dstPath);
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        }
        bw.close();
    }
}
