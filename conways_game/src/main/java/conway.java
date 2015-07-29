/**
 * Created by vaibhav.tomar on 20/07/15.
 */
public class conway {
    public static void main(String args[]) {
        System.out.print("hello");
        int arr[][] = new int[20][20];
        arr[0][0] = 1;
        System.out.print(arr[0][0]);
        for (int i = 0; i < 20; i++) {
            System.out.println();
            for (int j=0;j<20;j++)
            {
                System.out.print(" "+j);
            }

        }
    }
}

