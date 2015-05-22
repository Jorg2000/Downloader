import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by root on 22.05.2015.
 */
public class Downloader implements Runnable {


    InputStream is;
    FileOutputStream fos;
    URL url;
    String outPath;

    public Downloader(String url, String outPath) throws IOException {
        is = null;
        fos = null;
        this.url = new URL(url);
        this.outPath = outPath;
    }

    private void startDownload() {
        try {
        URLConnection urlConn = url.openConnection();
        is = new BufferedInputStream(urlConn.getInputStream());
        fos = new FileOutputStream(outPath);
        byte[] buffer = new byte[1024];
        int len;

        while ((len = is.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
    }
        catch (IOException e) {
        e.printStackTrace();
    }

}

    @Override
    public void run() {

        System.out.println( url + " " + outPath + " Started" );
        startDownload();
        System.out.println( url + " " + outPath + " Finished!");

    }

}
