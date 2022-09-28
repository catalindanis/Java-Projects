import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsExchange;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.util.Timer;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static boolean exitRequested = false;
    public static String lastAction = "";
    public static String lastCategory = "";

    public static void main(String[] args) throws java.lang.InterruptedException, IOException {
        try{
            for(int i=0;i<args.length;i++){
                if(args[i].equals("-access"))
                    Authorization.accessPoint = args[i+1];
                if(args[i].equals("-resource"))
                    Playlist.accessPoint = args[i+1];
                if(args[i].equals("-page"))
                    Menu.entries = Integer.valueOf(args[i+1]);
            }
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException){}
        Menu menu = new Menu();
        lastAction = "";
        while (!exitRequested) {
            String input = getInput();
            menu.processInput(input);
        }
    }

    private static String getInput() {
        return scanner.nextLine();
    }

}

class Authorization{
    private final String CLIENT_ID = "1fd36c09884c424a9a80923c5760aeb9";
    private final String CLIENT_SECRET = "887e4bba4c6243938352812f1ec463a7";
    private String accesCode = null;
    static String accessPoint = "https://accounts.spotify.com";
    static String accessToken = null;
    private boolean authorized = false;
    Authorization() throws IOException,InterruptedException{
        System.out.println("use this link to request the access code:");
        System.out.println(accessPoint+"/authorize?client_id=1fd36c09884c424a9a80923c5760aeb9&redirect_uri=http://localhost:8080&response_type=code");
        System.out.println("waiting for code...");

        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080),0);
        httpServer.start();
        httpServer.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String responseMessage = "Authorization code not found. Try again.";
                if(exchange.getRequestURI().getQuery() != null){
                    String query[] = exchange.getRequestURI().getQuery().split("=");
                    if(query[0].equals("code")) {
                        accesCode = query[1];
                        System.out.println("code received");
                        responseMessage = "Got the code. Return back to your program.";
                    }
                }
                exchange.sendResponseHeaders(200, responseMessage.length());
                exchange.getResponseBody().write(responseMessage.getBytes());
                exchange.getResponseBody().close();
            }
        });
        int time = 0;
        while(accesCode == null && time < 500){
            Thread.sleep(10);
            time += 1;
        }
        httpServer.stop(1);
        if(accesCode == null)
            return;

        System.out.println("making http request for access_token...");
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(accessPoint+"/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(String.format(
                        "grant_type=authorization_code&code=%s&redirect_uri=http://localhost:8080",accesCode
                ))).header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic "+ Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes()))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        accessToken = httpResponse.body().split(",")[0].split(":")[1].replaceAll("\"","");
        System.out.println("response:"+"\n"+httpResponse.body());
        System.out.println("---SUCCESS---");
        authorized = true;
        return;
    }
    public boolean isAuthorized(){
        return authorized;
    }
}

class Playlist{
    static String accessPoint = "https://api.spotify.com";

