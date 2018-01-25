import kohonen.WTALearningFunction;
import kohonen.LearningData;
import learningFactorFunctional.ConstantFunctionalFactor;
import metrics.EuclidesMetric;
import network.DefaultNetwork;
import topology.MatrixTopology;

import java.lang.Math;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException{

        Scanner inFile1 = new Scanner(new File("onehr.data")).useDelimiter(",");
        //Scanner inFile1 = new Scanner(new File("onehr.data"));
        int i=0;
        float maxV=0, minV=0;
        float g;

        List<String> temps = new ArrayList<String>();
        List<Float> temps2 = new ArrayList<Float>();

        while (inFile1.hasNext()) {
            String token1 = inFile1.next();
            temps.add(token1);
        }
        inFile1.close();

        String[] tempsArray = temps.toArray(new String[0]);
        int floatSize = tempsArray.length;
        Float[] floatArray = new Float[floatSize];
        Float[] normArray = new Float[floatSize];

        for (i=0 ; i<tempsArray.length ; i++){
            //System.out.println(s);
            //floatArray[i] = Float.parseFloat(s);
            floatArray[i] = Float.parseFloat(tempsArray[i].replaceAll("[\n\r]", ""));
            //System.out.println(floatArray[i]);
        }
        for (i = 0 ; i < floatArray.length ; i++){
            if(floatArray[i] > minV){
                minV = floatArray[i];
            }
            if(floatArray[i] < maxV){
                maxV = floatArray[i];
            }
        }
        
        for (i = 0 ; i < floatArray.length ; i++){
            normArray[i] = (floatArray[i] - minV) / (maxV-minV);
            //System.out.println(normArray[i]);
        }

        FileWriter out = null;
        try {
            out = new FileWriter("norm.txt");
            String c;
           int m = 1;
            for (i=0;i<normArray.length;i++) {
                c = normArray[i].toString() + "\t";
                if(m % 72 == 0){
                c = "\n" ;
                }
                m++;
                out.write(c);
            }
        }
        finally {
            if (out != null) {
                out.close();
            }
        }
        /*FileReader in = null;
        FileWriter out = null;
        double[] tab = new double[3000000];

        try {
            in = new FileReader("onehr.data");
            out = new FileWriter("norm.data");

            int c;
            int i=0;
            while ((c = in.read()) != -1) {
                tab[i] = c;
                                                                //// gowno do klonowania z tutorialspoint
                out.write(c);
                i++;
            }
            System.out.println(tab[1]);
        }finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }*/


        MatrixTopology topology = new MatrixTopology(10,50);
        double[] maxWeight = {200,100};
        DefaultNetwork network = new DefaultNetwork(2,maxWeight,topology);


        ConstantFunctionalFactor constantFactor = new ConstantFunctionalFactor(0.8);
        LearningData fileData = new LearningData("norm.txt");
        WTALearningFunction learning = new WTALearningFunction(network,20,new EuclidesMetric(),fileData,constantFactor);
        //learning.setShowComments(true);
        learning.learn();
        System.out.println(network.getNeuron(72));
        network.networkToFile("after.txt");
    }
}
