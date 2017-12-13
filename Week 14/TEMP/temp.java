import java.io.File;

public class temp {
    public static void main(String[] args) {
        System.out.println("Hi!");
        File f1 = new File("oldname.txt");
        File f2 = new File("newname.txt");
        boolean b = f1.renameTo(f2);
        System.out.println(b);
    }
}