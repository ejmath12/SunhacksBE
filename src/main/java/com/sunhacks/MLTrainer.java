package com.sunhacks;

import com.sunhacks.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

public class MLTrainer {
    public static void main(String args[])throws IOException {
        System.out.println("Recommendation System Ratings!!!");
        String UserItem = args[0];
        String SimPCJ = args[1];
        int[][] matrix = createMatrix();
        int[][] test = {{},{}}; //take from program arguments
        for(float f : cosineSimilarityUser(matrix, test)) {

        }

    }

    @Autowired
    private static EventRepository eventRepository;

    public static int[][] createMatrix()throws FileNotFoundException, IOException
    {

        // Initialize the matrix with -1 for all elements
        int[][] matrix = new int[6040][3952];
        for (int i = 0; i<matrix.length; ++i)
        {
            Arrays.fill(matrix[i], -1);
        }

        // Read the input values and form the full matrix
        BufferedReader br = new BufferedReader(new FileReader("ratings.csv"));
        StringTokenizer st = null;
        String row;
        while ((row = br.readLine()) != null)
        {
            st = new StringTokenizer(row, ",");
            while(st.hasMoreTokens())
            {
                int user = Integer.parseInt(st.nextToken());
                int event = Integer.parseInt(st.nextToken());
                int rating = Integer.parseInt(st.nextToken());
                matrix[user-1][event-1] = rating;
                st.nextToken();
            }
        }
        return matrix;
    }
        public static float[] cosineSimilarityUser(int[][] matrix,int[][] test)
    {
        int len = test.length;
        int lenUsers = matrix.length;
        int lenMovies = matrix[0].length;
        int user = 0;
        int event = 0;
        float[] opRating = new float[len];
        for (int i = 0; i < len; ++i)
        {
            user = test[i][0];
            event = test[i][1];
            float upperNum = 0;
            float upperDenom = 0;
            float n = 0;
            for (int j = 0; j < lenUsers; ++j)
            {

                if(matrix[j][event-1] != -1)
                {
                    n++;
                    float num = 0;
                    float denom1 = 0;
                    float denom2 = 0;
                    boolean flag = false;
                    for (int k = 0; k < lenMovies; ++k)
                    {
                        if ((matrix[user-1][k] != -1) && (matrix[j][k] != -1))
                        {
                            flag = true;
                            num += (float)matrix[user-1][k]*matrix[j][k];
                            denom1 += (float)matrix[user-1][k]*matrix[user-1][k];
                            denom2 += (float)matrix[j][k]*matrix[j][k];
                        }
                    }
                    if (flag)
                    {
                        upperDenom += num/(Math.sqrt(denom1*denom2));
                        upperNum += matrix[j][event-1]*num/(Math.sqrt(denom1*denom2));
                    }
                }
            }

            float predrating = 0;

            if (upperDenom > 0)
            {
                //predrating = upperNum/upperDenom;
                predrating = upperNum/n;
            }
            else
            {
                predrating = upperNum;
            }
            opRating[i] = predrating;
        }
        return opRating;
    }


}
