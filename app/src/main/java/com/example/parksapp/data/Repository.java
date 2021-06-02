package com.example.parksapp.data;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.parksapp.controller.AppController;
import com.example.parksapp.model.Activities;
import com.example.parksapp.model.EntranceFees;
import com.example.parksapp.model.Images;
import com.example.parksapp.model.OperatingHours;
import com.example.parksapp.model.Park;
import com.example.parksapp.model.StandardHours;
import com.example.parksapp.model.Topics;
import com.example.parksapp.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    static List<Park> parkList = new ArrayList<>();

    public static void getParks(final AsyncResponse callback, String statCode) {
        String url = Util.getParksUrl(statCode);
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                    try {
                        JSONArray jsonArrayRequest = response.getJSONArray("data");
                        for (int i = 0; i < jsonArrayRequest.length(); i++) {
                            Park park = new Park();
                            JSONObject object = jsonArrayRequest.getJSONObject(i);
                            park.setId(object.getString("id"));
                            park.setFullName(object.getString("fullName"));
                            park.setLatitude(object.getString("latitude"));
                            park.setLongitude(object.getString("longitude"));
                            park.setParkCode(object.getString("parkCode"));
                            park.setStates(object.getString("states"));

                            JSONArray imagesArray = object.getJSONArray("images");
                            List<Images> images = new ArrayList<>();
                            for (int j = 0; j < imagesArray.length(); j++) {
                                Images image = new Images();
                                image.setCaption(imagesArray.getJSONObject(j).getString("credit"));
                                image.setTitle(imagesArray.getJSONObject(j).getString("title"));
                                image.setUrl(imagesArray.getJSONObject(j).getString("url"));

                                images.add(image);

                            }
                            park.setImages(images);

                            park.setWeatherInfo(object.getString("weatherInfo"));
                            park.setName(object.getString("name"));
                            park.setDesignation(object.getString("designation"));

                            //setup Activities
                            JSONArray activityArray = object.getJSONArray("activities");
                            List<Activities> activitiesList = new ArrayList<>();
                            for (int j = 0; j < activityArray.length(); j++) {
                                Activities activities = new Activities();
                                activities.setId(activityArray.getJSONObject(j).getString("id"));
                                activities.setName(activityArray.getJSONObject(j).getString("name"));
                                activitiesList.add(activities);
                            }
                            park.setActivities(activitiesList);

                            //Topics
                            JSONArray topicsArray = object.getJSONArray("topics");
                            List<Topics> topicsList = new ArrayList<>();
                            for (int j = 0; j < topicsArray.length(); j++) {
                                Topics topics = new Topics();
                                topics.setId(topicsArray.getJSONObject(j).getString("id"));
                                topics.setName(topicsArray.getJSONObject(j).getString("name"));
                                topicsList.add(topics);
                            }
                            park.setTopics(topicsList);

                            //Working Hours
                            JSONArray opHours = object.getJSONArray("operatingHours");
                            List<OperatingHours> operatingHours = new ArrayList<>();
                            for (int j = 0; j < opHours.length(); j++) {
                                OperatingHours op = new OperatingHours();
                                op.setDescription(opHours.getJSONObject(j).getString("description"));
                                StandardHours standardHours = new StandardHours();
                                JSONObject hoursJsonObject = opHours.getJSONObject(j).getJSONObject("standardHours");

                                standardHours.setWednesday(hoursJsonObject.getString("wednesday"));
                                standardHours.setThursday(hoursJsonObject.getString("thursday"));
                                standardHours.setFriday(hoursJsonObject.getString("friday"));
                                standardHours.setSaturday(hoursJsonObject.getString("saturday"));
                                standardHours.setSunday(hoursJsonObject.getString("sunday"));
                                standardHours.setMonday(hoursJsonObject.getString("monday"));
                                standardHours.setTuesday(hoursJsonObject.getString("tuesday"));
                                op.setStandardHours(standardHours);

                                operatingHours.add(op);
                            }
                            park.setOperatingHours(operatingHours);

                            park.setDirectionsInfo(object.getString("directionsInfo"));

                            park.setDescription(object.getString("description"));

                            //Entrance Fees
                            JSONArray entranceFeesArray = object.getJSONArray("entranceFees");
                            List<EntranceFees> entranceFees = new ArrayList<>();
                            for (int j =0; j<entranceFeesArray.length(); j++){
                                EntranceFees entranceFees1 = new EntranceFees();
                                entranceFees1.setCost(entranceFeesArray.getJSONObject(j).getString("cost"));
                                entranceFees1.setDescription(entranceFeesArray.getJSONObject(j).getString("description"));
                                entranceFees1.setTitle(entranceFeesArray.getJSONObject(j).getString("title"));
                                entranceFees.add(entranceFees1);
                            }
                            park.setEntranceFees(entranceFees);
                            park.setWeatherInfo(object.getString("weatherInfo"));



                            parkList.add(park);

                        }
                        if (null != callback) {
                            callback.processPark(parkList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

}
