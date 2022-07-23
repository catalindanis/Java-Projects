public class MultithreadingSearch extends Thread{

    int[] v = new int[100];
    int inc,sf,val;
    boolean found = false;

    MultithreadingSearch(int v[],int inc,int sf,int val){
        this.v = v;
        this.inc = inc;
        this.sf = sf;
        this.val = val;
    }

    public void run(){
        if(inc == sf)
            found = v[inc] == val;
        else{
            int mij = (inc+sf)/2;
            MultithreadingSearch thread1 = new MultithreadingSearch(v,inc,mij,val);
            MultithreadingSearch thread2 = new MultithreadingSearch(v,mij+1,sf,val);
            thread1.run();
            thread2.run();
            while(thread1.isAlive() || thread2.isAlive()){
                if(thread1.found || thread2.found)
                    found = true;
            }
            found = (thread1.found || thread2.found);
        }
    }

}
