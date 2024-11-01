package com.example.player;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebService extends AsyncTask<Void, Integer, String> {
    private TextView resultView;
    private String urlString;
    private Player playerToSend;
    private String requestMethod;

    public WebService(TextView resultView, String urlString, String requestMethod, Player playerToSend) {
        this.resultView = resultView;
        this.urlString = urlString;
        this.requestMethod = requestMethod;
        this.playerToSend = playerToSend;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        resultView.setText("A processar...");
    }

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");

            if ("POST".equals(requestMethod) && playerToSend != null) {
                connection.setDoOutput(true);
                JSONObject playerJson = playerToSend.toJson();

                // Envia os dados no corpo da requisição
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = playerJson.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            int responseCode = connection.getResponseCode();
            Log.d("WebServiceTask", "Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                Log.d("WebServiceTask", "Response: " + response.toString());
            } else {
                Log.e("WebServiceTask", "Erro na resposta: " + responseCode);
            }
        } catch (Exception e) {
            Log.e("WebServiceTask", "Erro ao conectar com o serviço", e);
        }

        return response.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result != null && !result.isEmpty()) {
            try {

                // Interpretar o JSON
                JSONArray playersArray = new JSONArray(result);
                StringBuilder formattedResult = new StringBuilder();

                // Percorrer cada jogador no JSON
                for (int i = 0; i < playersArray.length(); i++) {
                    JSONObject playerObject = playersArray.getJSONObject(i);
                    int id = playerObject.getInt("id");
                    String name = playerObject.getString("name");
                    String nationality = playerObject.getString("nationality");
                    String birthDate = playerObject.getString("birthDate");
                    int titles = playerObject.getInt("titles");

                    // Formatando o conteúdo
                    formattedResult.append("ID: ").append(id).append("\n")
                            .append("Nome: ").append(name).append("\n")
                            .append("Nacionalidade: ").append(nationality).append("\n")
                            .append("Data de Nascimento: ").append(birthDate).append("\n")
                            .append("Títulos: ").append(titles).append("\n\n");
                }


                resultView.setText(formattedResult.toString());

            } catch (Exception e) {
                resultView.setText("Erro ao interpretar a resposta.");
            }
        } else {
            resultView.setText("Erro ao processar a solicitação");
        }
    }


}


