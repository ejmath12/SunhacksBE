package com.sunhacks;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sunhacks.models.*;
import com.sunhacks.repository.EventRepository;
import com.sunhacks.repository.PredictionRepo;
import com.sunhacks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.IOException;
import java.util.*;

@SpringBootApplication
public class MLTrainer implements CommandLineRunner {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PredictionRepo predictionRepo;

    public static void main(String args[]) {
        SpringApplication.run(MLTrainer.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        System.out.println("Recommendation System Ratings!!!");
        cosineSimilarityUser();

        }



    public void cosineSimilarityUser()
    {
        Map<String, Integer> userMap = new HashMap<String, Integer>();
        Map<Integer, String> userRevMap = new HashMap<Integer, String>();
        Map<String, Map<String, Boolean>> userToEventMap = new HashMap<>();
        Map<String, Integer> eventsMap = new HashMap<String, Integer>();
        Map<Integer, String> eventRevMap = new HashMap<Integer, String>();

        List<User> users = userRepository.findAll();
        int counter = 1;
        for (User user : users) {
            userMap.put(user.getUsername(), counter++);
            userRevMap.put(counter-1,user.getUsername());
        }
        counter = 1;
        List<Event> allEvents = eventRepository.findAll();
        for(Event event: allEvents) {
            eventsMap.put(event.getEventName(), counter++);
            eventRevMap.put(counter-1, event.getEventName());
            HashMap hMap = new HashMap<>();
            hMap.put(event.getEventName(),true);
            userToEventMap.put(event.getUsername(), hMap );
        }
        List<List<Integer>> test = new ArrayList<>();
        for (User user : users) {
            for (Event event : allEvents) {
                if(userToEventMap.get(user.getUsername()).get(event.getEventName()) == null) {
                    List<Integer> testList = new ArrayList<>();
                    testList.add(userMap.get(user.getUsername()));
                    testList.add(eventsMap.get(event.getEventName()));
                    test.add(testList);
                }
            }
        }
        List<Event> events1 = eventRepository.findAllByIsRated(true);

        // Initialize the matrix with -1 for all elements
        int[][] matrix = new int[users.size()][eventsMap.keySet().size()];
        for (int i = 0; i<matrix.length; ++i)
        {
            Arrays.fill(matrix[i], -1);
        }
        for(Event e: events1) {
            int user = userMap.get(e.getUsername());
            int event = eventsMap.get(e.getEventName());
            int rating = e.getEventRating();
            matrix[user-1][event-1] = rating;
        }
        List<Prediction> predictions = new ArrayList<>();
        int len = test.size();
        int lenUsers = matrix.length;
        int lenEvents = matrix[0].length;
        int user = 0;
        int event = 0;
        float[] opRating = new float[len];
        for (int i = 0; i < len; ++i)
        {
            Prediction prediction = new Prediction();
            user = test.get(i).get(0);
            event = test.get(i).get(1);
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
                    for (int k = 0; k < lenEvents; ++k)
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
            prediction.setId(new Key(userRevMap.get(user), eventRevMap.get(event)));
            prediction.setPredictedRating(predrating);
            predictions.add(prediction);
        }
        predictionRepo.save(predictions);
    }
}
