import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;

public class WeatherAppGui extends JFrame {
    private JSONObject weatherData;
    public WeatherAppGui(){
        //Setup Our GUi and add a title
        super("Weather App");

        //Configure gui to end the programs process once it has been closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Set the size of gui
        setSize(450,650);

        //load our gui in the center
        setLocationRelativeTo(null);

        //Make our gui layout manger null to manually position our components within the gui
        setLayout(null);

        //Prevent any resize of our gui
        setResizable(false);

        addGuiComponents();
    }

    private void addGuiComponents() {
        //Search text-field
        JTextField searchTextField = new JTextField();

        //Set Location and size of the field
        searchTextField.setBounds(15,15,351,45);

        //Set the font size and style
        searchTextField.setFont(new Font("Dialog",Font.PLAIN,24));

        add(searchTextField);

        //Weather Image
        JLabel weatherConditionImage = new JLabel(loadImage("src/resources/icons/cloudy.png"));
        weatherConditionImage.setBounds(0,125,450,217);
        add(weatherConditionImage);

        //Temperature Text
        JLabel temperatureText = new JLabel("-- C");
        temperatureText.setBounds(0,350,450,54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD,48));

        //Center The text
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        //Weather Description
        JLabel weatherConditionDesc = new JLabel("--");
        weatherConditionDesc.setBounds(0,405,450,36);
        weatherConditionDesc.setFont(new Font("Dialog",Font.PLAIN,32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        //Humidity Image
        JLabel humidityImage = new JLabel(loadImage("src/resources/icons/humidity.png"));
        humidityImage.setBounds(15,500,74,66);
        add(humidityImage);

        //humidity text
        JLabel humidityText = new JLabel("<html><b>Humidity</b> --%</html>");
        humidityText.setBounds(90,485,85,85);
        humidityText.setFont(new Font("Dialog",Font.PLAIN,16));
        add(humidityText);

        //WindSpeed Image
        JLabel windSpeedImage = new JLabel(loadImage("src/resources/icons/windspeed.png"));
        windSpeedImage.setBounds(220,500,74,66);
        add(windSpeedImage);

        //WindSpeed Text
        JLabel windSpeedText = new JLabel("<html><b>Windspeed</b> --km/h</html>");
        windSpeedText.setBounds(310,500,85,55);
        windSpeedText.setFont(new Font("Dialog",Font.PLAIN,16));
        add(windSpeedText);

        //Search Button
        JButton searchButton = new JButton(loadImage("src/resources/icons/search.png"));

        //change the cursor to a hand cursor when hovering over the button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(366,15,47,44);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get location from user
                String userInput = searchTextField.getText();

                //validate input - remove whitespace to ensure non-empty text
                if (userInput.replaceAll("\\s","").length() <= 0){
                    return;
                }
                //retrieve weather data
                weatherData = WeatherApp.getWeatherData(userInput);

                //update gui

                //update weather image
                String weatherCondition = (String) weatherData.get("weather_condition");

                //depending on the condition, we will update the weather image that corresponds with the condition
                switch (weatherCondition){
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src/resources/icons/clear.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/resources/icons/cloudy.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/resources/icons/rain.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/resources/icons/snow.png"));
                        break;
                }
                //update temperature text
                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + "c");
                //update weather condition text
                weatherConditionDesc.setText(weatherCondition);
                //update humidity text
                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");
                //update wind speed text
                double windspeed = (double) weatherData.get("windspeed");
                windSpeedText.setText("<html><b>Windspeed</b> " + windspeed + "km/h</html>");

            }
        });
        add(searchButton);
    }
    private ImageIcon loadImage(String resourcePack){
        try{
            //read the image
            BufferedImage image = ImageIO.read(new File(resourcePack));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Could not find resource");
        return null;
    }
}
