import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 21.05.2015.
 * This code implements MULTITHREADING downloading of all mp3s from chosen page on the EX.UA
 *
 */

public class myMainClass {

    public static void main(String[] args) throws IOException, InterruptedException {
        //Open and download webpage
        URL url = new URL("http://www.ex.ua/90889949?r=71793,23776"); //Change this if you want
        StringBuilder sb = new StringBuilder();
        InputStream is = url.openConnection().getInputStream();
        BufferedReader reader = new BufferedReader( new InputStreamReader( is )  );
        String line = null;
        String resault = "";

        while( ( line = reader.readLine() ) != null )  {
            sb.append(line);
        }
        reader.close();
        resault = sb.toString();

        //Using RE to find links
        String pattern = "'/get/[0-9]+' title.*?.mp3";
        Pattern pt = Pattern.compile(pattern);
        Matcher mt = pt.matcher(resault);

        // Processing string
        List<Link> links = new LinkedList<Link>();
        while(mt.find()) {
            String res = mt.group();
            String fileUrl;
            String fileName;
            fileUrl = "http://www.ex.ua/" + res.substring(res.indexOf("get"),res.indexOf("' "));
            fileName = res.substring(res.indexOf("='") + 2);
            links.add(new Link(fileUrl,fileName));
        }

        //Downloading process.
        /*
        * 1. Code is adding new thread to Linked list if number of active thread:
        * a) Less then we need;
        * b) Current file we want do download is not the last;
        * */
        boolean work = true;
        int currentFileIndex = 0;
        int activeThreadCount = 0;
        int threadsForDownload = 4; // Number of thread we want to use. You can change it ;)
        List<Thread> downloads = new LinkedList<Thread>();
        while(work) {
            Thread.sleep(100); // Reducing CPU consumption
            if (activeThreadCount < threadsForDownload && currentFileIndex < links.size() - 1) {
                Link l = links.get(currentFileIndex);
                currentFileIndex++;
                downloads.add(new Thread(new Downloader(l.url, l.file)));
                downloads.get(downloads.size() - 1).start();
            }
            activeThreadCount = 0;
            for (Thread tr : downloads) {
                if (tr.isAlive()){
                    activeThreadCount++;
                }
            }
            if (activeThreadCount == 0 ) {
                work = false;
            }
        }
        System.out.println("That's all for this page");
    }
}