    public void getCategories(String path,int entries) throws IOException,InterruptedException{

        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.start();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.accessToken)
                .uri(URI.create(accessPoint + path))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        //System.out.println(httpResponse.body());
        try {
            JsonArray categories = JsonParser.parseString(httpResponse.body())
                    .getAsJsonObject().getAsJsonObject("categories").getAsJsonArray("items");
            for (JsonElement jsonElement : categories)
                System.out.println(jsonElement.getAsJsonObject().get("name")
                        .toString().replaceAll("\"", ""));
        }catch (Exception exception){
            JsonObject jsonObject = JsonParser.parseString(httpResponse.body()).getAsJsonObject().get("error").getAsJsonObject();
            System.out.println(jsonObject.get("message").toString().replaceAll("\"",""));
            System.out.println(accessPoint);
        }
        httpServer.stop(1);
    }

    public void getCategories(String path,int entries,int currentPage) throws IOException,InterruptedException{
        if(currentPage < 1) {
            Menu.currentPage = 1;
            System.out.println("No more pages.");
            return;
        }
        int totalPages = 0;
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.start();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.accessToken)
                .uri(URI.create(accessPoint + path))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        //System.out.println(httpResponse.body());
        try {
            JsonArray categories = JsonParser.parseString(httpResponse.body())
                    .getAsJsonObject().getAsJsonObject("categories").getAsJsonArray("items");
            totalPages = categories.size() / entries;
            if(categories.size() % entries != 0)
                totalPages += 1;
            if(currentPage > totalPages) {
                Menu.currentPage = totalPages;
                System.out.println("No more pages.");
                httpServer.stop(1);
                return;
            }
            int startPoint = (currentPage-1) * entries;
            for(int i=startPoint;i<startPoint+entries && i<categories.size();i++)
                System.out.println(categories.get(i).getAsJsonObject().get("name")
                        .toString().replaceAll("\"", ""));
            System.out.println(String.format("---PAGE %d OF %d---",currentPage,totalPages));
        }catch (Exception exception){
            JsonObject jsonObject = JsonParser.parseString(httpResponse.body()).getAsJsonObject().get("error").getAsJsonObject();
            System.out.println(jsonObject.get("message").toString().replaceAll("\"",""));
            System.out.println(accessPoint);
        }
        httpServer.stop(1);
    }

    public void getNewReleases(String path,int entries) throws IOException,InterruptedException{
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080),0);
        httpServer.start();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.accessToken)
                .uri(URI.create(accessPoint+path))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        try{
            JsonArray items = JsonParser.parseString(httpResponse.body())
                    .getAsJsonObject().getAsJsonObject("albums").getAsJsonArray("items");
            for(int i=0;i<items.size();i++) {
                System.out.println(items.get(i).getAsJsonObject().get("name")
                        .toString().replaceAll("\"",""));
                JsonArray artists = items.get(i).getAsJsonObject().get("artists").getAsJsonArray();
                System.out.print("[");
                for(int j=0;j<artists.size()-1;j++){
                    System.out.print(artists.get(j).getAsJsonObject().get("name")
                            .toString().replaceAll("\"","")+", ");
                }
                System.out.print(artists.get(artists.size()-1).getAsJsonObject().get("name")
                        .toString().replaceAll("\"",""));
                System.out.println("]");
                System.out.println(items.get(i).getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify")
                        .toString().replaceAll("\"",""));
                System.out.println();
            }
        }catch (NullPointerException npe){
            JsonObject jsonObject = JsonParser.parseString(httpResponse.body()).getAsJsonObject().get("error").getAsJsonObject();
            System.out.println(jsonObject.get("message").toString().replaceAll("\"",""));
            System.out.println(accessPoint);
        }
        httpServer.stop(1);
    }

    public void getNewReleases(String path,int entries,int currentPage) throws IOException,InterruptedException{
        if(currentPage < 1) {
            Menu.currentPage = 1;
            System.out.println("No more pages.");
            return;
        }
        int totalPages = 0;
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080),0);
        httpServer.start();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.accessToken)
                .uri(URI.create(accessPoint+path))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        try{
            JsonArray items = JsonParser.parseString(httpResponse.body())
                    .getAsJsonObject().getAsJsonObject("albums").getAsJsonArray("items");
            totalPages = items.size() / entries;
            if(items.size() % entries != 0)
                totalPages += 1;
            if(currentPage > totalPages) {
                Menu.currentPage = totalPages;
                System.out.println("No more pages.");
                httpServer.stop(1);
                return;
            }
            int startPoint = (currentPage-1) * entries;
            for(int i=startPoint;i<startPoint+entries && i<items.size();i++) {
                System.out.println(items.get(i).getAsJsonObject().get("name")
                        .toString().replaceAll("\"",""));
                JsonArray artists = items.get(i).getAsJsonObject().get("artists").getAsJsonArray();
                System.out.print("[");
                for(int j=0;j<artists.size()-1;j++){
                    System.out.print(artists.get(j).getAsJsonObject().get("name")
                            .toString().replaceAll("\"","")+", ");
                }
                System.out.print(artists.get(artists.size()-1).getAsJsonObject().get("name")
                        .toString().replaceAll("\"",""));
                System.out.println("]");
                System.out.println(items.get(i).getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify")
                        .toString().replaceAll("\"",""));
                System.out.println();
            }
            System.out.println(String.format("---PAGE %d OF %d---",currentPage,totalPages));
        }catch (NullPointerException npe){
            JsonObject jsonObject = JsonParser.parseString(httpResponse.body()).getAsJsonObject().get("error").getAsJsonObject();
            System.out.println(jsonObject.get("message").toString().replaceAll("\"",""));
            System.out.println(accessPoint);
        }
        httpServer.stop(1);
    }

    public void getFeatured(String path,int entries) throws IOException,InterruptedException{
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080),0);
        httpServer.start();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.accessToken)
                .uri(URI.create(accessPoint+path))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        try{
            JsonArray items = JsonParser.parseString(httpResponse.body())
                    .getAsJsonObject().getAsJsonObject("playlists").getAsJsonArray("items");
            for(int i=0;i<items.size();i++) {
                try{
                    System.out.println(items.get(i).getAsJsonObject().get("name")
                            .toString().replaceAll("\"",""));
                    System.out.println(items.get(i).getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify")
                            .toString().replaceAll("\"",""));
                    System.out.println();
                }catch (IllegalStateException illegalStateException){}
            }
        }catch (NullPointerException npe){
            JsonObject jsonObject = JsonParser.parseString(httpResponse.body()).getAsJsonObject().get("error").getAsJsonObject();
            System.out.println(jsonObject.get("message").toString().replaceAll("\"",""));
            System.out.println(accessPoint);
        }
        httpServer.stop(1);
    }

    public void getFeatured(String path,int entries,int currentPage) throws IOException,InterruptedException{
        if(currentPage < 1) {
            Menu.currentPage = 1;
            System.out.println("No more pages.");
            return;
        }
        int totalPages = 0;
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080),0);
        httpServer.start();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.accessToken)
                .uri(URI.create(accessPoint+path))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        try{
            JsonArray items = JsonParser.parseString(httpResponse.body())
                    .getAsJsonObject().getAsJsonObject("playlists").getAsJsonArray("items");
            for(int i=0;i<items.size();)
                if(items.get(i) == null)
                    items.remove(i);
                else i++;
            totalPages = items.size() / entries;
            if(items.size() % entries != 0)
                totalPages += 1;
            if(currentPage > totalPages) {
                Menu.currentPage = totalPages;
                System.out.println("No more pages.");
                httpServer.stop(1);
                return;
            }
            int startPoint = (currentPage-1) * entries;
            for(int i=startPoint;i<startPoint+entries && i<items.size();i++) {
                try{
                    System.out.println(items.get(i).getAsJsonObject().get("name")
                            .toString().replaceAll("\"",""));
                    System.out.println(items.get(i).getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify")
                            .toString().replaceAll("\"",""));
                    System.out.println();
                }catch (IllegalStateException illegalStateException){}
            }
            System.out.println(String.format("---PAGE %d OF %d---",currentPage,totalPages));
        }catch (NullPointerException npe){
            JsonObject jsonObject = JsonParser.parseString(httpResponse.body()).getAsJsonObject().get("error").getAsJsonObject();
            System.out.println(jsonObject.get("message").toString().replaceAll("\"",""));
            System.out.println(accessPoint);
        }
        httpServer.stop(1);
    }

    public void getPlaylist(String playlistName,int entries) throws IOException,InterruptedException{
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080),0);
        httpServer.start();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.accessToken)
                .uri(URI.create(accessPoint+"/v1/browse/categories"))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        JsonArray categories = JsonParser.parseString(httpResponse.body())
                .getAsJsonObject().getAsJsonObject("categories").getAsJsonArray("items");
        String categoryID = null;
        boolean atLeastOneFound = false;
        for(JsonElement jsonElement : categories) {
            String currentPlaylistName = jsonElement.getAsJsonObject().get("name").toString().replaceAll("\"", "");
            if(currentPlaylistName.equals(playlistName)){
                categoryID = jsonElement.getAsJsonObject().get("id").toString().replaceAll("\"", "");
                break;
            }
        }
        if(categoryID == null) {
            System.out.println("Unknown category name.");
            return;
        }
        httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.accessToken)
                .uri(URI.create(accessPoint+"/v1/browse/categories/"+categoryID+"/playlists"))
                .GET()
                .build();
        httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        try {
            JsonArray playlists = JsonParser.parseString(httpResponse.body())
                    .getAsJsonObject().getAsJsonObject("playlists").getAsJsonArray("items");
            for (int i = 0; i < playlists.size(); i++) {
                if (playlists.get(i) == null)
                    continue;
                System.out.println(playlists.get(i).getAsJsonObject().get("name")
                        .toString().replaceAll("\"", ""));
                System.out.println(playlists.get(i).getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify")
                        .toString().replaceAll("\"", ""));
                System.out.println();
            }
        }catch (Exception exception){
            JsonObject jsonObject = JsonParser.parseString(httpResponse.body()).getAsJsonObject().get("error").getAsJsonObject();
            System.out.println(jsonObject.get("message").toString().replaceAll("\"",""));
            System.out.println(accessPoint);
        }
        httpServer.stop(1);
    }

    public void getPlaylist(String playlistName,int entries,int currentPage) throws IOException,InterruptedException{
        if(currentPage < 1) {
            Menu.currentPage = 1;
            System.out.println("No more pages.");
            return;
        }
        int totalPages = 0;
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080),0);
        httpServer.start();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.accessToken)
                .uri(URI.create(accessPoint+"/v1/browse/categories"))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        JsonArray categories = JsonParser.parseString(httpResponse.body())
                .getAsJsonObject().getAsJsonObject("categories").getAsJsonArray("items");
        String categoryID = null;
        boolean atLeastOneFound = false;
        for(JsonElement jsonElement : categories) {
            String currentPlaylistName = jsonElement.getAsJsonObject().get("name").toString().replaceAll("\"", "");
            if(currentPlaylistName.equals(playlistName)){
                categoryID = jsonElement.getAsJsonObject().get("id").toString().replaceAll("\"", "");
                break;
            }
        }
        if(categoryID == null) {
            System.out.println("Unknown category name.");
            return;
        }
        httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Authorization.accessToken)
                .uri(URI.create(accessPoint+"/v1/browse/categories/"+categoryID+"/playlists"))
                .GET()
                .build();
        httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        try {
            JsonArray playlists = JsonParser.parseString(httpResponse.body())
                    .getAsJsonObject().getAsJsonObject("playlists").getAsJsonArray("items");
            for(int i=0;i<playlists.size();)
                if(playlists.get(i) == null)
                    playlists.remove(i);
                else i++;
            totalPages = playlists.size() / entries;
            if(playlists.size() % entries != 0)
                totalPages += 1;
            if(currentPage > totalPages) {
                Menu.currentPage = totalPages;
                System.out.println("No more pages.");
                httpServer.stop(1);
                return;
            }
            int startPoint = (currentPage-1) * entries;
            for (int i = startPoint; i < startPoint+entries && i<playlists.size(); i++) {
                if (playlists.get(i) == null)
                    continue;
                System.out.println(playlists.get(i).getAsJsonObject().get("name")
                        .toString().replaceAll("\"", ""));
                System.out.println(playlists.get(i).getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify")
                        .toString().replaceAll("\"", ""));
                System.out.println();
            }
            System.out.println(String.format("---PAGE %d OF %d---",currentPage,totalPages));
        }catch (Exception exception){
            JsonObject jsonObject = JsonParser.parseString(httpResponse.body()).getAsJsonObject().get("error").getAsJsonObject();
            System.out.println(jsonObject.get("message").toString().replaceAll("\"",""));
            System.out.println(accessPoint);
        }
        httpServer.stop(1);
    }

    public void displayPage(String category, int entries, int currentPage) throws IOException,InterruptedException{
        switch (category){
            case "new":
                getNewReleases("/v1/browse/new-releases",entries,currentPage);
                break;
            case "featured":
                getFeatured("/v1/browse/featured-playlists",entries,currentPage);
                break;
            case "categories":
                getCategories("/v1/browse/categories",entries,currentPage);
                break;
            default:
                getPlaylist(category.split("playlists ")[1],entries,currentPage);
                break;
        }
    }
}

