import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        int[] v = new int[100];
        int n,val;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter length of array: ");
        n = scanner.nextInt();
        System.out.println("Enter array values: ");
        for(int i=1;i<=n;i++)
            v[i] = scanner.nextInt();
        System.out.println("Enter value you want to search: ");
        val = scanner.nextInt();

        MultithreadingSearch thread = new MultithreadingSearch(v,1,n,val);
        thread.start();
        while(thread.isAlive()){
            System.out.println("thread running");
        }
        System.out.println(thread.found);
    }
}
