import bo.Branch_Office;

public class test {
    public static void main(String[] args){
        Branch_Office bo1 = new Branch_Office() ;

        Thread th = new Thread((Runnable) bo1);
        th.start();
    }
}