class Menu{
    boolean isAuthorized = false;
    static int entries = 5;
    static int currentPage = 0;
    public void processInput(String input) throws IOException,InterruptedException{
        switch (input){
            case "auth":
                Authorization authorization = new Authorization();
                isAuthorized = authorization.isAuthorized();
                break;
            case "exit":
                if(Main.lastCategory.equals("")) {
                    System.out.println("---GOODBYE!---");
                    Main.exitRequested = true;
                }
                else{
                    currentPage = 0;
                    Main.lastCategory = "";
                }
                break;
            default:
                if(isAuthorized)
                    displayPlaylist(input);
                else System.out.println("Please, provide access for application.");
                break;
        }
    }

    private void displayPlaylist(String playlistName) throws IOException,InterruptedException{
        Playlist playlist = new Playlist();
        switch (playlistName) {
            case "categories":
                currentPage = 1;
                playlist.displayPage("categories",entries,currentPage);
                Main.lastCategory = "categories";
                break;
            case "new":
                currentPage = 1;
                playlist.displayPage("new",entries,currentPage);
                Main.lastCategory = "new";
                break;
            case "featured":
                currentPage = 1;
                playlist.displayPage("featured",entries,currentPage);
                Main.lastCategory = "featured";
                break;
            case "next":
                currentPage += 1;
                playlist.displayPage(Main.lastCategory,entries,currentPage);
                break;
            case "prev":
                currentPage -= 1;
                playlist.displayPage(Main.lastCategory,entries,currentPage);
                break;
            default:
                currentPage = 1;
                playlist.displayPage(playlistName,entries,currentPage);
                Main.lastCategory = playlistName;
                break;
        }
    }
}