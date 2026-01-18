const int SENSOR_PIN = A0;
const int LED_PIN = 13;
const String DEVICE_NAME = "SENSOR_ARDUINO_01";

bool sensorConectado = false;

void setup() {
  Serial.begin(9600);
  pinMode(SENSOR_PIN, INPUT);
  pinMode(LED_PIN, OUTPUT);
  digitalWrite(LED_PIN, LOW);
  
  Serial.println("Arduino Sensor de Humedad Inicializado");
  Serial.println("Dispositivo: " + DEVICE_NAME);
  Serial.println("LED en pin 13 - Apagado por defecto");
}

void loop() {
  if (Serial.available() > 0) {
    String comando = Serial.readStringUntil('\n');
    comando.trim();
    
    if (comando == "GET_NAME") {
      Serial.println(DEVICE_NAME);
    }
    else if (comando == "GET_HUMIDITY") {
      int valorAnalogico = analogRead(SENSOR_PIN);
      int porcentajeHumedad = map(valorAnalogico, 0, 1023, 0, 100);
      Serial.println(porcentajeHumedad);
    }
    else if (comando == "CONNECT") {
      sensorConectado = true;
      digitalWrite(LED_PIN, HIGH);
      Serial.println("CONNECTED");
    }
    else if (comando == "DISCONNECT") {
      sensorConectado = false;
      digitalWrite(LED_PIN, LOW);
      Serial.println("DISCONNECTED");
    }
    else if (comando == "GET_CONNECTION_STATUS") {
      if (sensorConectado) {
        Serial.println("CONNECTED");
      } else {
        Serial.println("DISCONNECTED");
      }
    }
  }
  
  delay(100);
}
