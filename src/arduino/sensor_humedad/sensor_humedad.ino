const int SENSOR_PIN = A0;
const String DEVICE_NAME = "SENSOR_ARDUINO_01";

void setup() {
  Serial.begin(9600);
  pinMode(SENSOR_PIN, INPUT);
  
  Serial.println("Arduino Sensor de Humedad Inicializado");
  Serial.println("Dispositivo: " + DEVICE_NAME);
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
  }
  
  delay(100);
}
