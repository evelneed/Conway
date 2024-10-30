import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;


public class FileRead {
    HashMap<Integer,String> LIVERULES = new HashMap<>();
    HashMap<Integer,String> DEADRULES = new HashMap<>();

    public HashMap<Integer, String> getLIVERULES() {
        return LIVERULES;
    }

    public HashMap<Integer, String> getDEADRULES() {
        return DEADRULES;
    }

    /**
     * This method is used to splice each line of rules into their own elements
     * @param line The current line being read by readFile
     * @return a list of Strings. Each element.
     */
    public String [] splice(String line){
        return line.split(" ");
    }

    public boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

    /**
     * Main method used to read through the file of rules.
     * @param file The file that is passed into the method.
     * @throws IOException
     */
    public void readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = "start";
        while(true){
            String[] list = new String[4];
            line = reader.readLine();
            if (line == null) {
                break;
            }
            //System.out.println(line);
            String[] status = splice(line);
            if(Objects.equals(status[0], "Live")){ // The Live rule handling
                Integer count = 0;
                Integer count1 = 0;
                while (true) {
                    line = reader.readLine();
                    if (Objects.equals(line, "break")) {
                        break;
                    }
                    //System.out.println(line);
                    String[] rules = splice(line);
                    String theString = rules[0] + rules[1] + rules[3];
                    if(rules[0].equals(">") || rules[0].equals("<") || rules[0].equals("=")){
                        count++;
                    }
                    if(isInteger(rules[1])){
                        if(Integer.valueOf(rules[1]) >= 1 && Integer.valueOf(rules[1]) <= 8){
                            count++;
                        }
                    }
                    if(rules[3].equals("d") || rules[3].equals("l")){
                        count++;
                    }
                    if(count > 2){
                        LIVERULES.put(count1,theString); // Adding to the Live rules
                        count1++;
                    }
                    else{
                        System.out.println("Invalid Rules");
                        System.exit(1);
                    }

                }
            } else if (Objects.equals(status[0], "Dead")) { // The Dead rule handling
                Integer count = 0;
                Integer count2 = 0;
                while (true) {
                    line = reader.readLine();
                    if (Objects.equals(line, "break")) {
                        break;
                    }
                   // System.out.println(line);
                    String[] rules = splice(line);
                    String theString = rules[0] + rules[1] + rules[3];
                    if(rules[0].equals(">") || rules[0].equals("<") || rules[0].equals("=")){
                        count2++;
                    }
                    if(isInteger(rules[1])){
                        if(Integer.valueOf(rules[1]) >= 1 && Integer.valueOf(rules[1]) <= 8){
                            count2++;
                        }
                    }
                    if(rules[3].equals("d") || rules[3].equals("l")){
                        count2++;
                    }
                    if(count2 > 2){
                        DEADRULES.put(count,theString); // Adding to the Live rules
                        count++;
                    }
                    else{
                        System.out.println("Invalid Rules");
                        System.exit(1);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
//        FileRead obj = new FileRead();
//        try {
//            obj.readFile("Rules.txt");
//        } catch(IOException e) {
//            System.out.println("Error reading the rules");
//            System.exit(1);
//        }
//        HashMap<Integer, String> temp = obj.getLIVERULES();
//        HashMap<Integer, String> temp2 = obj.getDEADRULES();
//        System.out.println(temp.get(0));
//        System.out.println(temp.get(1));
//        System.out.println(temp2.get(0));
    }
}
