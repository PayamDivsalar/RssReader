import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        RssReader rssReader = new RssReader();
        Scanner console = new Scanner(System.in);
        int functionSpecifier = console.nextInt();
        int pageSpecifier;
        String uRL;

        while(true){
            if(functionSpecifier == 1) {
                rssReader.showUpdates();
                pageSpecifier =console.nextInt();
                if(pageSpecifier != -1){
                    rssReader.getPages().get(pageSpecifier-1).retrieveRssContent();

                }

            }
            if(functionSpecifier == 2) {
                System.out.println("please enter the website URL to add");
                uRL = console.next();
                rssReader.addPage(new Page(uRL));
            }
            if(functionSpecifier == 3) {
                System.out.println("write the URL to remove");
                uRL = console.next();
                rssReader.deletePage(new Page(uRL));
            };
            if(functionSpecifier == 4) return;

            rssReader.startMenu();
            functionSpecifier = console.nextInt();
        }
    }

}
