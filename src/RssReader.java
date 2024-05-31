import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RssReader {

    private ArrayList<Page> pages = new ArrayList<>();
    private File file = new File("data.txt");

    public RssReader(){
       String[] line;
       String uRL;

            //add all the websites in the file into array list.
        try {
            Scanner console = new Scanner(file);

            while (console.hasNext()){
                line = console.nextLine().split(";");
                uRL = line[1];
                pages.add(new Page(uRL));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        startMenu();
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

        //this method is adding a page into file if it isn't already there.
    public void addPage(Page page){


        if(pageExist(page)){
            System.out.println(page.getURL() +"already exists");
        }
        else{
            this.pages.add(page);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt", true));
                writer.write(page.getPageTitle() + ';' + page.getURL() + ';' + page.getRSSURL() + '\n');
                System.out.println(page.getURL() + " added.");

                writer.close();
            } catch (IOException ioe) {
                System.out.println("Couldn't write to file");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println("Page successfully added");
        }

    }


    public void deletePage(Page page){
            boolean pageExistence = false;
            for(Page value : pages) {
                if (page.getURL().equals(value.getURL())) {
                    pages.remove(value);
                    pageExistence = true;
                    break;
                }
            }

            if(pageExistence){
            for (int i = 0; i < pages.size(); i++){

                try {
                    BufferedWriter writer;
                    if(i == 0){
                        writer = new BufferedWriter(new FileWriter("data.txt", false));

                    }

                    else {
                        writer = new BufferedWriter(new FileWriter("data.txt", true));

                    }
                    writer.write(pages.get(i).getPageTitle() + ';' + pages.get(i).getURL() + ';' + pages.get(i).getRSSURL() + '\n');
                    writer.close();
                } catch (IOException ioe) {
                    System.out.println("Couldn't write to file");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

            System.out.println("Page successfully deleted");

        }

        else{
            System.out.println("couldn't find " + page.getURL());
        }
    }

    private boolean pageExist(Page page){

        for (Page value : pages) {
            if (value.getURL().equals(page.getURL())) {
               return true;
            }

        }
        return false;
    }

    public void startMenu(){
        System.out.print("[1] show updates\n[2] Add URL\n[3] remove URL\n[4] exit\n");
    }

    public void showUpdates(){
        for(int i = 0; i < pages.size(); i++){

            System.out.println("[" + String.valueOf(i+1) + "]" + " " + pages.get(i).getPageTitle());
        }
        System.out.println("Enter -1 to return");

    }
}
