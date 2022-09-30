
package cs210Project;
import java.util.*;
import java.security.*;
import java.io.*;


public class CS210project {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Type: ");
        System.out.println("1 : To generate 2 random sentences with highest matching number of hexes in the 2 hashes");
        System.out.println("2 : To check how many matches are in 2 sentences");
        int user = sc.nextInt();
        sc.nextLine();
        if(user == 1){ // 2 Different types of functionality for user; Find sentences or Check 2 sentences.
            System.out.println("Enter how many sentences you wish to check: ");
            int n = Integer.parseInt(sc.next());
            findHighest(n);
        }
        else if(user == 2){ //checks users sentences
            System.out.println("Type in your two sentences.");
            String a = sc.nextLine().trim(); //need trim() just in case white space.
            String b = sc.nextLine().trim();
            a = sha256(a);
            b = sha256(b);
            System.out.println("Matches: " + checkHash(a,b));
        }
        else {
            System.out.println("You didnt enter a valid number."); //if any input but 1 or 2 entered
        }
        sc.close();
    }
    public static String sha256(String input) { //given class, turns sentence into 64len hash
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
            byte[] salt = "CS210+".getBytes("UTF-8");
            mDigest.update(salt);
            byte[] data = mDigest.digest(input.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < data.length; i++) {
                sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            return (e.toString());
        }
    }
    public static void sentence(String[] ar, int n){ //algorithm to form all possible sentences.
        n = n -1;
        Dictionary dict = new Dictionary();

        int n1 = 0;
        //num of sentences.
            for(int i = 0; i < 154;i++){ //pronoun index's from file
                for(int j = 158;j < 185;j++){ // verb index's from file
                    for(int k = 231;k<268;k++){ // adjective index's from file
                        for(int l = 186;l< 230;l++){ // noun index's from file
                            if(n1 < n){ //keeps repeating until user defined n amount of sentences has been created
                                ar[n1++] = dict.getWord(i).trim() + " " + dict.getWord(j).trim() + " " + "the" + " " + dict.getWord(k).trim() + " " + dict.getWord(l).trim() + "."; //insert all sentences into String[] sentence with set sentence structure
                            }
                            else{
                                break;
                            }
                        }
                    }
                }
            }




    }
    public static void rdmSentence(String[] ar, int n){ //Second algorithm to form sentences, but this one forms at random
        Dictionary dict = new Dictionary();
        for(int i = 0; i < n;i++){
            int pNum = (int)(Math.random()*154); //random index for pronouns
            String pronoun = dict.getWord(pNum).trim(); //need to use .trim() to remove excess white space for all words
            pronoun = pronoun.substring(0,1).toUpperCase() + pronoun.substring(1); //capitalizing first letter.

            int vNum = (int)(Math.random()*28)+158;//random index for verbs
            String verb = dict.getWord(vNum).trim();

            int aNum = (int)(Math.random()*38)+231;//random index for adjectives
            String adjective = dict.getWord(aNum).trim();

            int nNum = (int)(Math.random()*45)+186;//random index for nouns
            String noun = dict.getWord(nNum).trim();

            ar[i] = pronoun + " " + verb + " the " + adjective + " " + noun + "."; //sentence structure.
        }

    }
    public static int checkHash(String a,String b){ //returns the num of matching hex digits in the hash
        int matches = 0;
        for(int i = 0; i < a.length()-1;i++){
            if(a.charAt(i) == b.charAt(i)){
                matches++;
            }
        }
        return matches;
    }
    public static void findHighest(int n){ // algorithm to find n amount of sentences out of the total
        String[] sentences = new String[n];
        sentence(sentences, n); //calls the non-random sentence forming algorithm with n amount of sentences
        String[] hex = new String[n];
        for(int i = 0; i < sentences.length;i++){ //converts all sentences into hex and inserts into hex array
            hex[i] = sha256(sentences[i]);
        }
        int biggest = 0;
        String biggestA = "";
        String biggestB = "";
        for(int i = 0; i < hex.length-1;i++){//finding biggest hash pairs via linear search
            int curr = 0;
            for(int j = i+1; j < hex.length-1;j++){ // Only need to check i+1 ex if ur at index 0 or sentence 1, then you need to check 2 onwards, once you reach sentence 50, lets say out of 100, you only need to check 51 onwards since beforehand was checked already
                if(i != j && (sentences[i].equals(sentences[j]) == false)){
                    curr = checkHash(hex[i],hex[j]); //current will be the amount of matches between i and j sentences at this time
                    if(curr > biggest){ //checks if current is bigger than the biggest so far.
                        biggest = curr; //stores the current amount of biggest matches
                        biggestA = sentences[i]; //stores the current 2 sentences
                        biggestB = sentences[j];
                        System.out.println(biggest);
                        System.out.println(biggestA); // Prints the highest amount of matches and the 2 sentences once new 2 sentences are found with most matches so far
                        System.out.println(biggestB);
                    }
                }
                else{
                }
            }


        }
        System.out.println("FINAL"); //prints final 2 sentences with most matches once the n sentences have been went through.
        System.out.println(biggest);
        System.out.println(biggestA);
        System.out.println(biggestB);
    }
}
class Dictionary{ //dictionary class whihch we are given

    private String input[];

    public Dictionary(){
        input = load("C:\\Users\\danie\\Desktop\\CS210Project\\words.txt");
    }

    public int getSize(){
        return input.length;
    }

    public String getWord(int n){
        return input[n];
    }

    private String[] load(String file) {
        File aFile = new File(file);
        StringBuffer contents = new StringBuffer();
        BufferedReader input = null;
        try {
            input = new BufferedReader( new FileReader(aFile) );
            String line = null;
            int i = 0;
            while (( line = input.readLine()) != null){
                contents.append(line);
                i++;
                contents.append(System.getProperty("line.separator"));
            }
        }catch (FileNotFoundException ex){
            System.out.println("Can't find the file - are you sure the file is in this location: "+file);
            ex.printStackTrace();
        }catch (IOException ex){
            System.out.println("Input output exception while processing file");
            ex.printStackTrace();
        }finally{
            try {
                if (input!= null) {
                    input.close();
                }
            }catch (IOException ex){
                System.out.println("Input output exception while processing file");
                ex.printStackTrace();
            }
        }
        String[] array = contents.toString().split("\n");
        for(String s: array){
            s.trim();
        }
        return array;
    }
}